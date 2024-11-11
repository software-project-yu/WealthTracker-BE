package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.CustomUserInfoDTO;
import com.WealthTracker.demo.DTO.LoginRequestDTO;
import com.WealthTracker.demo.domain.User;

public interface LoginService {

    User login(LoginRequestDTO loginRequestDTO);
    CustomUserInfoDTO toCustomUserInfoDTO(User user);

    User getUserById(Long userId);
}
