package com.ssu.muzi.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class MemberRequest {

    // 한 사람의 샘플 이미지 DTO 클래스
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleImage {
        @NotEmpty(message = "angleType은 필수 항목입니다.")
        private String angleType;
        @NotEmpty(message = "faceVector는 필수 항목입니다.")
        private String faceVector;  // ex: "[0.23, -0.44, 1.02, ...]"
    }

    // 여러 사람의 샘플 이미지를 가진 DTO 클래스
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleImageList {
        private List<SampleImage> faceSampleList;
    }
}
