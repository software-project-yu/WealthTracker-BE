package com.WealthTracker.demo.domain.expend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExpendDateResponseDTO {
    //달 반환
    private int month;

    //주차 기록
    private int weekNum;

    //이번달 같은주 지출 총 합계
    private int thisMonthTotalCost;

    //저번달 같은 주 지출 총 합계
    private int prevMonthTotalCost;
}
