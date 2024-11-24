package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.domain.User;

import java.util.Optional;

public interface SignupService {

    String signupUser(SignupRequestDTO signupRequestDTO); // 유저 회원가입 메서드

    Optional<User> getUserByEmail(String email);  // 이메일로 사용자 조회

    void enableUser(User user); // 사용자 활성화 메서드

    void createPasswordResetCode(String email);  // 비밀번호 재설정 코드 생성

    String validatePasswordResetCode(String code);  // 비밀번호 재설정 코드 검증

    void resetPassword(String code, String newPassword);  // 비밀번호 재설정
}