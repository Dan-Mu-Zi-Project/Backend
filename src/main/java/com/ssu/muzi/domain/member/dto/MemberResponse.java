package com.ssu.muzi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberId {
        private Long memberId;
    }
}
