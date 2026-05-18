package com.example.llmlearningassistantapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityHistoryBinding;
import com.example.llmlearningassistantapp.util.HistoryManager;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private HistoryManager historyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        historyManager = new HistoryManager(this);

        ViewCompat.setBackgroundTintList(binding.btnBack, null);
        ViewCompat.setBackgroundTintList(binding.btnClearHistory, null);

        binding.tvHistoryContent.setText(historyManager.getHistoryText());

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnClearHistory.setOnClickListener(v -> {
            getSharedPreferences("QUIZ_HISTORY_PREFS", MODE_PRIVATE).edit().clear().apply();
            binding.tvHistoryContent.setText("No history available yet.");
        });
    }
}