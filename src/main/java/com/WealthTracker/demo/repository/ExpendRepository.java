package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpendRepository extends JpaRepository<Expend,Long> {
    List<Expend> findAllByUser(User user);
}
