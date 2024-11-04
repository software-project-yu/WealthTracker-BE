package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendResponseDTO;
import com.WealthTracker.demo.domain.Expend;

import java.util.List;

public interface ExpendService {
    //지출내역 쓰기
    Long writeExpend(ExpendRequestDTO expendRequestDTO, String token);

    //지출 내역 모두 보기
    List<ExpendResponseDTO> list(String token);
}
