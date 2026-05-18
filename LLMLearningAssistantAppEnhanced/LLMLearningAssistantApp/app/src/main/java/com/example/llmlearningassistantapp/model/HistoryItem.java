package com.example.llmlearningassistantapp.model;

public class HistoryItem {
    private String question;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean correct;

    public HistoryItem(String question, String selectedAnswer, String correctAnswer, boolean correct) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}