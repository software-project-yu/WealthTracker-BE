package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpendRepository extends JpaRepository<Expend,Long> {
    List<Expend> findAllByUser(User user);

    Optional<Expend> findByExpendId(Long expendId);
}
