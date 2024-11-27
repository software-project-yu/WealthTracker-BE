package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.DTO.UserProfileResponseDTO;
import com.WealthTracker.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/profile-update")
    public ResponseEntity<UserProfileResponseDTO> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileResponseDTO updateRequestDTO) {
        UserProfileResponseDTO updatedProfile = userService.updateProfile(token, updateRequestDTO);
        return ResponseEntity.ok(updatedProfile);
    }
}
