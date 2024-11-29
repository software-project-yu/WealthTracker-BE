package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.JwtResponseDTO;
import com.WealthTracker.demo.DTO.LoginRequestDTO;
import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.service.LoginService;
import com.WealthTracker.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "이메일 로그인", description = "< 로그인 > API")
public class LoginController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    //** 서비스 자체 이메일 로그인
    @Operation(summary = "이메일 로그인 시 필요한 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = loginService.login(loginRequestDTO);
        String token = jwtUtil.createAccessToken(loginService.toCustomUserInfoDTO(user));

        return ResponseEntity.ok(JwtResponseDTO.builder()
                .token(token)
                .build());
    }
}
