package com.WealthTracker.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailVerification(String to, String code) {
        String subject = "이메일 인증을 완료해주세요";
        String verificationUrl = "회원님의 인증번호는 : [" + code + "] 입니다.";
        String message = "안녕하세요!\n\nWealthTracker 이메일 인증코드를 입력해 주세요.\n\n" + verificationUrl + "\n\n감사합니다.";

        sendSimpleMessage(to, subject, message);
    }

    @Override
    public void sendPasswordReset(String to, String code) {
        String subject = "비밀번호 재설정 요청";
        String message = "안녕하세요!\n\n아래 6자리 인증 코드를 입력하여 이메일 인증을 완료해주세요.\n\n" + code + "\n\n감사합니다.";
        sendSimpleMessage(to, subject, message);
    }

    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("timer973@naver.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }
}
