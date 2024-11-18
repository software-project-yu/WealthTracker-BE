package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.Category_Expend;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "category_expend")
public class CategoryExpend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

   @OneToMany(mappedBy = "categoryExpend",cascade = CascadeType.ALL)
    private List<Expend> expendList=new ArrayList<>();

   @Enumerated(EnumType.STRING)
    private Category_Expend categoryName;

}
