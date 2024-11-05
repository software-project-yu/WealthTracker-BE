package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CategoryExpend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

   @OneToMany(mappedBy = "categoryExpend")
    private List<Expend> expendList=new ArrayList<>();

   @Enumerated(EnumType.STRING)
    private com.WealthTracker.demo.enums.CategoryExpend categoryName;

}
