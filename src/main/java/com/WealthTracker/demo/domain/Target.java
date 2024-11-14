package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private int targetAmount;

    private int savedAmount;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailySaving> dailySavings = new ArrayList<>();

    @Builder
    public Target(User user, int targetAmount, int savedAmount) {
        this.user = user;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
    }

    public void addSaving(DailySaving dailySaving) {
        dailySavings.add(dailySaving);
        savedAmount += dailySaving.getAmount();
    }

    public double getAchievementRate() {
        if (targetAmount == 0) return 0.0;
        return ((double) savedAmount / targetAmount) * 100;
    }
}
