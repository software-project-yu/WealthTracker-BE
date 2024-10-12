package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.CategoryExpend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpendCategoryRepository extends JpaRepository<CategoryExpend,Long> {
}
