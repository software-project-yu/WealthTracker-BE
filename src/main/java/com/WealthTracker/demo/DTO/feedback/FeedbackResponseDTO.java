package com.WealthTracker.demo.DTO.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
    private String message;
    private String createdAt;
}
