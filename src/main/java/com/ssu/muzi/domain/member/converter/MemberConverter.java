package com.ssu.muzi.domain.member.converter;

import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.dto.OauthResponse;
import com.ssu.muzi.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    // 인가 관련, DTO를 엔티티로 변환하는 메소드
    public Member toKakaoUserEntity(OauthResponse.KakaoInfo kakaoInfo) {
        return Member.builder()
                .authId(kakaoInfo.getAuthId()) // UserDTO의 authId를 User 엔티티의 authId로 설정
                .name(kakaoInfo.getName()) //카카오톡 이름
                .email(kakaoInfo.getEmail())
                .refreshToken(kakaoInfo.getRefreshToken()) // UserDTO의 리프레시 토큰을 User 엔티티의 리프레시 토큰으로 설정
                .build();
    }

    // 로그인 시, 정보를 응답으로 변환하는 메소드
    public OauthResponse.ServerAccessTokenInfo toServerAccessTokenInfo(String accessToken, Member member) {
        // 응답객체 생성
        OauthResponse.ServerAccessTokenInfo response = new OauthResponse.ServerAccessTokenInfo();
        response.setAccessToken(accessToken);
        response.setMemberId(member.getId());
        response.setIsFaceCaptured(member.getIsFaceCaptured());
        return response;
    }

    // 인가 관련 응답을 DTO로 반환
    public OauthResponse.KakaoInfo toLoginUserInfo(Member member) {
        return OauthResponse.KakaoInfo.builder()
                .authId(member.getAuthId())
                .name(member.getName())
                .refreshToken(member.getRefreshToken())
                .build();
    }

    // 회원가입 여부 체크
    public OauthResponse.CheckMemberRegistration toCheckMemberRegistration(boolean isRegistered) {
        return new OauthResponse.CheckMemberRegistration(isRegistered);
    }

    // 멤버 정보를 반환
    public MemberResponse.MemberInfo toMemberInfo(Member member) {
        return MemberResponse.MemberInfo.builder()
                .authId(member.getAuthId())
                .memberId(member.getId())
                .name(member.getName())
                .memberImageUrl(member.getMemberImageUrl())
                .onlyWifi(member.getOnlyWifi())
                .build();
    }

    // member Id만 반환
    public MemberResponse.MemberId toMemberId(Member member) {
        return MemberResponse.MemberId
                .builder()
                .memberId(member.getId())
                .build();
    }

}
