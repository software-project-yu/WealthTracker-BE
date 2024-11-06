package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedBack,Long> {

}
