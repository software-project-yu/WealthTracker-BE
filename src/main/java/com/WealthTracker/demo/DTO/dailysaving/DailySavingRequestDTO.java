package com.WealthTracker.demo.DTO.dailysaving;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySavingRequestDTO {

    private LocalDate date;

    private int amount;
}
