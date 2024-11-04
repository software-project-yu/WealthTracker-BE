package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class CategoryIncome {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryIncomeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incomeId")
    private Income income;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category_income categoryIncome;
}
