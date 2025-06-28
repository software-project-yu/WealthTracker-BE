package com.WealthTracker.demo.domain.income.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeRequestDTO {
    //날짜
    @NotNull
    @Size(min = 10,max = 10,message = "날짜는 반드시 yyyy-MM-dd 형식으로 입력")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String incomeDate;
    //금액
    @NotNull
    @Size(min = 1,message = "지출 금액은 1자리 이상 입력.")
    private Long cost;

    //카테고리
    @NotNull
    @Size(min = 1,message = "알맞은 카테고리 값 입력.")
    private String category;

    //내용
    @NotNull
    @Size(min = 1,max = 255,message = "1자이상 255자이하로 작성.")
    private String incomeName;

    //자산
    @NotNull
    @Size(min = 1,message = "알맞은 카테고리 입력.")
    private String asset;
}
