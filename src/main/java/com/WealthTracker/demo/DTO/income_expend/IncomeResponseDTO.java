package com.WealthTracker.demo.DTO.income_expend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResponseDTO {
    //수입 id
    private Long incomeId;
    //날짜
    private String incomeDate;
    //금액
    private Long cost;

    //카테고리
    private String category;

    //내용
    private String incomeName;

    //자산
    private String asset;
}
