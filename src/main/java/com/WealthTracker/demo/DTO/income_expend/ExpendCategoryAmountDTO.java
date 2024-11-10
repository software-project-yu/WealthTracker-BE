package com.WealthTracker.demo.DTO.income_expend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpendCategoryAmountDTO {
    //카테고리 이름
    private String categoryName;

    //지츌 총합
    private  int amount;

    //지출 퍼센트
    private int percent;

    //지출 리스트
    private List<ExpendResponseDTO> expendList;
}
