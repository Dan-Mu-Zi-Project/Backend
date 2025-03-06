package com.ssu.muzi.domain.member.service;

import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.Member;

public interface MemberService {
    MemberResponse.MemberInfo getMyInfo();
    MemberResponse.MemberId setNickName(Member member, String name);
}
