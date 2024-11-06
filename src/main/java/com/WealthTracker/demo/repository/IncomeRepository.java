package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Income;
import com.WealthTracker.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income,Long> {
    List<Income> findAllByUser(User user);
}
