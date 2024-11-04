package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendResponseDTO;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.service.income_expend.ExpendServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "지출",description = "지출 API")
public class ExpendController {
    private final ExpendServiceImpl expendService;

    @Operation(summary = "지출 내역 기록 API입니다. [담당자]:김도연", description = "카테고리 입력시 띄어쓰기 없이 정확한 값 입력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/expend")
    public ResponseEntity<?> write(@RequestBody ExpendRequestDTO expendResponseDTO, @RequestHeader("Authorization") String token) throws CustomException {
        expendService.writeExpend(expendResponseDTO, token);

        return new ResponseEntity<>(new ReturnCodeDTO(200, SuccessCode.SUCCESS_EXPEND.getMessage()),
                HttpStatusCode.valueOf(SuccessCode.SUCCESS_EXPEND.getStatus()));
    }

    @Operation(summary = "지출 내역 조회 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema (schema =@Schema (implementation = ExpendResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/expend/list")
    public ResponseEntity<List<ExpendResponseDTO>> list(@RequestHeader("Authorization") String token) throws CustomException {
        return new ResponseEntity<>(expendService.list(token), HttpStatusCode.valueOf(SuccessCode.SUCCESS_RESPOND_EXPEND.getStatus()));
    }
}
