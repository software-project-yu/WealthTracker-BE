package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.CategoryExpend;
import jakarta.persistence.*;

@Entity
public class Category_expend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    CategoryExpend categoryName;
}
