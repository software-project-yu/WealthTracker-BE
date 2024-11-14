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

    @PostMapping("/{targetId}/savings") //* 목표에 저축하는 API
    public ResponseEntity<Void> addDailySaving(@PathVariable Long targetId,
                                               @RequestBody DailySavingRequestDTO requestDTO,
                                               @RequestHeader("Authorization") String token) {
        targetService.addDailySaving(targetId, requestDTO, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{targetId}/achievement-rate") //* 목표 달성률 반환하는 API
    public ResponseEntity<Double> getAchievementRate(@PathVariable Long targetId,
                                                     @RequestHeader("Authorization") String token) {
        double achievementRate = targetService.getAchievementRate(targetId, token);
        return ResponseEntity.ok(achievementRate);
    }
}
