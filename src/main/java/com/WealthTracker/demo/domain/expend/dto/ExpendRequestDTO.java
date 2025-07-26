package com.WealthTracker.demo.domain.expend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//지출 요청받을 DTO
public class ExpendRequestDTO {
    //날짜
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expendDate;
    //금액
    @NotNull
    @Size(min = 1,message = "수입 금액은 1자리 이상 입력.")
    private Long cost;

    //카테고리
    @NotNull
    @Size(min = 1,message = "알맞은 카테고리 값 입력.")
    private String category;

    //내용
    @NotNull
    @Size(min = 1,max = 255,message = "1자이상 255자이하로 작성.")
    private String expendName;

    //자산
    @NotNull
    @Size(min = 1,message ="알맞은 카테고리 입력." )
    private String asset;


}
