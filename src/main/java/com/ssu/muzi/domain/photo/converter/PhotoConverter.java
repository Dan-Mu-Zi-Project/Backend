package com.ssu.muzi.domain.photo.converter;

import com.ssu.muzi.domain.photo.dto.PhotoRequest;
import com.ssu.muzi.domain.photo.dto.PhotoResponse;
import com.ssu.muzi.domain.photo.entity.Photo;
import com.ssu.muzi.domain.photo.entity.PhotoDownloadLog;
import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoConverter {

    private final ProfileService profileService;
    // takedAt 날짜 형식: "yyyy-MM-dd HH:mm:ss" (초까지 포함)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //presigned url 리스트
    public PhotoResponse.PreSignedUrlList toPreSignedUrlList(List<PhotoResponse.PreSignedUrl> preSignedUrlList) {
        List<PhotoResponse.PreSignedUrl> preSignedUrlInfoList = preSignedUrlList.stream()
                .map(preSignedUrlInfo -> toPreSignedUrl(
                        preSignedUrlInfo.getPreSignedUrl(),
                        preSignedUrlInfo.getPhotoUrl(),
                        preSignedUrlInfo.getPhotoName()
                ))
                .collect(Collectors.toList());

        return PhotoResponse.PreSignedUrlList
                .builder()
                .preSignedUrlInfoList(preSignedUrlInfoList)
                .build();
    }

    public PhotoResponse.PreSignedUrl toPreSignedUrl(String preSignedUrl, String photoUrl, String photoName) {
        return PhotoResponse.PreSignedUrl
                .builder()
                .preSignedUrl(preSignedUrl)
                .photoUrl(photoUrl)
                .photoName(photoName)
                .build();
    }

    // ------- presigned 끝

    // 프론트에서 요청온 photo 를 엔티티로 변환하는 로직
    public Photo toPhotoEntity(PhotoRequest.PhotoUpload photoUpload, Long uploaderProfileId) {
        LocalDateTime takeAt = LocalDateTime.parse(photoUpload.getTakedAt(), DATE_FORMATTER);

        return Photo.builder()
                .photoUrl(photoUpload.getImageUrl())
                .location(photoUpload.getLocation())
                .takeAt(takeAt)
                .uploaderProfileId(uploaderProfileId)
                .build();
    }

    // 요청온 photo 1개에 엮여 있는 profile List를 저장하기 위해, 엔티티로 변환하는 로직
    public List<PhotoProfileMap> toPhotoProfileMapEntities(Photo photo, List<Long> profileIdList) {
        return profileIdList
                .stream()
                .map(pid -> {
                    Profile profile = profileService.findProfile(pid);
                    return PhotoProfileMap
                            .builder()
                            .photo(photo)
                            .profile(profile)
                            .build();
                    })
                .collect(Collectors.toList());
    }

    // 특정 공유그룹에 사진 업로드 시, 응답을 위한 DTO
    public PhotoResponse.UploadPhoto toUploadPhotoDTO(Photo photo, List<Long> profileIdList) {
        return PhotoResponse.UploadPhoto.builder()
                .photoId(photo.getId())
                .imageUrl(photo.getPhotoUrl())
                .profileIdList(profileIdList)
                .build();
    }

    // 사진 다운로드 시, 응답 반환 (다운로드한 사진의 개수)
    public PhotoResponse.PhotoDownload toPhotoDownloadLog(List<PhotoDownloadLog> logs) {
        return PhotoResponse.PhotoDownload
                .builder()
                .downloadedCount(logs.size())
                .build();
    }

}
