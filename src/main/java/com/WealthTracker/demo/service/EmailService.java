package com.WealthTracker.demo.service;

public interface EmailService {
    void sendEmailVerification(String to, String code);
    void sendPasswordReset(String to, String code);
}
