package com.WealthTracker.demo.domain.expend_income.service;

import com.WealthTracker.demo.domain.expend_income.dto.ExpendIncomeResponseDTO;

import java.util.List;

public interface ExpendIncomeService {
    //지출 수입 월별 총 리스트 반환
    List<ExpendIncomeResponseDTO> getExpendIncomeAllList(String token,int month);
}
