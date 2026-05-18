package com.example.llmlearningassistantapp.network;

import java.util.List;
import java.util.Map;

public class SaveQuizResultRequest {
    private String username;
    private String topic;
    private String taskTitle;
    private int totalQuestions;
    private int correctCount;
    private int incorrectCount;
    private long timestamp;
    private List<Map<String, String>> questions;

    public SaveQuizResultRequest(
            String username,
            String topic,
            String taskTitle,
            int totalQuestions,
            int correctCount,
            int incorrectCount,
            long timestamp,
            List<Map<String, String>> questions
    ) {
        this.username = username;
        this.topic = topic;
        this.taskTitle = taskTitle;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
        this.timestamp = timestamp;
        this.questions = questions;
    }
}