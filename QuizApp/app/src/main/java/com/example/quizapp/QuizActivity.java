package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private TextView tvQuestionNumber;
    private TextView tvQuestion;
    private TextView tvProgressText;

    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private Switch switchTheme;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int selectedAnswerIndex = -1;
    private int score = 0;
    private boolean isSubmitted = false;
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initializeViews();
        clearButtonTints();
        setupThemeToggle();
        loadQuestions();
        setOptionClickListeners();
        setSubmitButtonListener();
        displayQuestion();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvProgressText = findViewById(R.id.tvProgressText);

        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnSubmit = findViewById(R.id.btnSubmit);

        progressBar = findViewById(R.id.progressBar);
        switchTheme = findViewById(R.id.switchTheme);

        userName = getIntent().getStringExtra("user_name");
        if (userName == null) {
            userName = "";
        }

        tvWelcome.setText("Welcome, " + userName + "!");
    }

    private void clearButtonTints() {
        ViewCompat.setBackgroundTintList(btnOption1, null);
        ViewCompat.setBackgroundTintList(btnOption2, null);
        ViewCompat.setBackgroundTintList(btnOption3, null);
        ViewCompat.setBackgroundTintList(btnOption4, null);
        ViewCompat.setBackgroundTintList(btnSubmit, null);
    }

    private void setupThemeToggle() {
        SharedPreferences prefs = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        switchTheme.setChecked(isDarkMode);

        switchTheme.setOnCheckedChangeListener((buttonView, checked) -> {
            prefs.edit().putBoolean("dark_mode", checked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate();
        });
    }

    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();

        questionList.add(new Question(
                "What is Android primarily used for?",
                new String[]{"Web server management", "Mobile app development", "Database design", "Network cabling"},
                1
        ));

        questionList.add(new Question(
                "Which file is mainly used to design Android UI layouts?",
                new String[]{"Java file", "Gradle file", "XML file", "Manifest file"},
                2
        ));

        questionList.add(new Question(
                "What is an Intent used for in Android?",
                new String[]{"To style buttons", "To navigate between activities", "To store images", "To create databases"},
                1
        ));

        questionList.add(new Question(
                "What does a ProgressBar show in a quiz app?",
                new String[]{"Battery level", "Internet speed", "Quiz progress", "Audio volume"},
                2
        ));

        questionList.add(new Question(
                "Which method is commonly used to connect UI elements in Java?",
                new String[]{"setText()", "findViewById()", "putExtra()", "onPause()"},
                1
        ));
    }

    private void setOptionClickListeners() {
        btnOption1.setOnClickListener(v -> selectOption(0));
        btnOption2.setOnClickListener(v -> selectOption(1));
        btnOption3.setOnClickListener(v -> selectOption(2));
        btnOption4.setOnClickListener(v -> selectOption(3));
    }

    private void setSubmitButtonListener() {
        btnSubmit.setOnClickListener(v -> {
            if (!isSubmitted) {
                handleSubmit();
            } else {
                handleNext();
            }
        });
    }

    private void selectOption(int index) {
        if (isSubmitted) {
            return;
        }

        selectedAnswerIndex = index;
        resetOptionStyles();

        Button selectedButton = getButtonByIndex(index);
        selectedButton.setBackgroundResource(R.drawable.button_selected);
        ViewCompat.setBackgroundTintList(selectedButton, null);
    }

    private void displayQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);

        tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + questionList.size());
        tvQuestion.setText(currentQuestion.getQuestionText());

        String[] options = currentQuestion.getOptions();
        btnOption1.setText(options[0]);
        btnOption2.setText(options[1]);
        btnOption3.setText(options[2]);
        btnOption4.setText(options[3]);

        progressBar.setMax(questionList.size());
        progressBar.setProgress(currentQuestionIndex);
        tvProgressText.setText("Completed: " + currentQuestionIndex + "/" + questionList.size());

        selectedAnswerIndex = -1;
        isSubmitted = false;

        resetOptionStyles();
        enableOptions(true);
        btnSubmit.setText("Submit");
        btnSubmit.setEnabled(true);
    }

    private void handleSubmit() {
        if (selectedAnswerIndex == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        isSubmitted = true;

        Question currentQuestion = questionList.get(currentQuestionIndex);
        int correctIndex = currentQuestion.getCorrectAnswerIndex();

        Button correctButton = getButtonByIndex(correctIndex);
        correctButton.setBackgroundResource(R.drawable.button_correct);
        ViewCompat.setBackgroundTintList(correctButton, null);

        if (selectedAnswerIndex != correctIndex) {
            Button wrongButton = getButtonByIndex(selectedAnswerIndex);
            wrongButton.setBackgroundResource(R.drawable.button_wrong);
            ViewCompat.setBackgroundTintList(wrongButton, null);
        } else {
            score++;
        }

        enableOptions(false);

        progressBar.setProgress(currentQuestionIndex + 1);
        tvProgressText.setText("Completed: " + (currentQuestionIndex + 1) + "/" + questionList.size());

        btnSubmit.setText("Next");
    }

    private void handleNext() {
        currentQuestionIndex++;

        if (currentQuestionIndex < questionList.size()) {
            displayQuestion();
        } else {
            Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
            resultIntent.putExtra("user_name", userName);
            resultIntent.putExtra("score", score);
            resultIntent.putExtra("total_questions", questionList.size());
            startActivity(resultIntent);
            finish();
        }
    }

    private Button getButtonByIndex(int index) {
        switch (index) {
            case 0:
                return btnOption1;
            case 1:
                return btnOption2;
            case 2:
                return btnOption3;
            default:
                return btnOption4;
        }
    }

    private void resetOptionStyles() {
        btnOption1.setBackgroundResource(R.drawable.button_default);
        btnOption2.setBackgroundResource(R.drawable.button_default);
        btnOption3.setBackgroundResource(R.drawable.button_default);
        btnOption4.setBackgroundResource(R.drawable.button_default);

        ViewCompat.setBackgroundTintList(btnOption1, null);
        ViewCompat.setBackgroundTintList(btnOption2, null);
        ViewCompat.setBackgroundTintList(btnOption3, null);
        ViewCompat.setBackgroundTintList(btnOption4, null);
    }

    private void enableOptions(boolean enabled) {
        btnOption1.setEnabled(enabled);
        btnOption2.setEnabled(enabled);
        btnOption3.setEnabled(enabled);
        btnOption4.setEnabled(enabled);

        btnOption1.setClickable(enabled);
        btnOption2.setClickable(enabled);
        btnOption3.setClickable(enabled);
        btnOption4.setClickable(enabled);
    }
}