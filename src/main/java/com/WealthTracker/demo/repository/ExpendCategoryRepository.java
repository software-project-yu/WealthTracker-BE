package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.CategoryExpend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpendCategoryRepository extends JpaRepository<CategoryExpend,Long> {
    @Query("SELECT ec FROM CategoryExpend AS ec WHERE ec.categoryName = :categoryName")
    Optional<CategoryExpend> findByCategoryName(@Param("categoryName") com.WealthTracker.demo.enums.CategoryExpend categoryName);
}
