package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.category_target.CategoryTargetRequestDTO;
import com.WealthTracker.demo.DTO.category_target.CategoryTargetResponseDTO;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.service.category_target.CategoryTargetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category-target")
@Tag(name = "카테고리별 지출 목표", description = "< 카테고리 지출 목표 > API")
public class CategoryTargetController {
    private final CategoryTargetService categoryTargetService;

    //* 카테고리 목표 생성하는 API
    @Operation(summary = "카테고리별 지출 목표를 생성하는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/create")
    public void createTarget(@RequestHeader("Authorization") String token,
                             @RequestBody CategoryTargetRequestDTO requestDTO) {
        categoryTargetService.createTarget(token, requestDTO);
    }

    //* 카테고리 목표 수정하는 API
    @Operation(summary = "카테고리별 지출 목표를 수정하는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/update")
    public void updateTarget(@RequestHeader("Authorization") String token,
                             @RequestBody CategoryTargetRequestDTO requestDTO) {
        categoryTargetService.updateTarget(token, requestDTO);
    }

    //* 해당 카테고리 목표를 조회하는 API
    @Operation(summary = "특정 한 카테고리의 지출 목표를 조회하는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/{category}")
    public CategoryTargetResponseDTO getTarget(@RequestHeader("Authorization") String token,
                                               @PathVariable("category") Category_Expend category) {
        return categoryTargetService.getTarget(token, category);
    }
}
