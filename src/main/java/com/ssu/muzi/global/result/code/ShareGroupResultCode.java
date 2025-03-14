package com.ssu.muzi.global.result.code;

import com.ssu.muzi.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShareGroupResultCode implements ResultCode {
    CREATE_SHARE_GROUP(200, "SG000", "성공적으로 그룹을 생성했습니다."),
    UPDATE_SHARE_GROUP(200, "SG002", "성공적으로 그룹 정보를 수정했습니다."),
    JOIN_SHARE_GROUP(200, "SG003", "성공적으로 그룹에 가입하였습니다."),
    SAVE_SAMPLE_IMAGE(200, "SG004", "성공적으로 샘플이미지를 저장하였습니다."),
    GET_VECTORLIST(200, "SG005", "성공적으로 그룹에 속한 모든 멤버의 벡터값을 반환하였습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
