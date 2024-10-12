package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income,Long> {
}
