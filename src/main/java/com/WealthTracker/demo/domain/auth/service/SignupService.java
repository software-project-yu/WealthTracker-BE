package com.WealthTracker.demo.domain.auth.service;

import com.WealthTracker.demo.domain.auth.dto.PasswordConfirmDTO;
import com.WealthTracker.demo.domain.auth.dto.SignupRequestDTO;
import com.WealthTracker.demo.domain.user.entity.User;

import java.util.Optional;

public interface SignupService {

    String signupUser(SignupRequestDTO signupRequestDTO); // 유저 회원가입 메서드

    Optional<User> getUserByEmail(String email);  // 이메일로 사용자 조회

    void cleanupExpiredVerificationCodes(); // 만료된 인증코드 삭제

    String createVerificationCodeAndSendEmail(String email); // 새로운 인증코드 생성

    void verifyEmail(String email, String code); // 이메일 인증코드 검증

    String confirmPassword(String token, PasswordConfirmDTO passwordConfirmDTO); // 비밀번호 확인 메서드

    void createPasswordResetCode(String email);  // 비밀번호 재설정 코드 생성

    String validatePasswordResetCode(String code);  // 비밀번호 재설정 코드 검증

    void resetPassword(String code, String newPassword);  // 비밀번호 재설정
}
