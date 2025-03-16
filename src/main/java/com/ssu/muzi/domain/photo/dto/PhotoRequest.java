package com.ssu.muzi.domain.photo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class PhotoRequest {

    // presigned url 요청
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreSignedUrlRequest {
        @NotEmpty(message = "사진의 이름은 하나 이상이어야 합니다.")
        private List<String> photoNameList;
    }

    // 사진 리스트 요청
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoUploadList {
        @NotEmpty(message = "요청하는 photo 리스트는 비어있을 수 없습니다.")
        private List<PhotoUpload> photoList;
    }

    // 사진 1개 요청
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoUpload {
        @NotEmpty(message = "imageUrl은 필수입니다.")
        private String imageUrl;
        @NotEmpty(message = "사진에 포함된 인물의 profileId 리스트는 필수입니다.")
        private List<Long> profileIdList;
        private String location;
        private String takedAt;
    }
}
