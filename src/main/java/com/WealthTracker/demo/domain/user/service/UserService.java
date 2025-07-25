package com.WealthTracker.demo.domain.user.service;


import com.WealthTracker.demo.domain.user.dto.UserProfileResponseDTO;

public interface UserService {
    UserProfileResponseDTO getProfile(String token); // 토큰을 받아서 프로필 정보 조회
    UserProfileResponseDTO updateProfile(String token, UserProfileResponseDTO updateRequestDTO); // 토큰을 받아서 프로필 정보 수정

}
