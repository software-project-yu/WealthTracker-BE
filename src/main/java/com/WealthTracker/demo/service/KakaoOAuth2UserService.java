package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.CustomUserInfoDTO;
import com.WealthTracker.demo.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOAuth2UserService extends DefaultOAuth2UserService {

    private final HttpSession httpSession;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오에서 받은 사용자 정보
        CustomUserInfoDTO userInfo = CustomUserInfoDTO.builder()
                .userId(Long.parseLong(attributes.get("id").toString()))
                .build();

        // JWT 토큰 생성
        String token = jwtUtil.createAccessToken(userInfo);

        // 생성된 JWT 토큰 출력
        log.info("JWT Token: {}", token);

        // 클라이언트에 JWT 토큰 전달 (필요에 따라 response에 추가하거나 세션에 저장)
        httpSession.setAttribute("login_info", attributes);
        httpSession.setAttribute("jwt_token", token);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(), "id");
    }
}

//** 참고 자료 https://kakao-tam.tistory.com/54