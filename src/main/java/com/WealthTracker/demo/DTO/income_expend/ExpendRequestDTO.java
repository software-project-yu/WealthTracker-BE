package com.WealthTracker.demo.DTO.income_expend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//지출 요청받을 DTO
public class ExpendRequestDTO {
    //날짜
    private String expendDate;
    //금액
    private Long cost;

    //카테고리
    private String category;

    //내용
    private String expendName;

    //자산
    private String asset;


}
