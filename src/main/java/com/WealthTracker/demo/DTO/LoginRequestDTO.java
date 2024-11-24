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
public class LoginRequestDTO {

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9_\\\\.\\\\-]+@[A-Za-z0-9\\\\-]+\\\\.[A-Za-z0-9\\\\-]+$",message = "알맞은 이메일 형식을 입력.")
    private String email;

    @NotNull
    private String password;
}
