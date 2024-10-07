package com.WealthTracker.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {

    private String token;

    private String type = "Bearer";

    public JwtResponseDTO(String token) {
        this.token = token;
    }
}
