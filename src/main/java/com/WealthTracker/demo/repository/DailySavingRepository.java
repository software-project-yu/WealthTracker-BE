package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.DailySaving;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySavingRepository extends JpaRepository<DailySaving, Long> {
}
