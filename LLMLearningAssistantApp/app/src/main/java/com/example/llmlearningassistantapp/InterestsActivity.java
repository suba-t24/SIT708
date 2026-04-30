package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityInterestsBinding;
import com.example.llmlearningassistantapp.util.SessionManager;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    private ActivityInterestsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInterestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setBackgroundTintList(binding.btnNext, null);

        binding.btnNext.setOnClickListener(v -> {
            List<String> selected = new ArrayList<>();

            for (int i = 0; i < binding.chipGroupTopics.getChildCount(); i++) {
                Chip chip = (Chip) binding.chipGroupTopics.getChildAt(i);
                if (chip.isChecked()) {
                    selected.add(chip.getText().toString());
                }
            }

            if (selected.isEmpty()) {
                Toast.makeText(this, "Select at least one interest", Toast.LENGTH_SHORT).show();
                return;
            }

            String username = getIntent().getStringExtra("username");
            String email = getIntent().getStringExtra("email");

            new SessionManager(this).saveUser(username, email, selected);

            startActivity(new Intent(InterestsActivity.this, DashboardActivity.class));
        });
    }
}
