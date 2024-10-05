package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Expend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long expendId;

    //비용
    private Long cost;

    //지출일자
    @CreatedDate
    private LocalDateTime expendDate;

    //지출내용
    private String expendName;

    //지출 방법


    //유저 ID
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}
