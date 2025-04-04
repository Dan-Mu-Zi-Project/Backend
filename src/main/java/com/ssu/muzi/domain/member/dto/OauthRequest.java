package com.ssu.muzi.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    // 전시용 회원가입
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExhibitionAddRequest {
        @NotEmpty(message = "닉네임은 필수로 입력해야 합니다.")
        @Size(min = 2, max = 10, message = "닉네임은 최소 2자, 최대 5자까지 입력 가능합니다.")
        private String name;
        @NotEmpty(message = "아이디 필수로 입력해야 합니다.")
        private String loginId;
        @NotEmpty(message = "비밀번호는 필수로 입력해야 합니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상 입력해야 합니다.")
        private String loginPassword;
    }

    // 전시용 로그인
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExhibitionLoginRequest {
        @NotEmpty(message = "아이디 필수로 입력해야 합니다.")
        private String loginId;
        @NotEmpty(message = "비밀번호는 필수로 입력해야 합니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상 입력해야 합니다.")
        private String loginPassword;
    }

}
