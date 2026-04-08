package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResultMessage;
    private TextView tvScore;
    private Button btnTakeNewQuiz, btnFinish;
    private Switch switchTheme;

    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvResultMessage = findViewById(R.id.tvResultMessage);
        tvScore = findViewById(R.id.tvScore);
        btnTakeNewQuiz = findViewById(R.id.btnTakeNewQuiz);
        btnFinish = findViewById(R.id.btnFinish);
        switchTheme = findViewById(R.id.switchThemeResult);

        ViewCompat.setBackgroundTintList(btnTakeNewQuiz, null);
        ViewCompat.setBackgroundTintList(btnFinish, null);

        userName = getIntent().getStringExtra("user_name");
        if (userName == null) userName = "";

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total_questions", 0);

        tvResultMessage.setText("Congratulations " + userName + "!");
        tvScore.setText("Your score: " + score + "/" + total);

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

        btnTakeNewQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.putExtra("user_name", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        btnFinish.setOnClickListener(v -> {
            finishAffinity();
        });
    }

    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}