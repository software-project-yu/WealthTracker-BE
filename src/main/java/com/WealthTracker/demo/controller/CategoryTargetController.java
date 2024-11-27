package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.category_target.CategoryTargetRequestDTO;
import com.WealthTracker.demo.DTO.category_target.CategoryTargetResponseDTO;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.service.category_target.CategoryTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category-target")
public class CategoryTargetController {
    private final CategoryTargetService categoryTargetService;

    //* 카테고리 목표 생성하는 API
    @PostMapping("/create")
    public void createTarget(@RequestHeader("Authorization") String token,
                             @RequestBody CategoryTargetRequestDTO requestDTO) {
        categoryTargetService.createTarget(token, requestDTO);
    }

    //* 카테고리 목표 수정하는 API
    @PutMapping("/update")
    public void updateTarget(@RequestHeader("Authorization") String token,
                             @RequestBody CategoryTargetRequestDTO requestDTO) {
        categoryTargetService.updateTarget(token, requestDTO);
    }

    //* 해당 카테고리 목표를 조회하는 API
    @GetMapping("/{category}")
    public CategoryTargetResponseDTO getTarget(@RequestHeader("Authorization") String token,
                                               @PathVariable("category") Category_Expend category) {
        return categoryTargetService.getTarget(token, category);
    }
}
