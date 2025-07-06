package com.WealthTracker.demo.domain.auth.service;

import com.WealthTracker.demo.domain.auth.dto.CustomUserInfoDTO;
import com.WealthTracker.demo.domain.auth.dto.LoginRequestDTO;
import com.WealthTracker.demo.domain.user.entity.User;

public interface LoginService {

    User login(LoginRequestDTO loginRequestDTO);
    CustomUserInfoDTO toCustomUserInfoDTO(User user);

    User getUserById(Long userId);
}
