package com.ssu.muzi.domain.member.service;

import com.ssu.muzi.domain.member.dto.MemberRequest;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.Member;

public interface MemberService {
    MemberResponse.MemberInfo getMyInfo();
    MemberResponse.MemberId setNickName(Member member, String name);
    MemberResponse.MemberId setMemberImageUrl(Member member, String memberImageUrl);
    MemberResponse.MemberId setWifi(Member member, Boolean onlyWifi);
    MemberResponse.MemberId saveSampleImages(Member member, MemberRequest.SampleImageList request);
    MemberResponse.MemberId deleteMember(Member member);
}
