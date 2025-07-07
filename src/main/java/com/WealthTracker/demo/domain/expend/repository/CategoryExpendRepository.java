package com.WealthTracker.demo.domain.expend.repository;

import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import com.WealthTracker.demo.domain.expend.entity.CategoryExpend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryExpendRepository extends JpaRepository<CategoryExpend,Long>{
    long countByCategoryName(Category_Expend categoryName);
}
