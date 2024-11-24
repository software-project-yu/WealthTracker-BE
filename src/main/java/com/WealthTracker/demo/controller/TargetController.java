package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.dailysaving.DailySavingRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetResponseDTO;
import com.WealthTracker.demo.service.target.TargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/target")
@RequiredArgsConstructor
public class TargetController {

    private final TargetService targetService;

    @PostMapping("/create") //* 새로운 목표 생성하는 API
    public ResponseEntity<TargetResponseDTO> createTarget(@RequestBody TargetRequestDTO requestDTO,
                                                          @RequestHeader("Authorization") String token) {
        TargetResponseDTO responseDTO = targetService.createTarget(requestDTO, token);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{targetId}") //* 목표 수정하는 API
    public ResponseEntity<TargetResponseDTO> updateTarget(@PathVariable Long targetId,
                                                          @RequestBody TargetRequestDTO requestDTO,
                                                          @RequestHeader("Authorization") String token) {
        targetService.updateTarget(targetId, requestDTO, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{targetId}") //* 목표 삭제하는 API
    public ResponseEntity<Void> deleteTarget(@PathVariable Long targetId,
                                             @RequestHeader("Authorization") String token) {
        targetService.deleteTarget(targetId, token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/savings/{targetId}") //* 목표에 저축하는 API
    public ResponseEntity<Void> addDailySaving(@PathVariable Long targetId,
                                               @RequestBody DailySavingRequestDTO requestDTO,
                                               @RequestHeader("Authorization") String token) {
        targetService.addDailySaving(targetId, requestDTO, token);
        return ResponseEntity.ok().build();
    }

}
