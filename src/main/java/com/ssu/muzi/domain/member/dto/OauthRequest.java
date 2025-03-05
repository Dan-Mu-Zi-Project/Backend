package com.ssu.muzi.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class OauthRequest {

    @Getter
    public static class FrontAccessTokenInfo {
        @NotNull(message = "프론트 액세스토큰을 필수로 입력해야 합니다.")
        private String accessToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotNull(message = "카카오에서 제공한 회원 번호를 필수로 입력해야 합니다.")
        private Long authId;
    }
}
