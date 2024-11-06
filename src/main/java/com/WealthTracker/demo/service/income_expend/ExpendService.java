package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendCategoryAmountDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendDateResponseDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendResponseDTO;
import com.WealthTracker.demo.domain.Expend;

import java.util.List;

public interface ExpendService {
    //지출내역 쓰기
    Long writeExpend(ExpendRequestDTO expendRequestDTO, String token);

    //지출 내역 모두 보기
    List<ExpendResponseDTO> expendList(String token);

    //지출 내역 상세보기
    ExpendResponseDTO  expendResponseDetail(String token,Long expendId);

    //카테고리별 지출 내역 불러오기
    List<ExpendCategoryAmountDTO> expendCategoryResponse(String token,String category);
    //지출 내역 수정
    Long updateExpend(String token,Long expendId,ExpendRequestDTO expendRequestDTO);
    //지출 내역 삭제
    Long deleteExpend(String token,Long expendId);
    //최근 지출 내역 불러오기-5개
    List<ExpendResponseDTO> getRecentExpend(String token);
    //그래프를 위한 같은 주 저번달, 이번 달 지출 총액 불러오기
    List<ExpendDateResponseDTO> getAmountByWeek(String token);

}
