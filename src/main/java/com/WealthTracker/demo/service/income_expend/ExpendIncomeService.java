package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendIncomeResponseDTO;

import java.util.List;

public interface ExpendIncomeService {
    //지출 수입 월별 총 리스트 반환
    List<ExpendIncomeResponseDTO> getExpendIncomeAllList(String token,int month);
}
