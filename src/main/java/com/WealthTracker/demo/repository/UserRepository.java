package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
