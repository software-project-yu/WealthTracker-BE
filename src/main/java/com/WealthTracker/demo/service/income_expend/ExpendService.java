package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.domain.Expend;

import java.util.List;

public interface ExpendService {
    //지출내역 쓰기
    public Long writeExpend(ExpendRequestDTO expendRequestDTO);
}
