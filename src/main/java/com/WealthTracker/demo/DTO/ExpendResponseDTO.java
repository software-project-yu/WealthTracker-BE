package com.WealthTracker.demo.DTO;

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

    //상호
    private String companyName;

    //날짜
    private String date;

    //지불방법
    private String expendMethod;

    //금액
    private Long cost;
}
