package com.example.llmlearningassistantapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class HistoryManager {

    private static final String PREF_NAME = "QUIZ_HISTORY_PREFS";
    private static final String KEY_HISTORY = "history_text";
    private static final String KEY_TOTAL = "total_questions";
    private static final String KEY_CORRECT = "correct_answers";
    private static final String KEY_INCORRECT = "incorrect_answers";
    private static final String KEY_ACCOUNT_TYPE = "account_type";

    private final SharedPreferences prefs;

    public HistoryManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveAttempt(String question, String selectedAnswer, String correctAnswer, boolean isCorrect) {
        String oldHistory = prefs.getString(KEY_HISTORY, "");

        String newEntry =
                "Question: " + question + "\n" +
                        "Selected Answer: " + selectedAnswer + "\n" +
                        "Correct Answer: " + correctAnswer + "\n" +
                        "Result: " + (isCorrect ? "Correct" : "Incorrect") + "\n\n";

        prefs.edit()
                .putString(KEY_HISTORY, newEntry + oldHistory)
                .putInt(KEY_TOTAL, getTotalQuestions() + 1)
                .putInt(KEY_CORRECT, getCorrectAnswers() + (isCorrect ? 1 : 0))
                .putInt(KEY_INCORRECT, getIncorrectAnswers() + (isCorrect ? 0 : 1))
                .apply();
    }

    public String getHistoryText() {
        return prefs.getString(KEY_HISTORY, "No history available yet.");
    }

    public int getTotalQuestions() {
        return prefs.getInt(KEY_TOTAL, 0);
    }

    public int getCorrectAnswers() {
        return prefs.getInt(KEY_CORRECT, 0);
    }

    public int getIncorrectAnswers() {
        return prefs.getInt(KEY_INCORRECT, 0);
    }

    public void saveAccountType(String type) {
        prefs.edit().putString(KEY_ACCOUNT_TYPE, type).apply();
    }

    public String getAccountType() {
        return prefs.getString(KEY_ACCOUNT_TYPE, "Starter");
    }
}