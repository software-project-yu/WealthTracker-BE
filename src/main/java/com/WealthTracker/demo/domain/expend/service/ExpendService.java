package com.WealthTracker.demo.domain.expend.service;

import com.WealthTracker.demo.domain.expend.dto.*;

import java.util.List;

public interface ExpendService {
    //지출내역 쓰기
    Long writeExpend(ExpendRequestDTO expendRequestDTO, String token);

    //지출 내역 모두 보기
    List<ExpendResponseDTO> expendList(String token, int month);

    //지출 내역 상세보기
    ExpendResponseDTO  expendResponseDetail(String token,Long expendId);


    //지출 내역 수정
    Long updateExpend(String token,Long expendId,ExpendRequestDTO expendRequestDTO);
    //지출 내역 삭제
    Long deleteExpend(String token,Long expendId);
    //최근 지출 내역 불러오기-5개
    List<ExpendResponseDTO> getRecentExpend(String token);
    //그래프를 위한 같은 주 저번달, 이번 달 지출 총액 불러오기
    List<ExpendDateResponseDTO> getAmountByWeek(String token);

    //이번 비용 내역
    List<ExpendCategoryAmountDTO> getAmountByMonth(String token);

    //일별 지출 총합-2주
    List<ExpendDayResponseDTO> getAmountByDate(String token);

}
