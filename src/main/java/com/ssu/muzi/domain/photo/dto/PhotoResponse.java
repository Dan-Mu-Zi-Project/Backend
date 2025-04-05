package com.ssu.muzi.domain.photo.dto;

import com.ssu.muzi.domain.shareGroup.dto.ProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public abstract class PhotoResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreSignedUrlList {
        private List<PreSignedUrl> preSignedUrlInfoList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreSignedUrl {
        private String preSignedUrl;
        private String photoUrl;
        private String photoName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadPhotoList {
        private Long uploaderProfileId;
        private List<UploadPhoto> uploadPhotoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadPhoto {
        private Long photoId;
        private String imageUrl;
        private List<Long> profileIdList;
    }

    // 사진 다운로드 로그 기록시 응답
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoDownload {
        private int downloadedCount;
    }

    // 상세 사진 정보를 응답
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoDetailInfo {
        private Long photoId;
        private String photoUrl;
        private LocalDateTime takeDate;    // "yyyy-MM-dd HH:mm:ss" 형식
        private String location;
        private ProfileResponse.ParticipantInfo uploaderInfo; // 업로드한 프로필의 info
        private List<ProfileResponse.ParticipantInfo> participantInfoList;
    }

    // 특정 profileId의 사진 리스트를 페이징 조회
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedPhotoInfo {
        private Long shareGroupId;
        private Long profileId; //누구의 앨범인지
        private List<PhotoPreviewInfo> photoPreviewList; //특정 프로필 앨범의 사진 리스트
        private int page; // 페이지 번호
        private long totalElements; // 해당 조건에 부합하는 요소의 총 개수
        private boolean isFirst; // 첫 페이지 여부
        private boolean isLast; // 마지막 페이지 여부
    }

    // 특정 profileId의 사진 리스트 페이징 조회 시, 하나의 사진을 의미
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoPreviewInfo {
        private Long photoId;
        private String photoUrl;
        private boolean isLikedByUser;
        private boolean isDownloadedByUser;
        private Integer width;
        private Integer height;
    }

    // 사진 id를 응답
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoId {
        private Long photoId;
    }

    // 사진 삭제 시
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoDeleteInfo {
        private int deletedCount;
    }
}
