package com.WealthTracker.demo.domain.category.dto;

import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTargetResponseDTO {
    private Category_Expend category;
    private Long targetAmount;
    private Long currentExpend; // 현재 지출
    private String message; // 목표 달성 여부
}
