package com.WealthTracker.demo.domain.payment.entity;

import com.WealthTracker.demo.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Builder (toBuilder = true)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@NoArgsConstructor

public class Payment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique = true)
    private Long paymentId;

    private LocalDateTime dueDate;

    private Long cost;

    private String tradeName;

    private LocalDateTime lastPayment;


    private String paymentDetail;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    // 결제 예정일 기록 시간
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime createdAt;
}