package com.WealthTracker.demo.domain.target.service;

import com.WealthTracker.demo.domain.target.dto.DailySavingRequestDTO;
import com.WealthTracker.demo.domain.target.dto.TargetGraphDTO;
import com.WealthTracker.demo.domain.target.dto.TargetRequestDTO;
import com.WealthTracker.demo.domain.target.dto.TargetResponseDTO;

public interface TargetService {

    TargetResponseDTO createTarget(TargetRequestDTO requestDTO, String token); //* 목표 생성하는 서비스

    void updateTarget(Long targetId, TargetRequestDTO requestDTO, String token); //* 목표 수정하는 서비스

    void deleteTarget(Long targetId, String token); //* 목표 삭제하는 서비스

    void addDailySaving(Long targetId, DailySavingRequestDTO requestDTO, String token); //* 저축한 금액 더하는 서비스

    TargetGraphDTO getGraphData(int month,String token);//메인 페이지 저축목표 그래프 데이터 리턴

}
