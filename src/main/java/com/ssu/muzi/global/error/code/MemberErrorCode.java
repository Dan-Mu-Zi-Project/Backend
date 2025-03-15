package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND_BY_MEMBER_ID(404, "EM000", "해당 memberId를 가진 회원이 존재하지 않습니다."),
    MEMBER_ALREADY_SIGNUP(400, "EM001", "해당 이메일을 가진 회원은 이미 회원가입하였습니다."),
    MEMBER_NOT_FOUND_BY_REFRESH_TOKEN(404, "EM002", "해당 리프레쉬 토큰을 가진 회원이 존재하지 않습니다."),
    MEMBER_NAME_BLANK(404,"EM003", "이름은 비어있으면 안 됩니다."),
    MEMBER_IMAGE_BLANK(404, "EM004", "프로필 사진 url이 비어 있습니다."),
    INVALID_SAMPLE_IMAGE_COUNT(404, "EM005", "촬영된 샘플이미지 개수가 3개 미만입니다. FRONT, LEFT, RIGHT 3개를 촬영해 주세요."),
    INVALID_ANGLE_TYPE(404, "EM006", "유효한 앵글 타입이 아닙니다. FRONT, LEFT, RIGHT 중 입력해 주세요."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
