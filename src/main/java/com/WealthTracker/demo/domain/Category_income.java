package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.CategoryIncome;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class Category_income {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryIncomeId;

    @Enumerated(EnumType.STRING)
    CategoryIncome categoryName;
}
