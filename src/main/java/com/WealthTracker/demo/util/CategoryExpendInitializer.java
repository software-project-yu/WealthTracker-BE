package com.WealthTracker.demo.util;

import com.WealthTracker.demo.domain.CategoryExpend;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.repository.ExpendRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryExpendInitializer {
    private final ExpendCategoryRepository categoryRepository;
    private final ExpendRepository expendRepository;

    @PostConstruct
    public void init(){
        // 카테고리가 데이터베이스에 없다면 기본 카테고리들 삽입
        if(categoryRepository.count()!=6){
            for (Category_Expend category : Category_Expend.values()) {
               if(categoryRepository.findByCategoryName(category).isEmpty()){
                   categoryRepository.save(CategoryExpend.builder()
                           .categoryName(category)
                           .build());
               }
            }
        }
    }
}
