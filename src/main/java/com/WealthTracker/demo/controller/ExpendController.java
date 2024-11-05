package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendDateResponseDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendResponseDTO;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.service.income_expend.ExpendServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "지출",description = "지출 API")
public class ExpendController {
    private final ExpendServiceImpl expendService;

    @Operation(summary = "지출 내역 기록 작성 API입니다. [담당자]:김도연", description = "카테고리 입력시 띄어쓰기 없이 정확한 값 입력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기록 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/expend")
    public ResponseEntity<?> write(@RequestBody ExpendRequestDTO expendResponseDTO, @RequestHeader("Authorization") String token) throws CustomException {
        expendService.writeExpend(expendResponseDTO, token);

        return new ResponseEntity<>(new ReturnCodeDTO(200, SuccessCode.SUCCESS_EXPEND.getMessage()),
                HttpStatusCode.valueOf(SuccessCode.SUCCESS_EXPEND.getStatus()));
    }

    @Operation(summary = "모든 지출 내역 조회 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema (schema =@Schema (implementation = ExpendResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/expend/list")
    public ResponseEntity<List<ExpendResponseDTO>> list(@RequestHeader("Authorization") String token) throws CustomException {
        return new ResponseEntity<>(expendService.expendList(token), HttpStatusCode.valueOf(SuccessCode.SUCCESS_RESPOND_EXPEND.getStatus()));
    }

    @Operation(summary = "지출 내역 최근 5개 조회 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema (schema =@Schema (implementation = ExpendResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/expend/recent")
    public ResponseEntity<List<ExpendResponseDTO>> recentExpend(@RequestHeader("Authorization") String token) throws CustomException{
        return new ResponseEntity<>(expendService.getRecentExpend(token),HttpStatusCode.valueOf(SuccessCode.SUCCESS_EXPEND.getStatus()));
    }

    @Operation(summary = "지출 내역 월별 그래프를 위한 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema (schema =@Schema (implementation = ExpendDateResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/expend/graph")
    public ResponseEntity<List<ExpendDateResponseDTO>> amountByWeek(@RequestHeader("Authorization") String token){
        return new ResponseEntity<>(expendService.getAmountByWeek(token),HttpStatusCode.valueOf(SuccessCode.SUCCESS_RESPOND_EXPEND.getStatus()));
    }

    @Operation(summary = "지출 내역 그래프를 위한 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/expend/{expendId}")
    public ResponseEntity<?> detailExpend(@RequestHeader("Authorization") String token, @PathVariable Long expendId){
        try {
            return new ResponseEntity<>(expendService.expendResponseDetail(token,expendId),HttpStatusCode.valueOf(SuccessCode.SUCCESS_EXPEND.getStatus()));
        }catch (CustomException ex){
            //사용자의 지출내역이 아닌 경우 예외처리
            return new ResponseEntity<>(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
