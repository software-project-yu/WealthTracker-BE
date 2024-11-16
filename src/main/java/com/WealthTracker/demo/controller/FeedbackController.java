package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.service.FeedbackServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "피드백", description = "피드백 API")

public class FeedbackController {
    @Value("${GEMINI_KEY}")
    private String key;

    @Value("${GEMINI_URL}")
    private String url;

    private final FeedbackServiceImpl feedbackService;

    @Operation(summary = "지출 내역 피드백을 위한 API입니다. [담당자]:김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드백 반환 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/feedback")
    public ResponseEntity<?> chat(@RequestHeader("Authorization") String token) {
        // key와 url을 서비스로 전달하여 사용
        try {
            String feedbackMsg = feedbackService.sendFeedBack(token, key, url);
            return new ResponseEntity<>(new ReturnCodeDTO(SuccessCode.SUCCESS_FEEDBACK.getStatus(), feedbackMsg), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ReturnCodeDTO(ErrorCode.TOOMANY_REQUEST.getStatus(),e.getMessage()),HttpStatus.BAD_GATEWAY);
        }
    }
}
