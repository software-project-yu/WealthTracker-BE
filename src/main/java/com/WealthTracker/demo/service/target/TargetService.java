package com.WealthTracker.demo.service.target;

import com.WealthTracker.demo.DTO.dailysaving.DailySavingRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetResponseDTO;

public interface TargetService {

    TargetResponseDTO createTarget(TargetRequestDTO requestDTO, String token); //* 목표 생성하는 서비스

    void updateTarget(Long targetId, TargetRequestDTO requestDTO, String token); //* 목표 수정하는 서비스

    void deleteTarget(Long targetId, String token); //* 목표 삭제하는 서비스

    void addDailySaving(Long targetId, DailySavingRequestDTO requestDTO, String token); //* 저축한 금액 더하는 서비스

}
