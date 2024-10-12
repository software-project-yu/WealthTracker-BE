package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.service.income_expend.IncomeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IncomeController {
    private final IncomeServiceImpl incomeService;

    @PostMapping("/income")
    public String write(@RequestBody IncomeRequestDTO incomeRequestDTO){
        try {
            incomeService.writeIncome(incomeRequestDTO);
            return "수입내역 기록 성공";
        }catch (Exception e){
            return "수입내역 기록 실패"+e.getMessage();
        }
    }

}
