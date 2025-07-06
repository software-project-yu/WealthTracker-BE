package com.WealthTracker.demo.domain.expend.dto;

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
    private  Long amount;

    //지출 퍼센트
    private int percent;

    //up or down
    private String upOrDown;

    //지출 리스트
    private List<ExpendResponseDTO> expendList;
}
