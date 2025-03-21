package com.ssu.muzi.global.result.code;

import com.ssu.muzi.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoResultCode implements ResultCode {
    CREATE_PRESIGNED_URL(200, "SP000", "성공적으로 presigned url을 생성했습니다."),
    PHOTO_UPLOAD_BY_SHAREGROUP(200, "SP001", "성공적으로 그룹에 사진을 업로드했습니다."),
    PHOTO_DOWNLOAD(200, "SP002", "성공적으로 사진 다운로드 로그를 기록했습니다."),
    PHOTO_LIKE(200, "SP003", "성공적으로 사진에 좋아요를 눌렀습니다."),
    CANCEL_LIKE(200, "SP004", "성공적으로 사진의 좋아요를 취소했습니다."),
    GET_PHOTO_DETAIL(200, "SP005", "성공적으로 사진의 상세 정보를 조회했습니다."),
    PHOTO_LIST_INFO(200, "SP006", "성공적으로 사진 리스트를 조회했습니다."),
    DELETE_PHOTO(200, "SP007", "성공적으로 사진을 삭제했습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}

