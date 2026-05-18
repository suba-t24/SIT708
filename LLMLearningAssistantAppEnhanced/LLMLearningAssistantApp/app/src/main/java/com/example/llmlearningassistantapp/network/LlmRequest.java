package com.example.llmlearningassistantapp.network;

public class LlmRequest {
    private String prompt;
    private String utilityType;

    public LlmRequest(String prompt, String utilityType) {
        this.prompt = prompt;
        this.utilityType = utilityType;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getUtilityType() {
        return utilityType;
    }
}