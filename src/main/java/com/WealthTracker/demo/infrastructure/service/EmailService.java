package com.WealthTracker.demo.infrastructure.service;

public interface EmailService {
    void sendEmailVerification(String to, String code);
    void sendPasswordReset(String to, String code);
}
