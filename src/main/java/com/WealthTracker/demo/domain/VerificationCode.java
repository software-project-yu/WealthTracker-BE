package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode { // 이메일 인증을 위한 토큰 Entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String code; //** 6자리 인증 코드

    private LocalDateTime expiryDate;

    public VerificationCode(String code, String email, LocalDateTime expiryDate) {
        this.code = code;
        this.email = email;
        this.expiryDate = expiryDate;
    }

}
