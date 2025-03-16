package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileErrorCode implements ErrorCode {
    PROFILE_NOT_FOUND(404, "EP000", "입력한 프로필 id가 유효하지 않아 프로필을 찾을 수 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
