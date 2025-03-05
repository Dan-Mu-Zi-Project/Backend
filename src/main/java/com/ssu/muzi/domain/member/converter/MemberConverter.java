package com.ssu.muzi.domain.member.converter;

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
                .refreshToken(kakaoInfo.getRefreshToken()) // UserDTO의 리프레시 토큰을 User 엔티티의 리프레시 토큰으로 설정
                .build();
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


}
