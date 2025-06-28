package com.WealthTracker.demo.domain.expend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpendDayResponseDTO {
    // 날짜 인덱스 (1 ~ 14일 차)

    int dayNum;

    //해당 날짜의 지출 총액
    Long costSum;
}
