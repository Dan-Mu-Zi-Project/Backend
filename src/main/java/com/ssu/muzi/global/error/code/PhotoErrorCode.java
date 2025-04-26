package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoErrorCode implements ErrorCode {
    PHOTO_NOT_FOUND(404, "EP000", "해당 photoId를 가진 사진이 존재하지 않습니다."),
    ALREADY_LIKED(404, "EP001", "사용자가 이미 좋아요를 누른 사진입니다."),
    NOT_LIKED(404, "EP002", "좋아요가 눌려 있지 않은 사진이기 때문에, 좋아요를 취소할 수 없습니다."),
    PHOTO_NOT_ENOUGH(404, "EP003", "사진이 8개 미만이기 때문에 랜덤 사진을 불러올 수 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
