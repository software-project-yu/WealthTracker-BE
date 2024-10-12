package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.CategoryIncome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeCategoryRepository extends JpaRepository<CategoryIncome,Long> {
}
