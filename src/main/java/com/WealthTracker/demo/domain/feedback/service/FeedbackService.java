package com.WealthTracker.demo.domain.feedback.service;

import com.WealthTracker.demo.domain.user.entity.User;

public interface FeedbackService {
    //보낼 메세지 만들고 저장
    String generateMessage(User user);

    String sendFeedBack(String token,String key,String url);
}
