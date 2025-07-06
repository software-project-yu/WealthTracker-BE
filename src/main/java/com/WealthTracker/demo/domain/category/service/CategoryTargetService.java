package com.WealthTracker.demo.domain.category.service;

import com.WealthTracker.demo.domain.category.dto.CategoryTargetRequestDTO;
import com.WealthTracker.demo.domain.category.dto.CategoryTargetResponseDTO;
import com.WealthTracker.demo.domain.category.enums.Category_Expend;

public interface CategoryTargetService {

    void createTarget(String token, CategoryTargetRequestDTO requestDTO); // 카테고리 별 지출 목표 금액 생성

    void updateTarget(String token, CategoryTargetRequestDTO requestDTO); // 카테고리 별 지출 목표 금액 업데이트

    CategoryTargetResponseDTO getTarget(String token, Category_Expend category); // 카테고리 별 지출 목표 조회
}
