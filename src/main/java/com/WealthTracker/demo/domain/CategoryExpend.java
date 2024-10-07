package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryExpend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryExpendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expendId")
    private Expend expend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category_expend categoryExpend;
}
