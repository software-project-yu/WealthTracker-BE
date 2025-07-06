package com.WealthTracker.demo.domain.user.entity;

import com.WealthTracker.demo.domain.feedback.entity.FeedBack;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    private String nickName;

    private String profile; // 프로필 사진 URL 또는 경로

    private boolean enabled = false; // 이메일 인증 여부

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedbackId")
    private FeedBack feedBack;

    // 비밀번호 변경 메서드
    public void setPassword(String password) {
        this.password = password;
    }

    //유저의 피드백 저장 설정 메서드
    public void setFeedBack(FeedBack feedBack){
        this.feedBack=feedBack;
    }
}
