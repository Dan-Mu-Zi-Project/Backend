package com.ssu.muzi.domain.member.service;

import com.ssu.muzi.domain.member.converter.MemberConverter;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.repository.MemberRepository;
import com.ssu.muzi.global.error.BusinessException;
import com.ssu.muzi.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.ssu.muzi.global.error.code.MemberErrorCode.MEMBER_NOT_FOUND_BY_MEMBER_ID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    //회원 정보 조회
    @Override
    public MemberResponse.MemberInfo getMyInfo() {
        // SecurityContext에서 현재 로그인된 사용자의 멤버 ID를 추출
        final long memberId = SecurityUtil.getCurrentUserId();

        //memberId를 사용하여 데이터베이스에서 해당 사용자의 정보를 조회
        Member member = findMemberByMemberId(memberId);
        //컨버터로 변환
        return memberConverter.toMemberInfo(member);
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_MEMBER_ID));
    }

}
