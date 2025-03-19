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
    ;
    private final int status;
    private final String code;
    private final String message;
}

