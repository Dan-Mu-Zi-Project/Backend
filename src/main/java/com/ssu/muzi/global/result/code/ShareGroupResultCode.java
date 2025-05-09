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
    GET_VECTORLIST(200, "SG005", "성공적으로 그룹에 속한 모든 멤버의 벡터값을 반환하였습니다."),
    GET_INVITATION(200, "SG006", "성공적으로 초대장을 조회했습니다."),
    GET_SHAREGROUP_INFO(200, "SG007", "성공적으로 그룹 상세 정보를 조회했습니다."),
    GET_HOME(200, "SG008", "성공적으로 홈 화면을 조회했습니다."),
    UPLOAD_GROUPIMAGE(200, "SG009", "성공적으로 그룹 이미지를 업로드하였습니다."),
    SHARE_GROUP_LIST_INFO(200, "SG010", "성공적으로 그룹 리스트를 조회했습니다."),
    LEAVE_SHARE_GROUP(200, "SG011", "성공적으로 그룹을 탈퇴했습니다."),
    DELETE_SHARE_GROUP(200, "SG012", "성공적으로 그룹을 삭제했습니다."),
    GROUP_CURRENT(200, "SG013", "성공적으로 현재 진행중인 그룹을 반환했습니다.")
    ;
    private final int status;
    private final String code;
    private final String message;
}
