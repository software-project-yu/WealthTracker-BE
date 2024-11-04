package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.Asset;
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
    private String incomeDate;

    @Enumerated(EnumType.STRING)
    private Asset asset;


    //다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
