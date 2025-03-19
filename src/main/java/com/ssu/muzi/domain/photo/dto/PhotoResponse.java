package com.ssu.muzi.domain.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
