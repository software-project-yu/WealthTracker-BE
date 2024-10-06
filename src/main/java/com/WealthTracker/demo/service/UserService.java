package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.domain.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);  // 사용자 등록

    Optional<User> getUserByEmail(String email);  // 이메일로 사용자 조회

    boolean existsByEmail(String email);  // 이메일 중복 체크

    void createPasswordResetCode(User user, String code);  // 비밀번호 재설정 코드 생성

    String validatePasswordResetCode(String code);  // 비밀번호 재설정 코드 검증

    void resetPassword(String code, String newPassword);  // 비밀번호 재설정
}
