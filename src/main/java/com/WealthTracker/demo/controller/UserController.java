package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.UserProfileResponseDTO;
import com.WealthTracker.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getProfile(
            @RequestHeader("Authorization") String token) {
        UserProfileResponseDTO profile = userService.getProfile(token);
        return ResponseEntity.ok(profile);
    }
}
