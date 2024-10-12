package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.service.income_expend.ExpendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExpendController {
    private final ExpendServiceImpl expendService;

    @PostMapping("/expend")
    public String write(@RequestBody ExpendRequestDTO expendResponseDTO){
        try {
            expendService.writeExpend(expendResponseDTO);
            return "지출내역 기록 성공";
        }catch (Exception e){
            return "지출내역 기록 실패"+e.getMessage();
        }
    }
}
