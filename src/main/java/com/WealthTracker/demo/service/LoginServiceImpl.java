package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.CustomUserInfoDTO;
import com.WealthTracker.demo.DTO.LoginRequestDTO;
import com.WealthTracker.demo.domain.User;
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
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(readOnly = true) //** 읽기 전용 트랜잭션으로 성능 최적화
    public User login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomUserInfoDTO toCustomUserInfoDTO(User user) {
        return CustomUserInfoDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
