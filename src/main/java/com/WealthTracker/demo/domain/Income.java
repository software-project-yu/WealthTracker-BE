package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.Asset;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime incomeDate;

    @Enumerated(EnumType.STRING)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private CategoryIncome categoryIncome;


    //다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    //수정 날짜
    @Nullable
    private LocalDateTime updateDate;

}
