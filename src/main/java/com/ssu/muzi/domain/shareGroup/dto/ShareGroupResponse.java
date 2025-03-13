package com.ssu.muzi.domain.shareGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
}
