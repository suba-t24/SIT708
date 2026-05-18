package com.example.llmlearningassistantapp.network;

import java.util.List;

public class SaveInterestsRequest {
    private String username;
    private List<String> interests;

    public SaveInterestsRequest(String username, List<String> interests) {
        this.username = username;
        this.interests = interests;
    }
}