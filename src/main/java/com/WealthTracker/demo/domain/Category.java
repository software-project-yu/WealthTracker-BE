package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.CategoryType;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    //비용
    private Long cost;

    //enum
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
}
