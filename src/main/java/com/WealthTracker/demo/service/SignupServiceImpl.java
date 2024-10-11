package com.WealthTracker.demo.service;

import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.domain.VerificationCode;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.repository.VerificationCodeRepository;
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

    @Override
    @Transactional
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // 암호화된 비밀번호를 저장
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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
            return "invalidCode";
        }

        VerificationCode verificationCode = optionalCode.get();
        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "expired";
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