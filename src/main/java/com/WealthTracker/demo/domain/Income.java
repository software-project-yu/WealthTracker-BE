package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.Asset;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "income")
public class Income {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

    //수입비
    private Long cost;

    //수입내용
    private String incomeName;

    //수입일자
    private LocalDateTime incomeDate;

    @Enumerated(EnumType.STRING)
    private Asset asset;

    //수입 기록 시간
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime createdAt;

    //다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private CategoryIncome categoryIncome;


    //수정 날짜
    @Nullable
    private LocalDateTime updateDate;


}
