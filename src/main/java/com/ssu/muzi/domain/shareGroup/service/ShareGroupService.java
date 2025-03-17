package com.ssu.muzi.domain.shareGroup.service;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupRequest;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupResponse;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;

public interface ShareGroupService {
    ShareGroup createShareGroup(ShareGroupRequest.CreateShareGroupRequest request, Member member);
    ShareGroup updateShareGroup(ShareGroupRequest.UpdateShareGroupRequest request, Long shareGroupId, Member member);
    Profile joinShareGroup(Long shareGroupId, Member member);
    ShareGroupResponse.ShareGroupVector getShareGroupVectorList(Long shareGroupId);
    ShareGroup findShareGroup(Long shareGroupId);
}
