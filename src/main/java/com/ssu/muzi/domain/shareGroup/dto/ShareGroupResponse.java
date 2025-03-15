package com.ssu.muzi.domain.shareGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public abstract class ShareGroupResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvitationInfo {
        private Long shareGroupId;
        private String groupName;
        private String description;
        private String place;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startedAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endedAt;
        private String ownerName;
        private String ownerImageUrl;
        private List<ProfileResponse.ParticipantInfo> participantInfoList;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinInfo {
        private Long shareGroupId;
        private Long profileId;
        private LocalDateTime joinedAt;
    }

    // 그룹의 임베딩벡터 응답 반환하는 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupVector {
        private Long shareGroupId;
        private List<MemberResponse.MemberEmbedding> memberEmbeddingList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupId {
        private Long shareGroupId;
    }
}
