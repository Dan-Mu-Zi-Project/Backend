package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShareGroupErrorCode implements ErrorCode {
    ALREADY_EXISTS_PROGRESSING_GROUP(404, "ES000", "해당 기간에 이미 정해진 여행 일정이 존재합니다."),
    STARTEDAT_AFTER_ENDEDAT(404, "ES001", "시작 날짜는 끝나는 날짜보다 이전이어야 합니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
