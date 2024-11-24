package com.WealthTracker.demo.DTO.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetRequestDTO {

    private int targetAmount;

    private LocalDate startDate;

    private LocalDate endDate;
}
