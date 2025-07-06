package com.WealthTracker.demo.domain.category.repository;

import com.WealthTracker.demo.domain.income.entity.CategoryIncome;
import com.WealthTracker.demo.domain.category.enums.Category_Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IncomeCategoryRepository extends JpaRepository<CategoryIncome,Long> {
    @Query("SELECT ci FROM CategoryIncome AS ci WHERE ci.categoryName = :categoryName")
    Optional<CategoryIncome> findByCategoryName(@Param("categoryName") Category_Income categoryName);

}
