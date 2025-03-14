package com.ssu.muzi.domain.shareGroup.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class ProfileResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantInfo {
        private Long profileId;
        private String name;
        private String memberImageUrl;
    }
}
