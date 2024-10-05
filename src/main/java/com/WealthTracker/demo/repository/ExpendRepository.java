package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Expend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpendRepository extends JpaRepository<Expend,Long> {
}
