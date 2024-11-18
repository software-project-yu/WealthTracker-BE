package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.Asset;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;
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
@Table(name = "expend")
public class Expend {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long expendId;

    //비용
    private Long cost;

    //지출일자
    private LocalDateTime expendDate;

    //지출내용
    private String expendName;


    //자산
    @Enumerated(EnumType.STRING)
    private Asset asset;

    //지출 기록 시간
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime createdAt;

    //수정 날짜
    @Nullable
    private LocalDateTime updateDate;

    //유저 ID
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    //카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private CategoryExpend categoryExpend;

    @PrePersist
    @PreUpdate
    public void setDefaultUpdateDate() {
        if (this.updateDate == null) {
            this.updateDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0); // 기본값 1970-01-01 00:00:00
        }
    }
}
