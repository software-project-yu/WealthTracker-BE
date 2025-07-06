package com.WealthTracker.demo.domain.user.repository;

import com.WealthTracker.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { // Email로 사용자의 정보를 조회 및 저장하는 Repository

    Optional<User> findByUserId(Long userId);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email); // 이메일 존재 여부 확인 메서드
}
