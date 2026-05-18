package com.example.llmlearningassistantapp.network;

import java.util.List;

public class ProfileResponse {
    private boolean success;
    private String message;
    private ProfileDto profile;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ProfileDto getProfile() {
        return profile;
    }

    public static class ProfileDto {
        private String username;
        private String email;
        private List<String> interests;
        private String upgradeTier;
        private int totalQuestions;
        private int correctAnswers;
        private int incorrectAnswers;

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public List<String> getInterests() {
            return interests;
        }

        public String getUpgradeTier() {
            return upgradeTier;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public int getIncorrectAnswers() {
            return incorrectAnswers;
        }
    }
}