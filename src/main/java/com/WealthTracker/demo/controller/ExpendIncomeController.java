package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendDayResponseDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendIncomeResponseDTO;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.service.income_expend.ExpendIncomeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "지출 및 수입",description = "지출 및 수입 API")
public class ExpendIncomeController {
    private final ExpendIncomeServiceImpl expendIncomeService;
    @Operation(summary = "지출과 수입내역 리스트 모두를 위한 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema =@Schema(implementation = ExpendIncomeResponseDTO.class)))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/expend-income")
    public ResponseEntity<?> allListByMonth(@RequestHeader("Authorization") String token, @RequestParam("month")int month){
        try {
            List<ExpendIncomeResponseDTO> response=expendIncomeService.getExpendIncomeAllList(token,month);
            // 성공 메시지 반환
            return ResponseEntity.ok(response);
        }catch (CustomException ex){
            return new ResponseEntity<>(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
