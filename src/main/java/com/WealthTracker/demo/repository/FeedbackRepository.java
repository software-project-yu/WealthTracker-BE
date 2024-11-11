package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.FeedBack;
import com.WealthTracker.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface FeedbackRepository extends JpaRepository<FeedBack,Long> {
    Optional<FeedBack> findFirstByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);
    void deleteByCreatedAtBefore(LocalDateTime date);
}
