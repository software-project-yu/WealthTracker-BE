package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.domain.VerificationCode;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.repository.VerificationCodeRepository;
import com.WealthTracker.demo.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationCodeUtil verificationCodeUtil;

    @Override
    @Transactional
    public String signupUser(SignupRequestDTO signupRequest) {
        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // 2. 사용자 정보 생성 및 비밀번호 암호화
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword())) // 비밀번호 암호화
                .name(signupRequest.getName())
                .nickName(signupRequest.getNickName())
                .enabled(false) // 인증 전 상태
                .build();

        // 3. 사용자 저장
        User savedUser = userRepository.save(user);

        // 4. 인증 코드 생성 및 저장
        String code = verificationCodeUtil.generateVerificationCode();
        VerificationCode verificationCode = VerificationCode.builder()
                .code(code)
                .email(savedUser.getEmail()) // email 필드 설정
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        verificationCodeRepository.save(verificationCode);

        // 5. 이메일로 인증 코드 발송
        emailService.sendEmailVerification(savedUser.getEmail(), code);

        return "이메일을 확인하여 인증을 완료해주세요.";
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 비밀번호 재설정 코드 생성 및 저장
    @Override
    @Transactional
    public void createPasswordResetCode(User user, String code) {
        VerificationCode passwordResetCode = VerificationCode.builder()
                .code(code)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(5))  // 5분 유효
                .build();
        verificationCodeRepository.save(passwordResetCode);
    }

    @Override
    @Transactional(readOnly = true)
    public String validatePasswordResetCode(String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByCode(code);
        if (!optionalCode.isPresent()) {
            return "유효하지 않은 코드";
        }

        VerificationCode verificationCode = optionalCode.get();
        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "코드가 만료되었습니다.";
        }

        return "valid";
    }

    @Override
    @Transactional
    public void resetPassword(String code, String newPassword) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByCode(code);
        if (!optionalCode.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 코드");
        }

        VerificationCode verificationCode = optionalCode.get();
        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("코드가 만료되었습니다.");
        }

        User user = verificationCode.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        verificationCodeRepository.delete(verificationCode);
    }

    @Override
    @Transactional
    public void enableUser(User user) {
        user.enable();
        userRepository.save(user);
    }
}