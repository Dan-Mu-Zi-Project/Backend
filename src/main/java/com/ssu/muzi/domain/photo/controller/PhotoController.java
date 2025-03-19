package com.ssu.muzi.domain.photo.controller;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.photo.converter.PhotoConverter;
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

import java.util.List;

import static com.ssu.muzi.global.result.code.PhotoResultCode.CREATE_PRESIGNED_URL;
import static com.ssu.muzi.global.result.code.PhotoResultCode.PHOTO_UPLOAD_BY_SHAREGROUP;

@RestController
@RequestMapping("/photos")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "04. 사진 관련 API", description = "사진 처리와 관련된 API입니다.")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoConverter photoConverter;

    @PostMapping("/preSignedUrl")
    @Operation(summary = "Presigned URL 요청 API", description = "Presigned URL을 요청하는 API입니다.")
    public ResultResponse<PhotoResponse.PreSignedUrlList> getPreSignedUrlList(@Valid @RequestBody PhotoRequest.PreSignedUrlRequest request,
                                                                              @LoginMember Member member) {
        long startTime = System.currentTimeMillis();
        List<PhotoResponse.PreSignedUrl> preSignedUrlList = photoService.getPreSignedUrlList(request, member);
        long finishTime = System.currentTimeMillis();
        log.info("PhotoServiceImpl.getPreSignedUrlList() 수행 시간: {} ms", finishTime - startTime);
        return ResultResponse.of(CREATE_PRESIGNED_URL, photoConverter.toPreSignedUrlList(preSignedUrlList));
    }

    @PostMapping("/{shareGroupId}")
    @Operation(summary = "특정 공유 그룹 사진 업로드 API", description = "특정 공유 그룹에 사진 업로드 및 사진에 등장한 프로필 매핑을 저장합니다.")
    public ResultResponse<PhotoResponse.UploadPhotoList> uploadPhotos(@PathVariable Long shareGroupId,
                                                                      @LoginMember Member member,
                                                                      @RequestBody @Valid PhotoRequest.PhotoUploadList request) {
        return ResultResponse.of(PHOTO_UPLOAD_BY_SHAREGROUP,
                photoService.uploadPhotos(shareGroupId, member, request));
    }

}
