package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Income {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

    //수입비
    private Long cost;

    //수입내용
    private String incomeName;

    //수입일자
    @CreatedDate
    private LocalDateTime incomeDate;

    //다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
