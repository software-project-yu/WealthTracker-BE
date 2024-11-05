package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class CategoryIncome {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    private com.WealthTracker.demo.enums.CategoryIncome categoryName;

    @OneToMany(mappedBy = "categoryIncome")
    private List<Income> incomeList=new ArrayList<>();

}
