package com.ssu.muzi.global.result.code;

import com.ssu.muzi.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberResultCode implements ResultCode {
    LOGIN(200, "SM000", "성공적으로 로그인하였습니다."),
    REFRESH_TOKEN(200, "SM001", "성공적으로 리프레쉬 토큰을 발급했습니다."),
    SAVE_SAMPLE_IMAGE(200, "SG002", "성공적으로 샘플이미지를 저장하였습니다."),
    CHECK_MEMBER_REGISTRATION(200, "SM003", "해당 정보에 대응하는 회원의 가입 여부를 성공적으로 조회하였습니다."),
    MYPAGE_INFO(200, "SM004", "내 정보를 성공적으로 조회하였습니다."),
    SET_NICKNAME(200, "SM005", "성공적으로 이름을 수정하였습니다."),
    SET_IMAGE(200, "SM006", "성공적으로 프로필 사진을 수정하였습니다."),
    SET_WIFI(200, "SM007", "성공적으로 와이파이 다운로드 여부를 변경했습니다."),
    DELETE_MEMBER(200, "SM008", "성공적으로 회원 탈퇴했습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
