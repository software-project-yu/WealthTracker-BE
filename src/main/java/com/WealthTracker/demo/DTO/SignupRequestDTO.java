package com.WealthTracker.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {

    @NotNull
    @Size(min = 1, max = 50)
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{8,15}$")
    //영어 + 숫자 + 특수문자 8~15자
    private String password;

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    //6-15자 영어 숫자
    @Pattern(regexp = "^[a-zA-Z0-9]{6,15}$")
    private String nickName;
}
