package com.WealthTracker.demo.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VerificationCodeUtil {

    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6자리 숫자 생성
    }
}
