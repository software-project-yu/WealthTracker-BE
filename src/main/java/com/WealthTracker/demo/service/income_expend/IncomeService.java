package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;

public interface IncomeService {
    Long writeIncome(IncomeRequestDTO incomeRequestDTO);
}
