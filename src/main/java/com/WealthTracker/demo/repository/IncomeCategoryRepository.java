package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.CategoryExpend;
import com.WealthTracker.demo.domain.CategoryIncome;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.enums.Category_Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IncomeCategoryRepository extends JpaRepository<CategoryIncome,Long> {
    @Query("SELECT ci FROM CategoryIncome AS ci WHERE ci.categoryName = :categoryName")
    Optional<CategoryIncome> findByCategoryName(@Param("categoryName") Category_Income categoryName);

}
