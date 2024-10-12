//package com.WealthTracker.demo.service;
//
//import com.WealthTracker.demo.DTO.CustomUserInfoDTO;
//import com.WealthTracker.demo.DTO.KakaoLoginResponseDTO;
//import com.WealthTracker.demo.domain.User;
//import com.WealthTracker.demo.repository.UserRepository;
//import com.WealthTracker.demo.util.JwtUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//
//@Service
//@RequiredArgsConstructor
//public class KakaoServiceImpl implements KakaoService {
//
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//
//    @Value("${kakao.key.cliend-id}")
//    private String clientId;
//
//    @Value("${kakao.redirect-uri}")
//    private String redirectUri;
//
//
//    @Override
//    public KakaoLoginResponseDTO kakaoLogin(String code, String currentDomain) {
//
//        String selectedRedirectUri = selectedRedirectUri(currentDomain);
//
//        String kakaoAccessToken = getKakaoAccessToken(code, selectedRedirectUri);
//
//        HashMap<String, Object> kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);
//
//        User kakaoUser = kakaoUserLogin(kakaoUserInfo);
//
//        String accessToken = jwtUtil.createAccessToken(new CustomUserInfoDTO(kakaoUser));
//        String refreshToken = jwtUtil.createToken(new CustomUserInfoDTO(kakaoUser), 1209600);
//
//        return new KakaoLoginResponseDTO(kakaoUser.getUserId(), kakaoUser.getNickName(), kakaoUser.getEmail(), accessToken, refreshToken);
//    }
//
//    // 인가 코드를 통해 카카오에서 액세스 토큰 받기
//    private String getKakaoAccessToken(String code, String redirectUri) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", clientId);
//        body.add("redirect_uri", redirectUri);
//        body.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode;
//        try {
//            jsonNode = objectMapper.readTree(response.getBody());
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to get access token from Kakao");
//        }
//        return jsonNode.get("access_token").asText();
//    }
//
//    // 카카오 사용자 정보 가져오기
//    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode;
//        try {
//            jsonNode = objectMapper.readTree(response.getBody());
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("카카오로부터 유저 정보를 불러오지 못했습니다.");
//        }
//
//        Long id = jsonNode.get("id").asLong();
//        String email = jsonNode.get("kakao_account").get("email").asText();
//        String nickname = jsonNode.get("properties").get("nickname").asText();
//
//        HashMap<String, Object> userInfo = new HashMap<>();
//        userInfo.put("id", id);
//        userInfo.put("email", email);
//        userInfo.put("nickname", nickname);
//
//        return userInfo;
//    }
//
//    // 카카오 사용자 정보로 로그인/회원가입 처리
//    private User kakaoUserLogin(HashMap<String, Object> userInfo) {
//        Long id = (Long) userInfo.get("id");
//        String email = (String) userInfo.get("email");
//        String nickname = (String) userInfo.get("nickname");
//
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) {
//            user = User.builder()
//                    .userId(id)
//                    .email(email)
//                    .nickName(nickname)
//                    .build();
//            userRepository.save(user);
//        }
//
//        return user;
//    }
//
//    // 도메인에 따른 Redirect URI 설정
//    private String selectRedirectUri(String currentDomain) {
//        return currentDomain.equals("localhost") ? "http://localhost:8080" : redirectUri;
//    }
//
//}
