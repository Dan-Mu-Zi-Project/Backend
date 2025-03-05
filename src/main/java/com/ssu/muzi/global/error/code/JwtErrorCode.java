package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {
    AUTHENTICATION_TYPE_IS_NOT_BEARER(400, "EJ001", "인증 타입이 Bearer가 아닙니다."),
    ACCESS_TOKEN_IS_EXPIRED(401, "EJ002", "액세스 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(402, "EJ003", "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(403, "EJ004", "유효하지않은 리프레시 토큰입니다."),
    MEMBER_NOT_FOUND(404, "EJ005", "해당 회원이 존재하지 않습니다. 탈퇴한 회원인지 확인해 주세요."),
    MEMBER_NOT_FOUND_BY_AUTH_ID(405, "EG006", "authId로 멤버를 찾을 수 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
