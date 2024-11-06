package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "Feedback")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedBack {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @Lob
    private String content;

    @DateTimeFormat
    private LocalDateTime createdAt;

}
