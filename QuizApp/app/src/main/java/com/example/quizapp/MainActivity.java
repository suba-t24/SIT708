package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private AppCompatButton btnStart;
    private Switch switchThemeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        btnStart = findViewById(R.id.btnStart);
        switchThemeMain = findViewById(R.id.switchThemeMain);

        ViewCompat.setBackgroundTintList(btnStart, null);

        SharedPreferences prefs = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);

        String savedName = prefs.getString("user_name", "");
        etName.setText(savedName);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user_name")) {
            String returnedName = intent.getStringExtra("user_name");
            etName.setText(returnedName);
        }

        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        switchThemeMain.setChecked(isDarkMode);

        switchThemeMain.setOnCheckedChangeListener((buttonView, checked) -> {
            prefs.edit().putBoolean("dark_mode", checked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate();
        });

        btnStart.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                etName.setError("Please enter your name");
                return;
            }

            prefs.edit().putString("user_name", name).apply();

            Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
            quizIntent.putExtra("user_name", name);
            startActivity(quizIntent);
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