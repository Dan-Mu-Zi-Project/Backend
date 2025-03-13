package com.ssu.muzi.domain.shareGroup.converter;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.shareGroup.dto.ProfileResponse;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupRequest;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupResponse;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShareGroupConverter {

    // 그룹 생성 요청 받은 그룹을 엔티티로 변환
    public ShareGroup toShareGroupEntity(ShareGroupRequest.CreateShareGroupRequest request) {
        return ShareGroup.builder()
                .groupName(request.getGroupName())             // 요청에서 받은 그룹명
                .description(request.getDescription()) // 요청에서 받은 그룹 소개
                .place(request.getPlace())
                .startedAt(request.getStartedAt())
                .endedAt(request.getEndedAt())
                .build();
    }

    // 초대장 정보 화면을 반환하는 DTO (참여자 목록, 오너의 정보 추가)
    public ShareGroupResponse.InvitationInfo toShareGroupInvitationInfo(ShareGroup shareGroup, Member member) {
        // shareGroup의 참가자 목록을 DTO 리스트로 변환
        List<ProfileResponse.ParticipantInfo> participantInfoList = shareGroup.getProfileList()
                .stream()
                .map(profile -> ProfileResponse.ParticipantInfo
                        .builder()
                        .profileId(profile.getId())
                        .name(profile.getMember().getName())
                        .memberImageUrl(profile.getMember().getMemberImageUrl())
                        .build())
                .collect(Collectors.toList());

        return ShareGroupResponse.InvitationInfo.builder()
                .shareGroupId(shareGroup.getId())
                .groupName(shareGroup.getGroupName())
                .description(shareGroup.getDescription())
                .place(shareGroup.getPlace())
                .startedAt(shareGroup.getStartedAt())
                .endedAt(shareGroup.getEndedAt())
                // 내 정보 (그룹을 만든 member)
                .ownerName(member.getName())
                .ownerImageUrl(member.getMemberImageUrl())
                // 참여자 목록
                .participantInfoList(participantInfoList)
                .createdAt(shareGroup.getCreatedAt())
                .build();
    }
}
