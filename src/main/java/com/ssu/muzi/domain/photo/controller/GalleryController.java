package com.ssu.muzi.domain.photo.controller;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.photo.converter.PhotoConverter;
import com.ssu.muzi.domain.photo.dto.PhotoRequest;
import com.ssu.muzi.domain.photo.dto.PhotoResponse;
import com.ssu.muzi.domain.photo.service.PhotoService;
import com.ssu.muzi.global.result.ResultResponse;
import com.ssu.muzi.global.result.code.PhotoResultCode;
import com.ssu.muzi.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssu.muzi.global.result.code.PhotoResultCode.CANCEL_LIKE;
import static com.ssu.muzi.global.result.code.PhotoResultCode.PHOTO_DOWNLOAD;
import static com.ssu.muzi.global.result.code.PhotoResultCode.PHOTO_LIKE;

@RestController
@RequestMapping("/gallery")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "03. 앨범 관련 API", description = "엘범 조회, 좋아요, 다운로드, 삭제에 관한 API 입니다.")
public class GalleryController {

    private final PhotoService photoService;
    private final PhotoConverter photoConverter;

    @PostMapping("/{shareGroupId}/download")
    @Operation(summary = "사진 다운로드 로그 기록 API", description = "특정 공유 그룹에 사진 업로드 및 사진에 등장한 프로필 매핑을 저장합니다.")
    public ResultResponse<PhotoResponse.PhotoDownload> recordDownload(@PathVariable Long shareGroupId,
                                                                      @LoginMember Member member,
                                                                      @RequestBody @Valid PhotoRequest.PhotoDownload request) {
        return ResultResponse.of(PHOTO_DOWNLOAD,
                photoService.recordDownload(shareGroupId, member, request));
    }

    @PostMapping("/{shareGroupId}/{photoId}/like")
    @Operation(summary = "사진 좋아요 누르기 API",
            description = "특정 사진에 좋아요 기록을 남깁니다. 같은 사용자가 같은 사진에 여러 번 좋아요를 누를 수 없습니다.")
    public ResultResponse<PhotoResponse.PhotoId> likePhoto(@PathVariable Long shareGroupId,
                                                           @PathVariable Long photoId,
                                                           @LoginMember Member member) {
        return ResultResponse.of(PHOTO_LIKE,
                photoService.likePhoto(shareGroupId, photoId, member));
    }

    @DeleteMapping("/{shareGroupId}/{photoId}/cancelLike")
    @Operation(summary = "사진 좋아요 취소 API",
            description = "로그인한 사용자가 특정 사진에 대해 좋아요를 취소합니다. (hard delete)")
    public ResultResponse<PhotoResponse.PhotoId> cancelLike(@PathVariable Long shareGroupId,
                                                            @PathVariable Long photoId,
                                                            @LoginMember Member member) {
        return ResultResponse.of(CANCEL_LIKE,
                photoService.cancelLike(shareGroupId, photoId, member));
    }

    // 특정 profile의 앨범 안의 사진 리스트 조회
    @GetMapping("/{shareGroupId}/{profileId}")
    @Operation(summary = "특정 앨범 사진 목록 조회 API",
            description = "특정 profileId(앨범 소유자)의 사진 목록을 최신순 페이징 처리하여 조회합니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 공유그룹 개수를 입력해주세요.")
    })
    public ResultResponse<PhotoResponse.PagedPhotoInfo> getPhotoList(@LoginMember Member member,
                                                                     @PathVariable Long shareGroupId,
                                                                     @PathVariable Long profileId,
                                                                     @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                                                         @Parameter(hidden = true) Pageable pageable) {
        Page<PhotoResponse.PhotoPreviewInfo> photoList = photoService.getPhotoList(member, shareGroupId, profileId, pageable);
        return ResultResponse.of(PhotoResultCode.PHOTO_LIST_INFO,
                photoConverter.toPagedPhotoInfo(photoList, shareGroupId, profileId));
    }


}
