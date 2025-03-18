package com.ssu.muzi.domain.shareGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public abstract class ShareGroupRequest {

    // 그룹 생성시
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateShareGroupRequest {
        @NotEmpty(message = "공유 그룹 이름은 필수로 입력해야 합니다.")
        @Size(max = 15, message = "그룹명은 최대 15자까지 입력 가능합니다.")
        private String groupName;
        @NotEmpty(message = "그룹 소개는 필수로 입력해야 합니다.")
        @Size(max = 20, message = "그룹 소개는 최대 20자까지 입력 가능합니다.")
        private String description;
        @NotEmpty(message = "여행 장소는 필수로 입력해야 합니다.")
        @Size(max = 10, message = "여행 장소는 최대 10자까지 입력 가능합니다.")
        private String place;
        //분 단위까지 입력
        @NotNull(message = "시작 날짜는 필수로 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime startedAt;
        @NotNull(message = "종료 날짜는 필수로 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime endedAt;
    }

    // 그룹 수정시
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateShareGroupRequest {
        @NotEmpty(message = "공유 그룹 컬러는 필수로 지정해야 합니다.")
        @Size(max = 8, message = "그룹 컬러는 hex 코드로 입력하세요.")
        private String groupColor;
        @NotEmpty(message = "공유 그룹 이름은 필수로 입력해야 합니다.")
        @Size(max = 15, message = "그룹명은 최대 15자까지 입력 가능합니다.")
        private String groupName;
        @NotEmpty(message = "그룹 소개는 필수로 입력해야 합니다.")
        @Size(max = 20, message = "그룹 소개는 최대 20자까지 입력 가능합니다.")
        private String description;
        @NotEmpty(message = "여행 장소는 필수로 입력해야 합니다.")
        @Size(max = 10, message = "여행 장소는 최대 10자까지 입력 가능합니다.")
        private String place;
        //분 단위까지 입력
        @NotNull(message = "시작 날짜는 필수로 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime startedAt;
        @NotNull(message = "종료 날짜는 필수로 입력해야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime endedAt;
    }

    // 그룹 이미지 업로드
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupImageUploadRequest {
        @NotEmpty(message = "이미지 URL은 필수입니다.")
        private String groupImageUrl;
    }
}
