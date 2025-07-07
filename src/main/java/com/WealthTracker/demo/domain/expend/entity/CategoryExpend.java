package com.WealthTracker.demo.domain.expend.entity;

import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "category_expend")
public class CategoryExpend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

   @OneToMany(mappedBy = "categoryExpend",cascade = CascadeType.ALL)
    private List<Expend> expendList=new ArrayList<>();

   @Enumerated(EnumType.STRING)
    private Category_Expend categoryName;

   @Column(unique = true)
   private String customCategoryName;

   public static CategoryExpend createBaseCategory(Category_Expend categoryExpend){
       return CategoryExpend.builder()
               .categoryName(categoryExpend)
               .build();
   }

   public static CategoryExpend createCustomCategory(String customName){
       return CategoryExpend.builder()
               .customCategoryName(customName)
               .build();
   }
}
