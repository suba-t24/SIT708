package com.example.llmlearningassistantapp.network;

public class AuthResponse {
    private boolean success;
    private String message;
    private UserDto user;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserDto getUser() {
        return user;
    }

    public static class UserDto {
        private String username;
        private String email;
        private String upgradeTier;

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getUpgradeTier() {
            return upgradeTier;
        }
    }
}