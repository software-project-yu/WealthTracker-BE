package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendResponseDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.service.income_expend.IncomeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "수입 내역 작성 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/income")
    public ResponseEntity<?> write(@RequestBody IncomeRequestDTO incomeRequestDTO, @RequestHeader("Authorization") String token) throws CustomException {

        incomeService.writeIncome(incomeRequestDTO, token);

        //성공메시지 리턴
        return new ResponseEntity<>(new ReturnCodeDTO(200, SuccessCode.SUCCESS_INCOME.getMessage()),
                HttpStatusCode.valueOf(SuccessCode.SUCCESS_INCOME.getStatus()));
    }

    @Operation(summary = "모든 수입 내역 조회 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema (schema =@Schema (implementation = IncomeResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/income/list")
    public ResponseEntity<List<IncomeResponseDTO>> list(@RequestHeader("Authorization") String token,@RequestParam("month")int month) throws CustomException {
        return new ResponseEntity<>(incomeService.incomeList(token,month), HttpStatusCode.valueOf(SuccessCode.SUCCESS_RESPOND_EXPEND.getStatus()));
    }

    @Operation(summary = "수입 내역 최근 5개 조회 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema (schema =@Schema (implementation = IncomeResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/income/recent")
    public ResponseEntity<List<IncomeResponseDTO>> recentIncome(@RequestHeader("Authorization") String token) throws CustomException{
        return new ResponseEntity<>(incomeService.getRecentIncome(token),HttpStatusCode.valueOf(SuccessCode.SUCCESS_EXPEND.getStatus()));
    }

    @Operation(summary = "수입 내역 상세보기를 위한 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/income/{incomeId}")
    public ResponseEntity<?> detailIncome(@RequestHeader("Authorization") String token, @PathVariable Long incomeId){
        try {
            return new ResponseEntity<>(incomeService.incomeResponseDetail(token,incomeId),HttpStatusCode.valueOf(SuccessCode.SUCCESS_EXPEND.getStatus()));
        }catch (CustomException ex){
            //사용자의 지출내역이 아닌 경우 예외처리
            return new ResponseEntity<>(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "수입 내역 삭제를 위한 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @DeleteMapping("/income/delete/{incomeId}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable Long incomeId){
        try {
            // 지출 내역 삭제 메서드 호출
            incomeService.deleteIncome(token, incomeId);
            // 성공 메시지 반환
            ReturnCodeDTO returnCodeDTO = ReturnCodeDTO.builder()
                    .status(200)
                    .message("삭제 성공")
                    .build();
            return new ResponseEntity<>(returnCodeDTO, HttpStatus.OK);
        }catch (CustomException ex){
            return new ResponseEntity<>(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
        //지출 내역이 존재하지 않을 때
        catch (Exception ex){
            // 일반 예외 처리
            ReturnCodeDTO returnCodeDTO = new ReturnCodeDTO(ErrorCode.EXPEND_NOT_FOUND.getStatus(),
                    ErrorCode.EXPEND_NOT_FOUND.getMessage());
            return new ResponseEntity<>(returnCodeDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "수입 내역 수정을 위한 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/income/update/{incomeId}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token,@PathVariable Long incomeId,@RequestBody IncomeRequestDTO incomeRequestDTO){
        try {
            // 지출 내역 수정 메서드 호출
            incomeService.updateIncome(token,incomeId,incomeRequestDTO);
            // 성공 메시지 반환
            ReturnCodeDTO returnCodeDTO = ReturnCodeDTO.builder()
                    .status(200)
                    .message("수정 성공")
                    .build();
            return new ResponseEntity<>(returnCodeDTO, HttpStatus.OK);
        }catch (CustomException ex){
            return new ResponseEntity<>(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}
