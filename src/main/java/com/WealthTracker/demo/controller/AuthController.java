package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.VerificationCodeConfirmDTO;
import com.WealthTracker.demo.DTO.VerificationCodeRequestDTO;
import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.domain.VerificationCode;
import com.WealthTracker.demo.repository.VerificationCodeRepository;
import com.WealthTracker.demo.service.EmailService;
import com.WealthTracker.demo.service.UserService;
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

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationCodeUtil verificationCodeUtil;
    private final VerificationCodeRepository verificationCodeRepository;

    //** 회원가입 요청 (이메일 인증 코드 발송)
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDTO signupRequest) {
        // 이메일 중복 체크
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("이미 등록된 이메일입니다.");
        }

        // 사용자 정보 저장 (enabled=false)
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword()) // 비밀번호 암호화는 서비스에서 처리
                .name(signupRequest.getName())
                .nickName(signupRequest.getNickName())
                .enabled(false) // 인증 전 상태
                .build();
        User savedUser = userService.registerUser(user);

        // 인증 코드 생성 및 저장
        String code = verificationCodeUtil.generateVerificationCode();
        VerificationCode verificationCode = VerificationCode.builder()
                .code(code)
                .email(savedUser.getEmail()) // email 필드 설정
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        verificationCodeRepository.save(verificationCode);

        // 이메일로 인증 코드 발송
        emailService.sendEmailVerification(signupRequest.getEmail(), code);

        return ResponseEntity.ok("이메일을 확인하여 인증을 완료해주세요.");
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
            return ResponseEntity.badRequest().body("인증 코드가 만료되었습니다.");
        }

        // 사용자 활성화
        User user = verificationCode.getUser();
        user.enable(); // 계정 활성화
        userService.registerUser(user);

        // 인증 코드 삭제 (선택 사항)
        verificationCodeRepository.delete(verificationCode);

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    //** 비밀번호 재설정 요청
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody VerificationCodeRequestDTO request) {
        Optional<User> userOpt = userService.getUserByEmail(request.getEmail());
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body("해당 이메일을 가진 사용자가 없습니다.");
        }

        User user = userOpt.get();
        String code = verificationCodeUtil.generateVerificationCode();
        userService.createPasswordResetCode(user, code);
        emailService.sendPasswordReset(user.getEmail(), code);

        return ResponseEntity.ok("비밀번호 재설정 이메일이 발송되었습니다.");
    }

    //** 비밀번호 재설정 확인
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<?> confirmResetPassword(@RequestParam("code") String code, @RequestBody VerificationCodeConfirmDTO confirmDTO) {
        String result = userService.validatePasswordResetCode(code);
        if (!result.equals("valid")) {
            return ResponseEntity.badRequest().body("비밀번호 재설정 코드가 유효하지 않습니다.");
        }

        userService.resetPassword(code, confirmDTO.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
