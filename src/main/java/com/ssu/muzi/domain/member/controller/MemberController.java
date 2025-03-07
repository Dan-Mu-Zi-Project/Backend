package com.ssu.muzi.domain.member.controller;

import com.ssu.muzi.domain.member.converter.MemberConverter;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.service.MemberService;
import com.ssu.muzi.global.result.ResultResponse;
import com.ssu.muzi.global.security.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ssu.muzi.global.result.code.MemberResultCode.MYPAGE_INFO;
import static com.ssu.muzi.global.result.code.MemberResultCode.SET_IMAGE;
import static com.ssu.muzi.global.result.code.MemberResultCode.SET_NICKNAME;

@RestController
@RequestMapping("/members")
@Tag(name = "01. 회원 API", description = "회원 도메인의 API입니다.")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberConverter memberConverter;


    // 현재 로그인된 유저정보 조회 API
    @GetMapping("/my")
    @Operation(summary = "내 정보 조회 API", description = "현재 멤버의 jwt를 통해 내 정보를 조회하는 API입니다.")
    public ResultResponse<MemberResponse.MemberInfo> getMyInfo() {
        return ResultResponse.of(MYPAGE_INFO, memberService.getMyInfo());
    }

    // 내 정보 수정 (이름 수정) API
    @PutMapping("/myName")
    @Operation(summary = "내 정보(이름) 수정 API", description = "유저 이름을 변경하는 API입니다.")
    public ResultResponse<MemberResponse.MemberId> setName(@LoginMember Member member,
                                                               @RequestParam String name) {
        return ResultResponse.of(SET_NICKNAME, memberService.setNickName(member, name));
    }

    // 내 정보 수정 (이름 수정) API
    @PutMapping("/my/image")
    @Operation(summary = "내 정보(프로필 사진) 수정 API", description = "유저 프로필 사진을 변경하는 API입니다.")
    public ResultResponse<MemberResponse.MemberId> setImage(@LoginMember Member member,
                                                           @RequestParam String memberImageUrl) {
        return ResultResponse.of(SET_IMAGE, memberService.setMemberImageUrl(member, memberImageUrl));
    }

}

