package com.WealthTracker.demo.controller;

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

    @GetMapping("/api/return")
    public ResponseEntity<Long> getUserIdFromToken(@RequestHeader("Authorization") String token) {
        String accessToken = jwtUtil.getAccessToken(token);
        Long userId = jwtUtil.getUserId(accessToken);

        return ResponseEntity.ok(userId);
    }

}
