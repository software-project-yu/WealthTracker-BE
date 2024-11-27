package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.CustomUserInfoDTO;
import com.WealthTracker.demo.DTO.LoginRequestDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //** SpringBean에 등록한 passwordEncoder를 주입해주기!

    @Override
    public User login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // enabled 값이 false인 경우 로그인 불가
        if (!user.isEnabled()) {
            throw new CustomException(ErrorCode.EMAIL_VERIFY_NEED);
        }

        // enabled 값이 false인 경우 로그인 불가
        if (!user.isEnabled()) {
            throw new RuntimeException("이메일 인증이 완료되지 않았습니다. 인증 후 다시 시도해주세요.");
        }

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
