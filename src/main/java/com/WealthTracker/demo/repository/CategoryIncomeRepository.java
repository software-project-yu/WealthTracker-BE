package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Category_income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryIncomeRepository extends JpaRepository<Category_income,Long> {
}
