package com.WealthTracker.demo.domain;

import com.WealthTracker.demo.enums.Category_Expend;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorytargetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Category_Expend category;

    private Long targetAmount;
}
