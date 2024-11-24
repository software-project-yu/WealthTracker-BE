package com.WealthTracker.demo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    //200
    SUCCESS_EMAIL_SENT(200, "인증 이메일이 성공적으로 발송되었습니다."),
    EMAIL_VERIFIED(200, "이메일 인증이 완료되었습니다."),
    SUCCESS_INCOME(200, "수입을 성공적으로 기록했습니다."),
    SUCCESS_EXPEND(200, "지출을 성공적으로 기록했습니다."),
    SUCCESS_RESPOND_INCOME(200, "수입을 성공적으로 불러왔습니다."),
    SUCCESS_RESPOND_EXPEND(200, "지출을 성공적으로 불러왔습니다."),
    SUCCESS_SIGNUP(200, "회원가입을 성공했습니다."),
    SUCCESS_FEEDBACK(200,"피드백을 성공적으로 불러왔습니다."),
    SUCCESS_LOGIN(200, "로그인을 성공했습니다."),
    SUCCESS_EXPEND_INCOME(200,"지출과 수입 내역을 성공적으로 불러왔습니다."),
    SUCCESS_RESET_CODE_SENT(200, "비밀번호 재설정 코드가 발송되었습니다."),
    SUCCESS_PASSWORD_RESET(200, "비밀번호가 성공적으로 재설정되었습니다.");
    private final int status;
    private final String message;

}
