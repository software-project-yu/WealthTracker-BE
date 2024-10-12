package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.service.income_expend.IncomeServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "수입",description = "수입 API")
public class IncomeController {
    private final IncomeServiceImpl incomeService;

    @PostMapping("/income")
    public ResponseEntity<?> write(@RequestBody IncomeRequestDTO incomeRequestDTO, @RequestHeader("Authorization") String token) throws Exception {

        incomeService.writeIncome(incomeRequestDTO, token);

        //성공메시지 리턴
        return new ResponseEntity<>(new ReturnCodeDTO(200, SuccessCode.SUCCESS_INCOME.getMessage()),
                HttpStatusCode.valueOf(SuccessCode.SUCCESS_INCOME.getStatus()));
    }

    @GetMapping("/income/list")
    public ResponseEntity<List<IncomeResponseDTO>> list(@RequestHeader("Authorization") String token) throws Exception {
        return new ResponseEntity<>(incomeService.list(token), HttpStatusCode.valueOf(200));
    }

}
