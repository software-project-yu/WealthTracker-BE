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
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    //** 서비스 자체 이메일 로그인
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = loginService.login(loginRequestDTO);
        String token = jwtUtil.createAccessToken(loginService.toCustomUserInfoDTO(user));

        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .token(token)
                .build();

        return ResponseEntity.ok(jwtResponseDTO);
    }
}
