package com.WealthTracker.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class User {
    @Id
    @Column(unique = true)
    private Long useId;

    //카테고리Id
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Expend> expends=new ArrayList<>();



}
