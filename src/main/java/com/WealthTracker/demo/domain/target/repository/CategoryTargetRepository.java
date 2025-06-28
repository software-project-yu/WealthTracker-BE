package com.WealthTracker.demo.domain.target.repository;

import com.WealthTracker.demo.domain.target.entity.CategoryTarget;
import com.WealthTracker.demo.domain.user.entity.User;
import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryTargetRepository extends JpaRepository<CategoryTarget, Long> {
    Optional<CategoryTarget> findByUserAndCategory(User user, Category_Expend category);
}
