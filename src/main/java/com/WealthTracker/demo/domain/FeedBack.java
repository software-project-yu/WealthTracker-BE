package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "Feedback")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedBack {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime createdAt;

}
