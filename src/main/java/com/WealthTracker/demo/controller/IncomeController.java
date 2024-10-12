package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.service.income_expend.IncomeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> write(@RequestBody IncomeRequestDTO incomeRequestDTO) throws Exception {

        incomeService.writeIncome(incomeRequestDTO);
        return new ResponseEntity<>(new ReturnCodeDTO(200, SuccessCode.SUCCESS_INCOME.getMessage()),
                HttpStatusCode.valueOf(SuccessCode.SUCCESS_INCOME.getStatus()));
    }

}
