package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.CustomUserInfoDTO;
import com.WealthTracker.demo.DTO.JwtResponseDTO;
import com.WealthTracker.demo.DTO.LoginRequestDTO;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.service.LoginService;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = loginService.login(loginRequestDTO);
        String token = jwtUtil.createAccessToken(loginService.toCustomUserInfoDTO(user));

        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .token(token)
                .build();

        return ResponseEntity.ok(jwtResponseDTO);
    }

    //** 토큰 테스트 코드
//    @GetMapping("/api/return")
//    public ResponseEntity<Long> getUserIdFromToken(@RequestHeader("Authorization") String token) {
//        String accessToken = jwtUtil.getAccessToken(token);
//        Long userId = jwtUtil.getUserId(accessToken);
//
//        return ResponseEntity.ok(userId);
//    }
//
//    @GetMapping("/api/userinfo")
//    public ResponseEntity<CustomUserInfoDTO> getUserInfoFromToken(@RequestHeader("Authorization") String token) {
//        String accessToken = jwtUtil.getAccessToken(token);
//        Long userId = jwtUtil.getUserId(accessToken);
//
//        // userId를 사용해 유저 정보 조회
//        User user = loginService.getUserById(userId);
//        CustomUserInfoDTO userInfoDTO = loginService.toCustomUserInfoDTO(user);
//
//        return ResponseEntity.ok(userInfoDTO);
//    }
}
