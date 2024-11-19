package com.WealthTracker.demo.DTO.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetResponseDTO {

    private Long targetId;

    private int targetAmount;

    private int savedAmount;

    private double achievementRate;
}
