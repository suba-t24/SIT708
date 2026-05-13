package com.example.llmchatbotapp;

public class ChatMessage {
    private int id;
    private String username;
    private String message;
    private String sender;
    private String timestamp;

    public ChatMessage(int id, String username, String message, String sender, String timestamp) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isUser() {
        return sender.equals("user");
    }
}