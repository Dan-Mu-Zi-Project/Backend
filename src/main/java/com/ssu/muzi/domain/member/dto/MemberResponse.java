package com.ssu.muzi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public abstract class MemberResponse {

    @Getter
    @AllArgsConstructor
    public static class CheckMemberRegistration {
        private Boolean isRegistered;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberInfo {
        private Long memberId;
        private Long authId;
        private String name;
        private String memberImageUrl;
        private Boolean onlyWifi;
    }

    // 각 회원의 벡터 정보 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberEmbedding {
        private Long memberId;
        private Long profileId;
        private String name;
        private List<EmbeddingVector> embeddingVectorList;
    }

    // 각 각도별 벡터 정보 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbeddingVector {
        private String angleType;
        private String vector;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberId {
        private Long memberId;
    }
}
