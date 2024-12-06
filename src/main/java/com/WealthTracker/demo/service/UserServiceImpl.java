package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.UserProfileResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public UserProfileResponseDTO getProfile(String token) {
        Long userId = jwtUtil.getUserId(token); // JWT에서 userId 추출
        User user = userRepository.findById(userId) // 정보 조회
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        // DTO로 반환
        return UserProfileResponseDTO.builder()
                .name(user.getName())
                .nickName(user.getNickName())
                .build();
    }
}
