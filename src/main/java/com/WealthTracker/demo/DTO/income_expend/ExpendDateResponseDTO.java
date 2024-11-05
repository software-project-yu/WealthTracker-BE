package com.WealthTracker.demo.DTO.income_expend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpendDateResponseDTO {
    //달 반환
    private int month;

    //주차 기록
    private int weekNum;

    //이번달 같은주 지출 총 합계
    private int thisWeekTotalCost;

    //저번달 같은 주 지출 총 합계
    private int lastWeekTotalCost;
}
