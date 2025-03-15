package com.ssu.muzi.domain.member.service;

import com.ssu.muzi.domain.member.converter.MemberConverter;
import com.ssu.muzi.domain.member.dto.OauthRequest;
import com.ssu.muzi.domain.member.dto.OauthResponse;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.repository.MemberRepository;
import com.ssu.muzi.global.error.BusinessException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.ssu.muzi.global.error.code.JwtErrorCode.INVALID_REFRESH_TOKEN;
import static com.ssu.muzi.global.error.code.JwtErrorCode.MEMBER_NOT_FOUND;


@Service
@Transactional
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

    private final KakaoOauthService kakaoOauthService;
    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    @Override
    public OauthResponse.ServerAccessTokenInfo login(OauthRequest.FrontAccessTokenInfo tokenRequest, HttpServletResponse response) {

        // 프론트에서 받은 액세스 토큰으로 서버 자체 액세스 토큰 생성
        String accessToken = loginWithKakao(tokenRequest.getAccessToken(), response);

        // Kakao 액세스 토큰을 사용해 Kakao 사용자 정보를 가져옴
        OauthResponse.KakaoInfo kakaoInfo = kakaoOauthService.getUserProfileByToken(tokenRequest.getAccessToken());


        // authId로 회원 정보를 조회
        Member member = memberRepository.findByAuthId(kakaoInfo.getAuthId())
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        // 컨버터를 사용해 응답 DTO 생성
        return memberConverter.toServerAccessTokenInfo(accessToken, member);
    }

    @Override
    public OauthResponse.RefreshTokenResponse tokenRefresh(HttpServletRequest request) {
        OauthResponse.RefreshTokenResponse refreshTokenResponse = new OauthResponse.RefreshTokenResponse(); //응답 객체 생성

        // 클라이언트가 보낸 쿠키 목록 가져오기
        Cookie[] list = request.getCookies();

        if(list == null) {
            throw new BusinessException(INVALID_REFRESH_TOKEN);
        }

        // 쿠키 목록에서 "refresh_token"이라는 이름의 쿠키를 필터링하여 가져옴
        Cookie refreshTokenCookie = Arrays.stream(list)
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .toList().get(0);

        if(refreshTokenCookie == null) {
            throw new BusinessException(INVALID_REFRESH_TOKEN);
        }

        // Refresh Token을 기반으로 새로운 Access Token을 생성
        String accessToken = refreshAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponse.setAccessToken(accessToken);
        return refreshTokenResponse;
    }

    @Override
    public OauthResponse.CheckMemberRegistration checkRegistration(OauthRequest.LoginRequest request) {
        boolean isRegistered = memberRepository.existsByAuthId(request.getAuthId());
        if (!isRegistered) {
            throw new BusinessException(MEMBER_NOT_FOUND);
        }
        return memberConverter.toCheckMemberRegistration(isRegistered);
    }

    @Override
    //카카오 로그인 로직
    public String loginWithKakao(String accessToken, HttpServletResponse response) {
        //액세스 토큰으로 사용자 정보 가져오고, 없는 사용자면 추가/있는 사용자면 갱신
        OauthResponse.KakaoInfo kakaoInfo = kakaoOauthService.getUserProfileByToken(accessToken);
        //가져온 사용자 정보를 바탕으로 Access Token과 Refresh Token을 생성하여 반환
        return getTokens(kakaoInfo.getAuthId(), response);
    }

    @Override
    //액세스토큰, 리프레시토큰 생성하고 DB에 저장
    public String getTokens(Long id, HttpServletResponse response) {
        //사용자의 ID를 바탕으로 Access Token을 생성
        final String accessToken = jwtTokenService.createAccessToken(id.toString());
        //Refresh Token을 생성
        final String refreshToken = jwtTokenService.createRefreshToken();

        // 사용자의 정보를 JPA를 통해 조회
        Member member = memberRepository.findByAuthId(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        // 현재 사용자 정보에 새로운 refresh token, access token을 추가
        member.setRefreshToken(refreshToken);
        member.setAccessToken(accessToken);

        // 사용자 정보를 DB에 저장 (업데이트)
        memberRepository.save(member);

        // 생성된 Refresh Token을 클라이언트의 쿠키에 저장
        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
        // 생성된 Access Token을 반환하여 클라이언트에게 전달
        return accessToken;
    }

    @Override
    // 리프레시 토큰으로 액세스토큰 새로 갱신
    public String refreshAccessToken(String refreshToken) {
        // Refresh Token을 기반으로 사용자 조회
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        // refresh token이 유효하지 않으면 예외
        if(!jwtTokenService.validateToken(refreshToken)) {
            throw new BusinessException(INVALID_REFRESH_TOKEN);
        }

        // 유효한 Refresh Token을 기반으로 새로운 Access Token을 생성하여 반환
        return jwtTokenService.createAccessToken(member.getAuthId().toString());
    }
}