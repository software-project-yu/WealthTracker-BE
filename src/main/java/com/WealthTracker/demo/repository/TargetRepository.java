package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Optional<Target> findByTargetIdAndUserUserId(Long targetId, Long userId);
}
