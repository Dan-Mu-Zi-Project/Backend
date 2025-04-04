package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthErrorCode implements ErrorCode {
    DUPLICATE_LOGIN_ID(404, "EO000", "중복된 아이디입니다."),
    INVALID_PASSWORD(404, "EM001", "비밀번호가 비어 있거나 8자 이하인지 확인해주세요."),
    MEMBER_LOGINID_NOT_FOUND(404, "EM002", "해당 id가 존재하지 않습니다. (전시용 로그인)"),
    NOT_CORRECT_PASSWORD(404, "EM003", "해당 패스워드는 유효하지 않습니다. (전시용 로그인)"),

    ;

    private final int status;
    private final String code;
    private final String message;
}
