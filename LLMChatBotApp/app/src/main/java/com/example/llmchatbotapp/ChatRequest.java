package com.example.llmchatbotapp;

public class ChatRequest {
    private String username;
    private String message;

    public ChatRequest(String username, String message) {
        this.username = username;
        this.message = message;
    }
}
