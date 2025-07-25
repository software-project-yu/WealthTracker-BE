package com.WealthTracker.demo.domain.expend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpendResponseDTO {
    //지출id
    private Long expendId;
    //지출내용
    private String expendName;

    //날짜
    private String expendDate;


    //금액
    private Long cost;

    //분류
    private String asset;

    //카테고리
    private String category;
}
