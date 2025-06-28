package com.WealthTracker.demo.domain.target.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private LocalDate date;

    @NotNull
    private int amount;
}
