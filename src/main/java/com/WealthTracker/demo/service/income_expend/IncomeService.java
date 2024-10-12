package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;

import java.util.List;

public interface IncomeService {
    Long writeIncome(IncomeRequestDTO incomeRequestDTO,String token);

    //지출 내역 모두 보내기
    List<IncomeResponseDTO> list(String token);
}
