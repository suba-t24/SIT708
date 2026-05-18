package com.example.llmlearningassistantapp.network;

import java.util.List;
import java.util.Map;

public class HistoryResponse {
    private boolean success;
    private String message;
    private List<HistoryDto> history;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<HistoryDto> getHistory() {
        return history;
    }

    public static class HistoryDto {
        private String username;
        private String topic;
        private String taskTitle;
        private int totalQuestions;
        private int correctCount;
        private int incorrectCount;
        private long timestamp;
        private List<Map<String, String>> questions;

        public String getTopic() {
            return topic;
        }

        public String getTaskTitle() {
            return taskTitle;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public int getCorrectCount() {
            return correctCount;
        }

        public int getIncorrectCount() {
            return incorrectCount;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public List<Map<String, String>> getQuestions() {
            return questions;
        }
    }
}