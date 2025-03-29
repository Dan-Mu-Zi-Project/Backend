package com.ssu.muzi.domain.shareGroup.converter;

import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.entity.MemberSampleImage;
import com.ssu.muzi.domain.shareGroup.dto.ProfileResponse;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupRequest;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupResponse;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import com.ssu.muzi.domain.shareGroup.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShareGroupConverter {

    // 그룹 생성 요청 받은 그룹을 엔티티로 변환
    public ShareGroup toShareGroupEntity(ShareGroupRequest.CreateShareGroupRequest request, Member member) {
        return ShareGroup.builder()
                .groupName(request.getGroupName())             // 요청에서 받은 그룹명
                .description(request.getDescription()) // 요청에서 받은 그룹 소개
                .place(request.getPlace())
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .groupColor("#FFF4CD")
                .groupImageUrl("https://muzi-photo.s3.ap-northeast-2.amazonaws.com/groupImage/basicGroupImage.png")
                .ownerName(member.getName())
                .ownerImageUrl(member.getMemberImageUrl())
                .build();
    }

    // 업데이트 요청 받은 그룹을 엔티티로 변환
    public ShareGroup updateShareGroupEntity(ShareGroup shareGroup, ShareGroupRequest.UpdateShareGroupRequest request) {
        shareGroup.setGroupColor(request.getGroupColor());
        shareGroup.setGroupName(request.getGroupName());
        shareGroup.setDescription(request.getDescription());
        shareGroup.setPlace(request.getPlace());
        shareGroup.setStartedAt(request.getStartedAt());
        shareGroup.setEndedAt(request.getEndedAt());
        return shareGroup;
    }

    // 그룹 생성 시, 초대장 정보 화면을 반환하는 DTO (참여자 목록, 오너의 정보 추가)
    public ShareGroupResponse.InvitationInfo toShareGroupInvitationInfo(ShareGroup shareGroup) {
        // shareGroup의 참가자 목록을 DTO 리스트로 변환
        List<ProfileResponse.ParticipantInfo> participantInfoList = toParticipantInfoList(shareGroup);

        return ShareGroupResponse.InvitationInfo
                .builder()
                .shareGroupId(shareGroup.getId())
                .groupName(shareGroup.getGroupName())
                .description(shareGroup.getDescription())
                .place(shareGroup.getPlace())
                .startedAt(shareGroup.getStartedAt())
                .endedAt(shareGroup.getEndedAt())
                .ownerName(shareGroup.getOwnerName())
                .ownerImageUrl(shareGroup.getOwnerImageUrl())
                // 참여자 목록
                .participantInfoList(participantInfoList)
                .createdAt(shareGroup.getCreatedAt())
                .build();
    }

    // shareGroup을 받아서, 그 그룹에 속한 참가자 목록을 DTO 리스트로 변환
    private List<ProfileResponse.ParticipantInfo> toParticipantInfoList(ShareGroup shareGroup) {
        return shareGroup.getProfileList()
                .stream()
                .map(profile -> ProfileResponse.ParticipantInfo
                        .builder()
                        .profileId(profile.getId())
                        .name(profile.getMember().getName())
                        .memberImageUrl(profile.getMember().getMemberImageUrl())
                        .build())
                .collect(Collectors.toList());
    }


    // 그룹 가입 시
    public ShareGroupResponse.JoinInfo toShareGroupJoinInfo(Profile profile) {

        return ShareGroupResponse.JoinInfo.builder()
                .shareGroupId(profile.getShareGroup().getId())
                .profileId(profile.getId())
                .joinedAt(profile.getJoinedAt())
                .build();
    }

    // 그룹 상세 조회 시
    public ShareGroupResponse.ShareGroupDetailInfo toShareGroupDetailInfo(ShareGroup shareGroup) {

        // shareGroup의 참가자 목록을 DTO 리스트로 변환
        List<ProfileResponse.ParticipantInfo> participantInfoList = toParticipantInfoList(shareGroup);

        return ShareGroupResponse.ShareGroupDetailInfo
                .builder()
                .shareGroupId(shareGroup.getId())
                .groupName(shareGroup.getGroupName())
                .description(shareGroup.getDescription())
                .groupImage(shareGroup.getGroupImageUrl())
                .place(shareGroup.getPlace())
                .startedAt(shareGroup.getStartedAt())
                .endedAt(shareGroup.getEndedAt())
                // 참여자 목록
                .participantInfoList(participantInfoList)
                .build();
    }

    // 샘플 이미지 리스트를 받아 MemberEmbedding DTO를 생성
    public MemberResponse.MemberEmbedding toMemberEmbedding(Profile profile, List<MemberSampleImage> sampleImageList) {
        List<MemberResponse.EmbeddingVector> embeddingVectorList = sampleImageList
                .stream()
                .map(image -> MemberResponse.EmbeddingVector
                        .builder()
                        .angleType(image.getAngleType().name()) // enum → 문자열로 변환
                        .vector(image.getFaceVector())
                        .build())
                .toList();

        // 프로필에 저장된 닉네임이 있다면 사용, 없으면 연결된 member의 이름 사용
        String name = profile.getMember().getName();

        return MemberResponse.MemberEmbedding
                .builder()
                .memberId(profile.getMember().getId())
                .profileId(profile.getId())
                .name(name)
                .embeddingVectorList(embeddingVectorList)
                .build();
    }

    // ShareGroup과 EmbeddingVector List를 기반으로 최종 응답 DTO를 생성
    public ShareGroupResponse.ShareGroupVector toShareGroupVector(ShareGroup shareGroup,
                                                                  List<MemberResponse.MemberEmbedding> memberEmbeddingList) {
        return ShareGroupResponse.ShareGroupVector
                .builder()
                .shareGroupId(shareGroup.getId())
                .memberEmbeddingList(memberEmbeddingList)
                .build();
    }

    // 홈 화면에 있는 "하나"의 그룹을 응답 DTO로 변환
    public ShareGroupResponse.HomeDetail toHomeDetail(ShareGroup group, Status status) {
        return ShareGroupResponse.HomeDetail
                .builder()
                .shareGroupId(group.getId())
                .status(status.name()) // enum을 문자열로 변환 ("BEFORE_START", "IN_PROGRESS", "RECENTLY_ENDED")
                .groupName(group.getGroupName())
                .place(group.getPlace())
                .startedAt(group.getStartedAt())
                .endedAt(group.getEndedAt())
                .build();
    }

    // 내가 속한 전체 그룹 조회 (프리뷰) 시, "그룹 하나"에 대한 응답
    public ShareGroupResponse.ShareGroupPreviewInfo toShareGroupPreview (ShareGroup group, Status status, long downloadCount, long entireCount) {
        return ShareGroupResponse.ShareGroupPreviewInfo
                .builder()
                .shareGroupId(group.getId())
                .groupColor(group.getGroupColor())
                .status(status.name())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .startedAt(group.getStartedAt())
                .endedAt(group.getEndedAt())
                .downloadCount((int) downloadCount)
                .entireCount((int) entireCount)
                .createdAt(group.getCreatedAt())
                .build();
    }

    // 내가 속한 전체 그룹 조회를, 응답용 페이징 처리로 변환
    public ShareGroupResponse.PagedShareGroupInfo toPagedShareGroupInfo(Page<ShareGroupResponse.ShareGroupPreviewInfo> page) {
        return ShareGroupResponse.PagedShareGroupInfo
                .builder()
                .shareGroupInfoList(page.getContent())
                .totalElements(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }

    // 그룹 Id만 반환
    public ShareGroupResponse.ShareGroupId toShareGroupId(ShareGroup shareGroup) {
        return ShareGroupResponse.ShareGroupId.builder()
                .shareGroupId(shareGroup.getId())
                .build();
    }
}
