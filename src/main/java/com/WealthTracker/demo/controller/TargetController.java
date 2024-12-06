package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.ReturnCodeDTO;
import com.WealthTracker.demo.DTO.dailysaving.DailySavingRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetGraphDTO;
import com.WealthTracker.demo.DTO.target.TargetRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetResponseDTO;
import com.WealthTracker.demo.service.target.TargetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/target")
@RequiredArgsConstructor
@Tag(name = "저축 목표", description = "< 목표 > API")
public class TargetController {

    private final TargetService targetService;

    //* 새로운 목표 생성하는 API
    @Operation(summary = "사용자의 저축 목표를 생성하는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/create")
    public ResponseEntity<TargetResponseDTO> createTarget(@RequestBody TargetRequestDTO requestDTO,
                                                          @RequestHeader("Authorization") String token) {
        TargetResponseDTO responseDTO = targetService.createTarget(requestDTO, token);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    //* 목표 수정하는 API
    @Operation(summary = "사용자의 저축 목표를 수정하는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PutMapping("/update/{targetId}")
    public ResponseEntity<TargetResponseDTO> updateTarget(@PathVariable Long targetId,
                                                          @RequestBody TargetRequestDTO requestDTO,
                                                          @RequestHeader("Authorization") String token) {
        targetService.updateTarget(targetId, requestDTO, token);
        return ResponseEntity.ok().build();
    }

    //* 목표 삭제하는 API
    @Operation(summary = "사용자의 저축 목표를 삭제하는 API 입니다. [담당자] : 박재성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @DeleteMapping("/delete/{targetId}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Long targetId,
                                             @RequestHeader("Authorization") String token) {
        targetService.deleteTarget(targetId, token);
        return ResponseEntity.noContent().build();
    }

    //* 목표에 저축하는 API
    @Operation(summary = "특정한 일자에 목표를 저축하는 API 입니다. [담당자] : 박재성"
            , description = "캘린더에 표시되는 한 일자의 목표 금액을 저축합니다!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저축 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @PostMapping("/savings/{targetId}")
    public ResponseEntity<Void> addDailySaving(@PathVariable Long targetId,
                                               @RequestBody DailySavingRequestDTO requestDTO,
                                               @RequestHeader("Authorization") String token) {
        targetService.addDailySaving(targetId, requestDTO, token);
        return ResponseEntity.ok().build();
    }

    //* 월별 저축 목표 그래프 API
    @Operation(summary = "그래프에 필요한 월별 저축 목표 데이터를 반환하는 API 입니다. [담당자] : 김도연")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReturnCodeDTO.class))}),
            @ApiResponse(responseCode = "409",description = "유저 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = {@Content(mediaType = "string")})
    })
    @GetMapping("/graph")
    public ResponseEntity<?> getGraphData(@RequestHeader("Authorization")String token,@RequestParam("month")int month){
        TargetGraphDTO targetGraphDTO=targetService.getGraphData(month, token);
        return new ResponseEntity<>(targetGraphDTO,HttpStatus.OK);
    }

}
