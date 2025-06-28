package com.WealthTracker.demo.domain.target.entity;

import com.WealthTracker.demo.domain.target.entity.Target;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class DailySaving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetId", nullable = false)
    private Target target;

    private LocalDate date;

    private int amount;

    @Builder
    public DailySaving(Target target, LocalDate date, int amount) {
        this.target = target;
        this.date = date;
        this.amount = amount;
    }
}
