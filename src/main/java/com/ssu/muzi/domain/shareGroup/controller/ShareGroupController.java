package com.ssu.muzi.domain.shareGroup.controller;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.shareGroup.converter.ShareGroupConverter;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupRequest;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupResponse;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import com.ssu.muzi.domain.shareGroup.service.ShareGroupService;
import com.ssu.muzi.global.result.ResultResponse;
import com.ssu.muzi.global.result.code.ShareGroupResultCode;
import com.ssu.muzi.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shareGroups")
@Tag(name = "02. 공유그룹 관련 API", description = "공유그룹 생성, 참여, 조회, 삭제 등을 처리하는 API입니다.")
public class ShareGroupController {
    private final ShareGroupConverter shareGroupConverter;
    private final ShareGroupService shareGroupService;

    @PostMapping
    @Operation(summary = "공유그룹 생성 API", description = "새로운 공유그룹을 생성하는 API입니다.")
    public ResultResponse<ShareGroupResponse.InvitationInfo> createShareGroup(@RequestBody @Valid ShareGroupRequest.CreateShareGroupRequest request,
                                                                              @LoginMember Member member) {
        ShareGroup shareGroup = shareGroupService.createShareGroup(request, member);
        return ResultResponse.of(ShareGroupResultCode.CREATE_SHARE_GROUP,
                shareGroupConverter.toShareGroupInvitationInfo(shareGroup));
    }

    @PatchMapping("{shareGroupId}/info")
    @Operation(summary = "공유그룹 수정 API", description = "공유그룹을 수정하는 API입니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupId> updateShareGroup(@RequestBody @Valid ShareGroupRequest.UpdateShareGroupRequest request,
                                                                            @PathVariable Long shareGroupId,
                                                                            @LoginMember Member member) {
        ShareGroup shareGroup = shareGroupService.updateShareGroup(request, shareGroupId, member);
        return ResultResponse.of(ShareGroupResultCode.UPDATE_SHARE_GROUP,
                shareGroupConverter.toShareGroupId(shareGroup));
    }

    @PostMapping("{shareGroupId}/join")
    @Operation(summary = "공유그룹 참여 API", description = "특정 공유그룹에 참여하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "shareGroupId", description = "특정 공유그룹 id를 입력해 주세요.")
    })
    public ResultResponse<ShareGroupResponse.JoinInfo> joinShareGroup(@PathVariable(name = "shareGroupId") Long shareGroupId,
                                                                      @LoginMember Member member) {
        // 그룹 id와 member정보를 바탕으로 프로필 생성
        Profile profile = shareGroupService.joinShareGroup(shareGroupId, member);
        return ResultResponse.of(ShareGroupResultCode.JOIN_SHARE_GROUP,
                shareGroupConverter.toShareGroupJoinInfo(profile));
    }

    // 특정 그룹의 얼굴 샘플 이미지 벡터값 조회 API
    @GetMapping("/{shareGroupId}/vector")
    @Operation(summary = "공유 그룹에 속한 모든 멤버의 벡터 조회 API", description = "해당 공유 그룹에 속한 회원들의 벡터(샘플 이미지) 정보를 조회합니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupVector> getShareGroupVectorList(@PathVariable Long shareGroupId) {
        return ResultResponse.of(ShareGroupResultCode.GET_VECTORLIST,
                shareGroupService.getShareGroupVectorList(shareGroupId));
    }

    // 특정 공유 그룹의 초대장 조회 API
    @GetMapping("/{shareGroupId}/invite")
    @Operation(summary = "공유 그룹의 초대장 조회", description = "해당 공유 그룹의 초대장을 조회합니다.")
    public ResultResponse<ShareGroupResponse.InvitationInfo> getInvitation(@PathVariable Long shareGroupId) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);
        return ResultResponse.of(ShareGroupResultCode.GET_INVITATION,
                shareGroupConverter.toShareGroupInvitationInfo(shareGroup));
    }

    // 특정 공유 그룹의 상세정보 조회 API
    @GetMapping("/{shareGroupId}/info")
    @Operation(summary = "특정 공유 그룹의 정보 조회", description = "해당 공유 그룹을 클릭했을 때, 상세정보를 조회합니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupDetailInfo> getShareGroupInfo(@PathVariable Long shareGroupId) {
        ShareGroup shareGroup = shareGroupService.findShareGroup(shareGroupId);
        return ResultResponse.of(ShareGroupResultCode.GET_SHAREGROUP_INFO,
                shareGroupConverter.toShareGroupDetailInfo(shareGroup));
    }

}

// DB 수정
