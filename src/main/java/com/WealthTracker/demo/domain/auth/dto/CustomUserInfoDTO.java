package com.WealthTracker.demo.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserInfoDTO {

    private Long userId;

    private String email;

    private String name;
}