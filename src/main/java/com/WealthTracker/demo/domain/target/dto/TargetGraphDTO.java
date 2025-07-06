package com.WealthTracker.demo.domain.target.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetGraphDTO {
    private int targetAmount;//목표 금액
    private int nowAmount;//현재 금액
}
