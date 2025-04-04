package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthErrorCode implements ErrorCode {
    DUPLICATE_LOGIN_ID(404, "EO000", "중복된 아이디입니다."),
    INVALID_PASSWORD(404, "EM001", "비밀번호가 비어 있거나 8자 이하인지 확인해주세요."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
