package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;

import java.util.List;

public interface IncomeService {
    Long writeIncome(IncomeRequestDTO incomeRequestDTO,String token);

    //수입 내역 모두 보내기
    List<IncomeResponseDTO> incomeList(String token,int month);

    //수입 내역 상세보기
    IncomeResponseDTO incomeResponseDetail(String token,Long incomeId);

    //수입 내역 수정
    Long updateIncome(String token,Long incomeId,IncomeRequestDTO incomeRequestDTO);

    //수입 내역 삭제
    Long deleteIncome(String token,Long incomeId);

    //최근 수입 내역 불러오기-5개
    List<IncomeResponseDTO> getRecentIncome(String token);
}
