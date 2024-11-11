package com.WealthTracker.demo.DTO.income_expend;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpendIncomeResponseDTO {
    //지출 or 수입 타입
    private String type;
    //지출,수입 아이디
    private Long id;

    //날짜
    private String date;

    //카테고리
    private String category;

    //자산
    private String asset;

    //내용
    private String content;

}
