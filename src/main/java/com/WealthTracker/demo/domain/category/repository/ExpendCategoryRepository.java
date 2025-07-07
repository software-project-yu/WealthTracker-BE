package com.WealthTracker.demo.domain.category.repository;

import com.WealthTracker.demo.domain.expend.entity.CategoryExpend;
import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.expression.spel.ast.OpInc;

import java.util.Optional;

public interface ExpendCategoryRepository extends JpaRepository<CategoryExpend, Long> {
    @Query("SELECT ec FROM CategoryExpend AS ec WHERE ec.categoryName = :categoryName")
    Optional<CategoryExpend> findByCategoryName( Category_Expend categoryName);

    Optional<CategoryExpend> findByCustomCategoryName(@Param("customCategoryName") String customCategoryName);

    //카테고리 아이디로 카테고리명 반환
    @Query("SELECT ec FROM CategoryExpend ec WHERE ec.categoryId = :categoryId")
    Optional<CategoryExpend> findCategoryExpendByCategoryId(Long categoryId);
}
