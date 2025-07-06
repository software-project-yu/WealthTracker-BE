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
public class CategoryTargetRequestDTO {
    private Category_Expend category;
    private Long targetAmount;
}
