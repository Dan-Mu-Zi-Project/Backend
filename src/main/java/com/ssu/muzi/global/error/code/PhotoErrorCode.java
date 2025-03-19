package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoErrorCode implements ErrorCode {
    PHOTO_NOT_FOUND(404, "EP000", "해당 photoId를 가진 사진이 존재하지 않습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
