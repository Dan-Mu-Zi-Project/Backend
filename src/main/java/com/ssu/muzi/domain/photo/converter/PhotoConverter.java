package com.ssu.muzi.domain.photo.converter;

import com.ssu.muzi.domain.photo.dto.PhotoRequest;
import com.ssu.muzi.domain.photo.dto.PhotoResponse;
import com.ssu.muzi.domain.photo.entity.Photo;
import com.ssu.muzi.domain.photo.entity.PhotoDownloadLog;
import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import com.ssu.muzi.domain.shareGroup.dto.ProfileResponse;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.engine.messageinterpolation.el.RootResolver.FORMATTER;

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
        LocalDateTime takeAt = null;
        if (photoUpload.getTakedAt() != null && !photoUpload.getTakedAt().trim().isEmpty()) {
            try {
                takeAt = LocalDateTime.parse(photoUpload.getTakedAt(), DATE_FORMATTER);
            } catch (Exception e) {
                // 파싱에 실패하면 null로 유지
            }
        }

        return Photo.builder()
                .photoUrl(photoUpload.getImageUrl())
                .location(photoUpload.getLocation())
                .takeAt(takeAt)
                .uploaderProfileId(uploaderProfileId)
                .width(photoUpload.getWidth())
                .height(photoUpload.getHeight())
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

    // 특정 사진의 상세정보 반환
    public PhotoResponse.PhotoDetailInfo toPhotoDetail(Photo photo, Profile uploaderProfile, List<Profile> participantProfiles) {
        return PhotoResponse.PhotoDetailInfo
                .builder()
                .photoId(photo.getId())
                .photoUrl(photo.getPhotoUrl())
                .takeDate(photo.getTakeAt())
                .location(photo.getLocation())
                .uploaderInfo(ProfileResponse.ParticipantInfo
                        .builder()
                        .profileId(uploaderProfile.getId())
                        .memberImageUrl(uploaderProfile.getMember().getMemberImageUrl())
                        .name(uploaderProfile.getMember().getName())
                        .build())
                .participantInfoList(
                        participantProfiles
                                .stream()
                                .map(profile -> ProfileResponse.ParticipantInfo
                                        .builder()
                                        .profileId(profile.getId())
                                        .memberImageUrl(profile.getMember().getMemberImageUrl())
                                        .name(profile.getMember().getName())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }


    // 특정 앨범의 사진 리스트 응답 시, 하나의 사진 정보를 반환
    public PhotoResponse.PhotoPreviewInfo toPhotoPreview(Photo photo, boolean isLikedByUser, boolean isDownloadedByUser) {
        return PhotoResponse.PhotoPreviewInfo
                .builder()
                .photoId(photo.getId())
                .photoUrl(photo.getPhotoUrl())
                .isLikedByUser(isLikedByUser)
                .isDownloadedByUser(isDownloadedByUser)
                .width(photo.getWidth())
                .height(photo.getHeight())
                .build();
    }

    // 랜덤 photo를 위한 사진 정보 반환
    public PhotoResponse.RandomPhotoPreviewInfo toRandomPhotoPreview(Photo photo) {
        return PhotoResponse.RandomPhotoPreviewInfo
                .builder()
                .photoId(photo.getId())
                .photoUrl(photo.getPhotoUrl())
                .width(photo.getWidth())
                .height(photo.getHeight())
                .build();
    }

    // 특정 앨범의 사진 리스트 응답 시, 최종 응답용 PagedPhoto DTO로 변환
    public PhotoResponse.PagedPhotoInfo toPagedPhotoInfo(Page<PhotoResponse.PhotoPreviewInfo> dtoPage, Long shareGroupId, Long albumProfileId) {
        return PhotoResponse.PagedPhotoInfo
                .builder()
                .shareGroupId(shareGroupId)
                .profileId(albumProfileId)
                .photoPreviewList(dtoPage.getContent())
                .page(dtoPage.getTotalPages())
                .totalElements(dtoPage.getTotalElements())
                .isFirst(dtoPage.isFirst())
                .isLast(dtoPage.isLast())
                .build();
    }

    // 특정 프로필 리스트가 포함된 사진 리스트 응답 시, 최종 응답용 PagedPhoto DTO로 변환
    public PhotoResponse.PagedPhotoFilterInfo toPagedPhotoFilterInfo(Page<PhotoResponse.PhotoPreviewInfo> dtoPage, Long shareGroupId) {
        return PhotoResponse.PagedPhotoFilterInfo
                .builder()
                .shareGroupId(shareGroupId)
                .photoPreviewList(dtoPage.getContent())
                .page(dtoPage.getTotalPages())
                .totalElements(dtoPage.getTotalElements())
                .isFirst(dtoPage.isFirst())
                .isLast(dtoPage.isLast())
                .build();
    }

    // 사진 삭제 시, 삭제 개수 반환
    public PhotoResponse.PhotoDeleteInfo toPhotoDeleteInfo(int deletedCount) {
        return PhotoResponse.PhotoDeleteInfo
                .builder()
                .deletedCount(deletedCount)
                .build();
    }

    // 사진 Id만 반환
    public PhotoResponse.PhotoId toPhotoId(Photo photo) {
        return PhotoResponse.PhotoId
                .builder()
                .photoId(photo.getId())
                .build();
    }

}
