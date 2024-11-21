package com.WealthTracker.demo.service;


import com.WealthTracker.demo.DTO.UserProfileResponseDTO;

public interface UserService {
    UserProfileResponseDTO getProfile(String token); // 토큰을 받아서 프로필 정보 조회
}
