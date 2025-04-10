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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssu.muzi.global.result.code.ShareGroupResultCode.GROUP_CURRENT;

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

    // 홈 화면 조회
    @GetMapping("/home")
    @Operation(summary = "홈 화면: 현재 진행 중인 그룹 조회 API", description = "로그인한 사용자가 참여한 그룹 중, 아직 시작 전, 진행 중, 종료 후 7일 이내 그룹을 조회합니다.")
    public ResultResponse<ShareGroupResponse.Home> getHomeGroups(@LoginMember Member member) {
        return ResultResponse.of(ShareGroupResultCode.GET_HOME,
                shareGroupService.getHomeGroups(member));
    }

    // 그룹 이미지 업로드
    @PostMapping("/{shareGroupId}/image")
    @Operation(summary = "그룹 이미지 업로드 API", description = "특정 공유 그룹의 이미지를 업데이트합니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupId> uploadGroupImage(@PathVariable Long shareGroupId,
                                                                            @RequestBody @Valid ShareGroupRequest.GroupImageUploadRequest request) {
        return ResultResponse.of(ShareGroupResultCode.UPLOAD_GROUPIMAGE,
                shareGroupService.updateGroupImage(shareGroupId, request));
    }

    // 내가 참여한 그룹 페이징 조회
    @GetMapping("/my")
    @Operation(summary = "내가 참여한 공유그룹 목록 조회 API", description = "내가 참여한 공유그룹 목록을 페이징 처리하여 조회하는 API입니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 공유그룹 개수를 입력해주세요.")
    })
    public ResultResponse<ShareGroupResponse.PagedShareGroupInfo> getMyShareGroupList(@LoginMember Member member,
                                                                                      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                                                                      @Parameter(hidden = true) Pageable pageable) {
        Page<ShareGroupResponse.ShareGroupPreviewInfo> shareGroupList = shareGroupService.getMyShareGroupList(member, pageable);
        return ResultResponse.of(ShareGroupResultCode.SHARE_GROUP_LIST_INFO,
                shareGroupConverter.toPagedShareGroupInfo(shareGroupList));
    }

    // 그룹 삭제
    @DeleteMapping("/{shareGroupId}/leave")
    @Operation(summary = "그룹 탈퇴 API",
            description = "본인 제외 그룹 내 회원이 남아 있을 때만, 그룹을 탈퇴할 수 있습니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupId> leaveShareGroup(@PathVariable Long shareGroupId,
                                                                            @LoginMember Member member) {
        return ResultResponse.of(ShareGroupResultCode.LEAVE_SHARE_GROUP,
                shareGroupService.leaveShareGroup(shareGroupId, member));
    }

    // 그룹 탈퇴
    @DeleteMapping("/{shareGroupId}")
    @Operation(summary = "그룹 삭제 API",
            description = "그룹 내 회원이 본인밖에 없을 때, 그룹을 삭제할 수 있습니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupId> deleteShareGroup(@PathVariable Long shareGroupId,
                                                                           @LoginMember Member member) {
        return ResultResponse.of(ShareGroupResultCode.DELETE_SHARE_GROUP,
                shareGroupService.deleteShareGroup(shareGroupId, member));
    }

    // 현재 진행중 그룹
    @GetMapping("/current")
    @Operation(summary = "현재 진행 중인 그룹 조회 API",
            description = "현재 시간이 그룹의 시작일과 종료일 사이에 있는 공유 그룹의 ID를 반환합니다.")
    public ResultResponse<ShareGroupResponse.ShareGroupId> getCurrentGroup(@LoginMember Member member) {
        return ResultResponse.of(GROUP_CURRENT,
                shareGroupService.getCurrentGroup(member));
    }




}
