package com.WealthTracker.demo.domain.feedback.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private List<Candidate> candidates;
    private PromptFeedback promptFeedback;

    @Getter
    @Setter
    public static class Candidate {
        private Content content;
        private String finishReason;
        private int index;
        private List<SafetyRating> safetyRatings;

    }

    @Getter @Setter
    @ToString
    public static class Content {
        private List<Parts> parts;
        private String role;

    }

    @Getter @Setter
    @ToString
    public static class Parts {
        private String text;

    }

    @Getter @Setter
    public static class SafetyRating {
        private String category;
        private String probability;
    }

    @Getter
    @Setter
    public static class PromptFeedback {
        private List<SafetyRating> safetyRatings;

    }
}