package com.WealthTracker.demo.DTO;

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
    @Pattern(regexp = "^[A-Za-z0-9_\\\\.\\\\-]+@[A-Za-z0-9\\\\-]+\\\\.[A-Za-z0-9\\\\-]+$", message = "알맞은 이메일 형식을 입력.")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$\n")
    private String password;

    @NotNull
    @Size(min = 1,max = 10,message = "1자 이상 10자 이하의 이름을 입력.")
    private String name;

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]{6,15}$\n")
    private String nickName;
}