package com.example.llmlearningassistantapp.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private final String questionText;
    private final List<String> options;
    private final int correctIndex;

    public Question(String questionText, List<String> options, int correctIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}
