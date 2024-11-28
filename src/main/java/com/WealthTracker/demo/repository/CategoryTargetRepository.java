package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.CategoryTarget;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Category_Expend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryTargetRepository extends JpaRepository<CategoryTarget, Long> {
    Optional<CategoryTarget> findByUserAndCategory(User user, Category_Expend category);
}
