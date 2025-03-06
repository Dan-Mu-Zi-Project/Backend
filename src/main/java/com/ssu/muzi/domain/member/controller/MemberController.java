package com.ssu.muzi.domain.member.controller;

import com.ssu.muzi.domain.member.converter.MemberConverter;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.service.MemberService;
import com.ssu.muzi.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssu.muzi.global.result.code.MemberResultCode.MYPAGE_INFO;

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
}

