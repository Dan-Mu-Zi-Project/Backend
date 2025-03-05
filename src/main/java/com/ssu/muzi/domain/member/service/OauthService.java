package com.ssu.muzi.domain.member.service;

import com.ssu.muzi.domain.member.dto.OauthRequest;
import com.ssu.muzi.domain.member.dto.OauthResponse;
import com.ssu.muzi.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {
    OauthResponse.ServerAccessTokenInfo login(OauthRequest.FrontAccessTokenInfo oauthRequest, HttpServletResponse response);
    String refreshAccessToken(String refreshToken);
    String getTokens(Long id, HttpServletResponse response);
    String loginWithKakao(String accessToken, HttpServletResponse response);
    OauthResponse.RefreshTokenResponse tokenRefresh(HttpServletRequest request);
    OauthResponse.KakaoInfo getMyInfo();
    Member findMemberByAuthId(Long authId);
    OauthResponse.CheckMemberRegistration checkRegistration(OauthRequest.LoginRequest request);
}
