package com.ssu.muzi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public abstract class  OauthResponse {
    @Getter
    @Setter
    public static class ServerAccessTokenInfo {
        private String accessToken;
        private Long memberId;
        private Boolean isFaceCaptured;
    }

    // 카카오에서 유저 정보를 받아오는 응답 객체
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class KakaoInfo {
        private Long authId;
        private String name; //카카오 설정 이름
        private String email; //카카오 설정 이메일
        private String refreshToken;
    }

    // authId로 회원가입 했는지 알아보기
    @Getter
    @AllArgsConstructor
    public static class CheckMemberRegistration {
        private Boolean isRegistered;
    }

    @Getter
    @Setter
    public static class RefreshTokenResponse {
        private String accessToken;
    }
}
