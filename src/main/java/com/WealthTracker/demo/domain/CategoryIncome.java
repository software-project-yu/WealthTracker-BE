package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.Category_Income;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "category_income")
public class CategoryIncome {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @OneToMany(mappedBy = "categoryIncome",cascade = CascadeType.ALL)
    private List<Income> incomeList=new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category_Income categoryName;

}
