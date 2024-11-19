package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.VerificationCodeConfirmDTO;
import com.WealthTracker.demo.DTO.VerificationCodeRequestDTO;
import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.domain.VerificationCode;
import com.WealthTracker.demo.repository.VerificationCodeRepository;
import com.WealthTracker.demo.service.EmailService;
import com.WealthTracker.demo.service.SignupService;
import com.WealthTracker.demo.util.VerificationCodeUtil;
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
    private final EmailService emailService;
    private final VerificationCodeUtil verificationCodeUtil;
    private final VerificationCodeRepository verificationCodeRepository;

    //** 회원가입 요청 (이메일 인증 코드 발송)
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDTO signupRequest) {
        try {
            String message = signupService.signupUser(signupRequest);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //** 이메일 인증 확인 및 회원가입 완료 (유저 정보 저장)
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("email") String email,
                                        @RequestParam("code") String code) {
        // 이메일과 코드로 인증 코드 확인
        Optional<VerificationCode> verificationCodeOpt = verificationCodeRepository.findByEmailAndCode(email, code);
        if (!verificationCodeOpt.isPresent()) {
            return ResponseEntity.badRequest().body("인증 코드가 유효하지 않습니다.");
        }

        VerificationCode verificationCode = verificationCodeOpt.get();

        // 코드 만료 여부 확인
        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            // 인증 코드가 만료되었으면 삭제
            verificationCodeRepository.delete(verificationCode);
            return ResponseEntity.badRequest().body("인증 코드가 만료되었습니다.");
        }

        // 사용자 활성화
        User user = verificationCode.getUser();
        signupService.enableUser(user);

        // 인증 코드 삭제
        verificationCodeRepository.delete(verificationCode);

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    //** 인증 코드 재생성 요청 (이메일로 새로운 코드 발송)
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestBody VerificationCodeRequestDTO requestDTO) {
        // 기존 인증 코드 삭제
        verificationCodeRepository.deleteByEmail(requestDTO.getEmail());

        // User 조회
        Optional<User> userOpt = signupService.getUserByEmail(requestDTO.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("해당 이메일로 등록된 사용자를 찾을 수 없습니다.");
        }
        User user = userOpt.get();

        // 새로운 인증 코드 생성
        String newCode = verificationCodeUtil.generateVerificationCode();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5);

        // 새로운 인증 코드 저장
        VerificationCode newVerificationCode = VerificationCode.builder()
                .email(requestDTO.getEmail())
                .code(newCode)
                .expiryDate(expiryDate)
                .user(user)
                .build();
        verificationCodeRepository.save(newVerificationCode);

        // 이메일 발송
        emailService.sendEmailVerification(requestDTO.getEmail(), newCode);

        return ResponseEntity.ok("새로운 인증 코드가 이메일로 발송되었습니다.");
    }

    //** 비밀번호 재설정 요청
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody VerificationCodeRequestDTO request) {
        try {
            // 비밀번호 재설정 코드 생성 및 이메일 발송 로직 처리
            signupService.createPasswordResetCode(request.getEmail());
            return ResponseEntity.ok("비밀번호 재설정 이메일이 발송되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //** 비밀번호 재설정 확인
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<?> confirmResetPassword(@RequestParam("code") String code, @RequestBody VerificationCodeConfirmDTO confirmDTO) {
        String result = signupService.validatePasswordResetCode(code);
        if (!result.equals("valid")) {
            return ResponseEntity.badRequest().body("비밀번호 재설정 코드가 유효하지 않습니다.");
        }
        try {
            // 비밀번호 재설정
            signupService.resetPassword(code, confirmDTO.getNewPassword());
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
