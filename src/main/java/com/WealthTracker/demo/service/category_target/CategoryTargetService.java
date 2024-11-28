package com.WealthTracker.demo.service.category_target;

import com.WealthTracker.demo.DTO.category_target.CategoryTargetRequestDTO;
import com.WealthTracker.demo.DTO.category_target.CategoryTargetResponseDTO;
import com.WealthTracker.demo.enums.Category_Expend;

public interface CategoryTargetService {

    void createTarget(String token, CategoryTargetRequestDTO requestDTO); // 카테고리 별 지출 목표 금액 생성

    void updateTarget(String token, CategoryTargetRequestDTO requestDTO); // 카테고리 별 지출 목표 금액 업데이트

    CategoryTargetResponseDTO getTarget(String token, Category_Expend category);
}
