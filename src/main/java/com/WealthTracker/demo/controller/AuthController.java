package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.VerificationCodeConfirmDTO;
import com.WealthTracker.demo.DTO.VerificationCodeRequestDTO;
import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.domain.VerificationCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.VerificationCodeRepository;
import com.WealthTracker.demo.service.EmailService;
import com.WealthTracker.demo.service.SignupService;
import com.WealthTracker.demo.util.VerificationCodeUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController { //** Signup 및 EmailAuth 담당 Controller **//

    private final SignupService signupService;
    private final VerificationCodeRepository verificationCodeRepository;

    //* 이메일 인증 코드 발송
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

    //* 회원가입 및 유저 등록
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDTO requestDTO) {
        try {
            signupService.signupUser(requestDTO);
            return ResponseEntity.ok(SuccessCode.SUCCESS_SIGNUP.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.EMAIL_CONFLICT.getMessage());
        }
    }


    //* 인증 코드 재생성 요청 (이메일로 새로운 코드 발송)
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

    //* 비밀번호 재설정 요청
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
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<?> confirmResetPassword(@RequestParam("code") String code, @RequestBody @Valid  VerificationCodeConfirmDTO confirmDTO) {
        String result = signupService.validatePasswordResetCode(code);
        if (!result.equals("valid")) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_RESET_INVALID.getMessage());
        }
        try {
            // 비밀번호 재설정
            signupService.resetPassword(code, confirmDTO.getNewPassword());
            return ResponseEntity.ok(SuccessCode.SUCCESS_PASSWORD_RESET.getMessage());
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(ErrorCode.PASSWORD_RESET_INVALID.getMessage());
        }
    }
}
