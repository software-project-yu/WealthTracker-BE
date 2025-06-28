package com.WealthTracker.demo.infrastructure.repository;

import com.WealthTracker.demo.infrastructure.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> { // Email 인증 코드를 조회와 저장하는 Repository
    Optional<VerificationCode> findByCode(String Code);

    Optional<VerificationCode> findByEmail(String email);

    void deleteByExpiryDateBefore(LocalDateTime now);

    void deleteByEmail(String email);
}
