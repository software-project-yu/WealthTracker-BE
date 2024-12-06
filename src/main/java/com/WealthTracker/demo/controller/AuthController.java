package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.*;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.VerificationCodeRepository;
import com.WealthTracker.demo.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "회원가입 및 인증", description = "< 회원가입 / 인증 > API")
public class AuthController {

    private final SignupService signupService;
    private final VerificationCodeRepository verificationCodeRepository;

    //* 이메일 인증 코드 발송
    @Operation(summary = "회원가입 시 이메일로 인증코드 보내는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증코드 전송 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/send-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody VerificationCodeRequestDTO verificationCodeRequestDTO) {
        try {
            signupService.createVerificationCodeAndSendEmail(verificationCodeRequestDTO.getEmail());
            return ResponseEntity.ok(SuccessCode.SUCCESS_EMAIL_SENT.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.EMAIL_ALREADY_REGISTERED.getMessage());
        }
    }

    //* 이메일 인증 확인
    @Operation(summary = "이메일 인증코드 확인하는 API 입니다. [담당자] : 박재성", description = "모든 인증코드 확인은 해당 API를 이용해주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증코드 인증 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("email") String email,
                                        @RequestParam("code") String code) {
        try {
            signupService.verifyEmail(email, code);
            return ResponseEntity.ok(SuccessCode.EMAIL_VERIFIED.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.INVALID_VERIFICATION_CODE.getMessage());
        }
    }

    //* 인증 코드 재생성 요청 (이메일로 새로운 코드 발송)
    @Operation(summary = "인증코드 만료 시 재발급하는 API 입니다. [담당자] : 박재성", description = "이메일 인증코드 만료시간은 5분입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증코드 재전송 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestBody VerificationCodeRequestDTO requestDTO) {
        try {
            // 기존 인증 코드 삭제
            verificationCodeRepository.deleteByEmail(requestDTO.getEmail());
            // 새로운 인증 코드 생성 및 이메일 발송
            signupService.createVerificationCodeAndSendEmail(requestDTO.getEmail());
            return ResponseEntity.ok(SuccessCode.SUCCESS_RESET_CODE_SENT.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }

    //* 회원가입 및 유저 등록
    @Operation(summary = "회원가입 API 입니다. [담당자] : 박재성", description = "인증코드 확인 절차를 반드시 거쳐야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDTO requestDTO) {
        try {
            signupService.signupUser(requestDTO);
            return ResponseEntity.ok(SuccessCode.SUCCESS_SIGNUP.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.EMAIL_CONFLICT.getMessage());
        }
    }

    //* 비밀번호 재설정 요청
    @Operation(summary = "비밀번호 재설정 시 이메일로 인증코드를 보내는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 재설정 인증코드 전송 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid VerificationCodeRequestDTO request) {
        try {
            // 비밀번호 재설정 코드 생성 및 이메일 발송 로직 처리
            signupService.createPasswordResetCode(request.getEmail());
            return ResponseEntity.ok(SuccessCode.SUCCESS_RESET_CODE_SENT.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }

    //* 비밀번호 재설정 확인
    @Operation(summary = "비밀번호를 재설정하는 API 입니다. [담당자] : 박재성", description = "인증코드 및 재설정 할 비밀번호를 같이 입력해주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<?> confirmResetPassword(@RequestBody @Valid VerificationCodeConfirmDTO confirmDTO) {
        String result = signupService.validatePasswordResetCode(confirmDTO.getCode());
        if (!result.equals("valid")) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_RESET_INVALID.getMessage());
        }
        try {
            // 비밀번호 재설정
            signupService.resetPassword(confirmDTO.getCode(), confirmDTO.getNewPassword());
            return ResponseEntity.ok(SuccessCode.SUCCESS_PASSWORD_RESET.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_RESET_INVALID.getMessage());
        }
    }

    //* 비밀번호 확인
    @Operation(summary = "설정 -> 비밀번호 변경 시 확인하는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 확인 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/confirm-password")
    public ResponseEntity<?> confirmPassword(@RequestHeader("Authorization") String token,
                                             @RequestBody PasswordConfirmDTO passwordConfirmDTO) {
        try {
            signupService.confirmPassword(token, passwordConfirmDTO);
            return ResponseEntity.ok(SuccessCode.SUCCESS_PASSWORD_CONFIRM.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_MISMATCH.getMessage());
        }
    }
}
