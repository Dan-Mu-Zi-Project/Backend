package com.ssu.muzi.domain.photo.controller;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.photo.dto.PhotoRequest;
import com.ssu.muzi.domain.photo.dto.PhotoResponse;
import com.ssu.muzi.domain.photo.service.PhotoService;
import com.ssu.muzi.global.result.ResultResponse;
import com.ssu.muzi.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssu.muzi.global.result.code.PhotoResultCode.PHOTO_DOWNLOAD;

@RestController
@RequestMapping("/gallery")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "03. 앨범 관련 API", description = "엘범 조회, 좋아요, 다운로드, 삭제에 관한 API 입니다.")
public class GalleryController {

    private final PhotoService photoService;

    @PostMapping("/{shareGroupId}/download")
    @Operation(summary = "사진 다운로드 로그 기록 API", description = "특정 공유 그룹에 사진 업로드 및 사진에 등장한 프로필 매핑을 저장합니다.")
    public ResultResponse<PhotoResponse.PhotoDownload> recordDownload(@PathVariable Long shareGroupId,
                                                                      @LoginMember Member member,
                                                                      @RequestBody @Valid PhotoRequest.PhotoDownload request) {
        return ResultResponse.of(PHOTO_DOWNLOAD,
                photoService.recordDownload(shareGroupId, member, request));
    }

}
