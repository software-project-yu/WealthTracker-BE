package com.WealthTracker.demo.controller;

import com.WealthTracker.demo.service.FeedbackServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "피드백",description = "피드백 API")
public class FeedbackController {
    @Value("${GEMINI_KEY}")
    private String key;

    @Value("${GEMINI_URL}")
    private String url;

   private final FeedbackServiceImpl feedbackService;

    @GetMapping("/feedback")
    public String chat(@RequestHeader("Authorization") String token) {
        // key와 url을 서비스로 전달하여 사용
        return feedbackService.sendFeedBack(token, key, url);
    }
}
