package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.PaymentRequestDTO;
import com.WealthTracker.demo.DTO.PaymentResponseDTO;
import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendCategoryAmountDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.service.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "결제", description = "결제 내역 API")

public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @Operation(summary = "결제 내역 기록 API 입니다.", description = "결제 내역을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기록 성공", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content
                    = {@Content(mediaType = "string")})
    })
    @PostMapping("/payment")
    public ResponseEntity<?> write(@RequestBody PaymentRequestDTO paymentRequestDTO,
                                   @RequestHeader("Authorization") String token, BindingResult bindingResult) throws CustomException {
        // 유효성 검사
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        paymentService.writePayment(paymentRequestDTO, token);

        return new ResponseEntity<>(new ReturnCodeDTO(200, SuccessCode.SUCCESS_PAYMENT.getMessage()),
                HttpStatusCode.valueOf(SuccessCode.SUCCESS_PAYMENT.getStatus()));
    }


    @Operation(summary = "결제 내역 조회 API 입니다.", description = "결제 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content =
                    {@Content(mediaType = "application/json", array = @ArraySchema (schema =@Schema
                            (implementation = PaymentResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content =
                    {@Content(mediaType = "string")})
    })
    @GetMapping("/payment/list")
    public ResponseEntity<List<PaymentResponseDTO>> list(@RequestHeader("Authorization")
                                                         String token) throws CustomException {
        // 결제 내역 조회
        List<PaymentResponseDTO> paymentResponseDTOList = paymentService.listPayments(token);
        // 조회 성공 시 결제 내역 리스트 반환
        return ResponseEntity.status(200).body(paymentResponseDTOList);
    }

    @Operation(summary = "결제 내역 삭제 API 입니다.", description = "결제 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content =
                    {@Content(mediaType = "application/json", schema = @Schema
                            (implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content =
                    {@Content(mediaType = "string")})
    })
    @DeleteMapping("/payment/delete/{paymentId}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable Long paymentId){
        try {
            // 결제 내역 삭제 메서드 호출
            paymentService.deletePayment(token, paymentId);
            // 성공 메시지 반환
            ReturnCodeDTO returnCodeDTO = ReturnCodeDTO.builder()
                    .status(200)
                    .message("삭제 성공")
                    .build();
            return new ResponseEntity<>(returnCodeDTO, HttpStatus.OK);
        }catch (CustomException ex){
            return new ResponseEntity<>(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "결제 내역 수정을 위한 API 입니다.",  description = "결제 내역을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content =
                    {@Content(mediaType = "application/json", schema = @Schema
                            (implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/payment/update/{paymentId}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @PathVariable Long paymentId, @RequestBody PaymentRequestDTO paymentRequestDTO, BindingResult bindingResult){
        // 유효성 검사
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            // 결제 내역 수정 메서드 호출
            paymentService.updatePayment(token,paymentId,paymentRequestDTO);
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

    @Operation(summary = "결제 내역 최근 2개 조회 API 입니다.", description = "최근 2개의 결제 내역을 조회합니다.")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description = "조회 성공", content =
                    {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PaymentResponseDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/payment/recent")
    public ResponseEntity<List<PaymentResponseDTO>> recentPayments(@RequestHeader("Authorization")String token) throws CustomException{
        // 결제 내역 중 최근 2개 조회
        List<PaymentResponseDTO> recentPayments = paymentService.getRecentPayments(token);
        // 조회된 결제 내역 리스트 반환
        return new ResponseEntity<>(recentPayments, HttpStatus.OK);
    }

    @Operation(summary = "결제 내역 리스트(결제 내역 페이지)를 위한 API입니다.", description = "결제 내역 리스트입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema (schema =@Schema (implementation = ExpendCategoryAmountDTO.class)))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/payment/paymentList")
    public ResponseEntity<?> listWithPaymentPage(@RequestHeader("Authorization") String token){
        try {
            // 성공 메시지 반환
            return new ResponseEntity<>( paymentService.getAmountByMonth(token),HttpStatusCode.valueOf(SuccessCode.SUCCESS_RESPOND_PAYMENT.getStatus()));
        }catch (CustomException ex){
            return new ResponseEntity<>(new ReturnCodeDTO(ex.getErrorCode().getStatus(),ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}
