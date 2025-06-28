package com.WealthTracker.demo.domain.auth.service;

import com.WealthTracker.demo.domain.auth.dto.CustomUserInfoDTO;
import com.WealthTracker.demo.domain.auth.dto.LoginRequestDTO;
import com.WealthTracker.demo.global.constants.ErrorCode;
import com.WealthTracker.demo.domain.user.entity.User;
import com.WealthTracker.demo.global.error.CustomException;
import com.WealthTracker.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //** SpringBean에 등록한 passwordEncoder를 주입해주기!

    @Override
    public User login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())

                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        // enabled 값이 false인 경우 로그인 불가
        if (!user.isEnabled()) {
            throw new CustomException(ErrorCode.EMAIL_VERIFY_NEED,ErrorCode.EMAIL_VERIFY_NEED.getMessage());

        }

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH,ErrorCode.PASSWORD_MISMATCH.getMessage());
        }

        return user;
    }

    @Override
    public CustomUserInfoDTO toCustomUserInfoDTO(User user) {
        return CustomUserInfoDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }
}
