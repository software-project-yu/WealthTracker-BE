package com.WealthTracker.demo.domain.target.repository;

import com.WealthTracker.demo.domain.target.entity.DailySaving;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySavingRepository extends JpaRepository<DailySaving, Long> {
}
