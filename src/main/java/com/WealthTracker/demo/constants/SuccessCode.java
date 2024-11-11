package com.WealthTracker.demo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    //200
    SUCCESS_INCOME(200, "수입을 성공적으로 기록했습니다."),
    SUCCESS_EXPEND(200, "지출을 성공적으로 기록했습니다."),
    SUCCESS_RESPOND_INCOME(200, "수입을 성공적으로 불러왔습니다."),
    SUCCESS_RESPOND_EXPEND(200, "지출을 성공적으로 불러왔습니다."),
    SUCCESS_SIGNUP(200, "회원가입을 성공했습니다."),
    SUCCESS_FEEDBACK(200,"피드백을 성공적으로 불러왔습니다."),
    SUCCESS_LOGIN(200, "로그인을 성공했습니다.");
    private final int status;
    private final String message;

}
