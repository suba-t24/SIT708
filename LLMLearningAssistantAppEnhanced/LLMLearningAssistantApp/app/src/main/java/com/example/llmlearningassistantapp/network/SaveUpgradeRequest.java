package com.example.llmlearningassistantapp.network;

public class SaveUpgradeRequest {
    private String username;
    private String tier;

    public SaveUpgradeRequest(String username, String tier) {
        this.username = username;
        this.tier = tier;
    }
}