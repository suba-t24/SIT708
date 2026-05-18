package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityInterestsBinding;
import com.example.llmlearningassistantapp.network.ApiClient;
import com.example.llmlearningassistantapp.network.BasicResponse;
import com.example.llmlearningassistantapp.network.SaveInterestsRequest;
import com.example.llmlearningassistantapp.util.SessionManager;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterestsActivity extends AppCompatActivity {

    private ActivityInterestsBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInterestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        ViewCompat.setBackgroundTintList(binding.btnNext, null);

        binding.btnNext.setOnClickListener(v -> saveInterests());
    }

    private void saveInterests() {
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

        sessionManager.saveInterests(selected);

        ApiClient.getService()
                .saveInterests(new SaveInterestsRequest(sessionManager.getUsername(), selected))
                .enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        startActivity(new Intent(InterestsActivity.this, DashboardActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        Toast.makeText(InterestsActivity.this, "Saved locally. Backend sync failed.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(InterestsActivity.this, DashboardActivity.class));
                        finish();
                    }
                });
    }
}