package com.ssu.muzi.domain.photo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.photo.converter.PhotoConverter;
import com.ssu.muzi.domain.photo.dto.PhotoRequest;
import com.ssu.muzi.domain.photo.dto.PhotoResponse;
import com.ssu.muzi.domain.photo.entity.Photo;
import com.ssu.muzi.domain.photo.entity.PhotoDownloadLog;
import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import com.ssu.muzi.domain.photo.repository.PhotoDownloadLogRepository;
import com.ssu.muzi.domain.photo.repository.PhotoProfileMapRepository;
import com.ssu.muzi.domain.photo.repository.PhotoRepository;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.service.ProfileService;
import com.ssu.muzi.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ssu.muzi.global.error.code.PhotoErrorCode.PHOTO_NOT_FOUND;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final AmazonS3 amazonS3;
    private final PhotoConverter photoConverter;
    private final ProfileService profileService;
    private final PhotoRepository photoRepository;
    private final PhotoProfileMapRepository photoProfileMapRepository;
    private final PhotoDownloadLogRepository photoDownloadLogRepository;

    @Value("${spring.cloud.aws.s3.photo-bucket}")
    private String BUCKET_NAME;
    @Value("${spring.cloud.aws.region.static}")
    private String REGION;

    public static final String RAW_PATH_PREFIX = "photo";

    // presigned url 리스트
    @Override
    public List<PhotoResponse.PreSignedUrl> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request,
                                                                Member member) {
        return request.getPhotoNameList()
                .stream()
                .map(this::getPreSignedUrl)
                .collect(Collectors.toList());
    }

    // presigned url
    private PhotoResponse.PreSignedUrl getPreSignedUrl(String originalFilename) {
        String fileName = createPath(originalFilename);
        String photoName = fileName.split("/")[1];
        String photoUrl = generateFileAccessUrl(fileName);

        URL preSignedUrl = amazonS3.generatePresignedUrl(getGeneratePreSignedUrlRequest(BUCKET_NAME, fileName));
        return photoConverter.toPreSignedUrl(preSignedUrl.toString(), photoUrl, photoName);
    }

    // 원본 사진 전체 경로 생성
    private String createPath(String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s", RAW_PATH_PREFIX, fileId + fileName);
    }

    // 사진 고유 ID 생성
    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    // 원본 사진의 접근 URL 생성
    private String generateFileAccessUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", BUCKET_NAME, REGION, fileName);
    }

    // 사진 업로드용(PUT) PreSigned URL 생성
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    // PreSigned URL 유효 기간 설정
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 3;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    // ---------- 여기까지 presigned

    @Override
    // 특정 공유 그룹에 대해, 업로더(로그인한 Member)의 사진 업로드 요청을 처리
    public PhotoResponse.UploadPhotoList uploadPhotos(Long shareGroupId, Member uploader, PhotoRequest.PhotoUploadList request) {

        // 업로더의 해당 공유 그룹에 속한 Profile을 조회
        Profile uploaderProfile = profileService.findProfile(uploader.getId(), shareGroupId);

        // photo 정보를 담기 위한 리스트 생성
        List<PhotoResponse.UploadPhoto> list = new ArrayList<>();

        // 요청 DTO 내의 각 PhotoUpload 처리
        for (PhotoRequest.PhotoUpload photoUpload : request.getPhotoList()) {

            // 각 photoUpload에 포함된 profileId가 실제 존재하는지 검증
            for (Long profileId : photoUpload.getProfileIdList()) {
                profileService.findProfile(profileId);
            }

            // 1. 컨버터를 사용하여 1개의 Photo 엔티티 생성, 저장. (uploaderProfileId 설정)
            Photo photo = photoConverter.toPhotoEntity(photoUpload, uploaderProfile.getId());
            photo = photoRepository.save(photo);

            // 2. 컨버터를 사용하여 해당 사진 1개의 PhotoProfileMap 엔티티 목록 생성 후 저장
            List<PhotoProfileMap> mappingEntities = photoConverter.toPhotoProfileMapEntities(photo, photoUpload.getProfileIdList());
            photoProfileMapRepository.saveAll(mappingEntities);

            // 3. 컨버터를 사용해 응답용 Upload DTO 생성 (사진 1개 응답)
            PhotoResponse.UploadPhoto uploadPhotoDTO = photoConverter.toUploadPhotoDTO(photo, photoUpload.getProfileIdList());
            list.add(uploadPhotoDTO);
        }

        // 최종 응답 DTO 생성: 업로더의 Profile ID와 업로드된 사진 목록 포함
        return PhotoResponse.UploadPhotoList
                .builder()
                .uploaderProfileId(uploaderProfile.getId())
                .uploadPhotoList(list)
                .build();
    }


    // 요청받은 photoIds에 대해, 나(profile)의 다운로드 로그 기록
    @Override
    public PhotoResponse.PhotoDownload recordDownload(Long shareGroupId, Member member, PhotoRequest.PhotoDownload request) {

        // 1. 다운로드할 앨범이 있는, 해당 공유 그룹에 속한 내 Profile을 조회
        Profile profile = profileService.findProfile(member.getId(), shareGroupId);

        // 2. 다운로드 요청받은 photoIds에 대해
        List<PhotoDownloadLog> logs = request.getPhotoIdList()
                .stream()
                .map(photoId -> {
                    // 각 Photo를 조회 (및 검증. 없으면 exception)
                    Photo photo = findPhoto(photoId);
                    // 다운로드 로그 엔티티 생성
                    return PhotoDownloadLog
                            .builder()
                            .profile(profile)
                            .photo(photo)
                            .build();
                }).collect(Collectors.toList());

        // 로그 엔티티들을 저장
        photoDownloadLogRepository.saveAll(logs);

        // 컨버터를 사용하여 응답 DTO로 변환
        return photoConverter.toPhotoDownloadLog(logs);
    }

    private Photo findPhoto(Long photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessException(PHOTO_NOT_FOUND));
    }

}
