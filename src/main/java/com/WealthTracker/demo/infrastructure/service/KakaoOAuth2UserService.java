package com.WealthTracker.demo.infrastructure.service;

import com.WealthTracker.demo.domain.auth.dto.CustomUserInfoDTO;
import com.WealthTracker.demo.global.util.JwtUtil;
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
        // 사용자 정보 추출
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String nickname = properties.get("nickname").toString(); //** 카카오 로그인 한 유저의 이름을 저장
        String email = kakaoAccount.get("email").toString(); //** 카카오 로그인 한 유저의 이메일을 저장

        CustomUserInfoDTO userInfo = CustomUserInfoDTO.builder() //** 사용자 정보를 DTO에 저장 후 Builder로 id, email, name 저장
                .userId(Long.parseLong(attributes.get("id").toString()))
                .email(email)
                .name(nickname) // nickname이 사실상 name임
                .build();


        String token = jwtUtil.createAccessToken(userInfo); //** 해당 카카오 로그인 유저의 정보로 서비스 자체의 토큰 생성

        // 생성된 JWT 토큰 출력
        log.info("JWT Token: {}", token);

        // 클라이언트에 JWT 토큰 전달
        httpSession.setAttribute("login_info", attributes);
        httpSession.setAttribute("jwt_token", token);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(), "id");
    }
}

//** 참고 자료 https://kakao-tam.tistory.com/54