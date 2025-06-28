package com.WealthTracker.demo.domain.user.controller;

import com.WealthTracker.demo.infrastructure.dto.ReturnCodeDTO;
import com.WealthTracker.demo.domain.user.dto.UserProfileResponseDTO;
import com.WealthTracker.demo.domain.user.service.UserService;
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
@Tag(name = "사용자 정보", description = "< 프로필 > API")
public class UserController {

    private final UserService userService;

    //* 프로필 정보 조회
    @Operation(summary = "유저의 프로필 정보를 반환하는 API 입니다. [담당자] : 박재성",
            description = "프로필 정보는 이름과 닉네임입니다. 'nickName' << 대-소문자 구별에 주의해 주세요!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getProfile(
            @RequestHeader("Authorization") String token) {
        UserProfileResponseDTO profile = userService.getProfile(token);
        return ResponseEntity.ok(profile);
    }

    //* 프로필 정보 수정
    @Operation(summary = "유저의 프로필 정보를 업데이트하는 API 입니다. [담당자] : 박재성",
            description = "프로필 정보는 이름과 닉네임입니다. 'nickName' << 대-소문자 구별에 주의해 주세요!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/profile-update")
    public ResponseEntity<UserProfileResponseDTO> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileResponseDTO updateRequestDTO) {
        UserProfileResponseDTO updatedProfile = userService.updateProfile(token, updateRequestDTO);
        return ResponseEntity.ok(updatedProfile);
    }
}
