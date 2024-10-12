//package com.WealthTracker.demo.controller;
//
//import com.WealthTracker.demo.DTO.KakaoLoginResponseDTO;
//import com.WealthTracker.demo.service.KakaoService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/users")
//public class KakaoUserController {
//
//    private final KakaoService kakaoService;
//
//    @ResponseBody
//    @GetMapping("/login/oauth/kakao") //** 카카오 로그인 API
//    public ResponseEntity<KakaoLoginResponseDTO> kakaoLogin(@RequestParam String code, HttpServletRequest request){
//        String currentDomain = request.getServerName(); // 환경에 따른 redirectUri 결정
//        KakaoLoginResponseDTO response = kakaoService.kakaoLogin(code, currentDomain);
//        return ResponseEntity.ok(response);
//
//    }
//}
