package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.domain.VerificationCode;
import com.WealthTracker.demo.error.CustomException;
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
        // 1. 이메일 중복 체크 및 유효한 유저 존재 여부 확인
        Optional<User> existingUserOpt = userRepository.findByEmail(signupRequest.getEmail());
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            if (existingUser.isEnabled()) {
                throw new CustomException(ErrorCode.EMAIL_ALREADY_REGISTERED);
            }
        }

        // 2. 기존 인증 코드 삭제 (유효하지 않은 인증 코드 제거)
        verificationCodeRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(verificationCode -> {
                    if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
                        verificationCodeRepository.delete(verificationCode);
                    } else {
                        throw new CustomException(ErrorCode.EMAIL_ALREADY_SENT);
                    }
                });

        // 3. 사용자 정보 생성 및 비밀번호 암호화
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword())) // 비밀번호 암호화
                .name(signupRequest.getName())
                .nickName(signupRequest.getNickName())
                .enabled(false) // 인증 전 상태
                .build();
      
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
    public void createPasswordResetCode(String email) {
        // User 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 랜덤 코드 생성
        String code = verificationCodeUtil.generateVerificationCode();

        // VerificationCode 생성
        VerificationCode verificationCode = VerificationCode.builder()
                .code(code)
                .user(user)
                .email(user.getEmail())
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        verificationCodeRepository.save(verificationCode);
        emailService.sendPasswordReset(user.getEmail(), code); // 코드 발송
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
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        VerificationCode verificationCode = optionalCode.get();
        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        User user = verificationCode.getUser();

        // 기존 비밀번호와 새 비밀번호 비교
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        // 비밀번호 업데이트
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 사용된 인증 코드 삭제
        verificationCodeRepository.delete(verificationCode);
    }

    @Override
    @Transactional
    public void enableUser(User user) {
        user.enable();
        userRepository.save(user);
    }
}