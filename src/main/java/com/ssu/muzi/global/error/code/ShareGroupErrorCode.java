package com.ssu.muzi.global.error.code;

import com.ssu.muzi.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShareGroupErrorCode implements ErrorCode {
    ALREADY_EXISTS_PROGRESSING_GROUP(404, "ES000", "해당 기간에 이미 정해진 여행 일정이 존재합니다."),
    STARTEDAT_AFTER_ENDEDAT(404, "ES001", "시작 날짜는 끝나는 날짜보다 이전이어야 합니다."),
    SHARE_GROUP_NOT_FOUND(404, "ES002", "해당 id에 해당하는 공유그룹을 찾을 수 없습니다."),
    ALREADY_STARTED_TRAVEL(404, "ES003", "이미 여행이 시작됐기 때문에 여행 날짜를 변경할 수 없어요."),
    INVALID_DATE_MODIFICATION(404, "ES004", "시작 날짜나 종료 날짜는 현재 날짜 이전으로 설정될 수 없어요."),
    ALREADY_JOINED_GROUP(404, "ES005", "이미 참여한 그룹입니다."),
    ALREADY_STARTED_TRAVEL_NOT_JOIN(404, "ES006", "이미 여행이 시작되어 참여할 수 없어요."),
    LEAVE_NOT_ALLOWED(404, "ES007", "그룹에 본인 제외 최소 1명이 남아 있어야 탈퇴할 수 있습니다. 그룹 삭제를 이용해 주세요."),
    DELETE_NOT_ALLOWED(404, "ES008", "그룹에 다른 사람이 남아 있으면 그룹을 삭제할 수 없습니다. 그룹 탈퇴를 이용해 주세요."),
    NOT_EXIST_CURRENT_GROUP(404, "ES009", "현재 진행중인 그룹이 없습니다.")
    ;

    private final int status;
    private final String code;
    private final String message;
}
