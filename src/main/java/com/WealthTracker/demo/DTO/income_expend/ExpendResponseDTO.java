package com.WealthTracker.demo.DTO.income_expend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ExpendResponseDTO {
    //지출내용
    private String expendContent;

    //날짜
    private String date;

    //지불방법
    private String expendMethod;

    //금액
    private Long cost;

    //분류
    private String asset;
}
