package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.data.DummyRepository;
import com.example.llmlearningassistantapp.databinding.ActivityDashboardBinding;
import com.example.llmlearningassistantapp.model.QuizTask;
import com.example.llmlearningassistantapp.network.ApiClient;
import com.example.llmlearningassistantapp.network.LlmRequest;
import com.example.llmlearningassistantapp.network.LlmResponse;
import com.example.llmlearningassistantapp.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private QuizTask task;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        // check if we need to refresh task
        boolean refresh = getIntent().getBooleanExtra("refreshTask", false);

        if (refresh) {
            task = DummyRepository.getGeneratedTask(sessionManager.getInterests());
        } else {
            task = DummyRepository.getGeneratedTask(sessionManager.getInterests());
        }

        ViewCompat.setBackgroundTintList(binding.btnStartTask, null);
        ViewCompat.setBackgroundTintList(binding.btnSummarizeLesson, null);
        ViewCompat.setBackgroundTintList(binding.btnCreateFlashcards, null);
        ViewCompat.setBackgroundTintList(binding.btnStudyPlan, null);
        ViewCompat.setBackgroundTintList(binding.btnProfile, null);

        binding.btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        binding.tvWelcome.setText("Hello, " + sessionManager.getUsername());
        binding.tvTaskTitle.setText(task.getTaskTitle());
        binding.tvTaskDescription.setText(task.getTaskDescription());

        binding.btnStartTask.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TaskActivity.class);
            intent.putExtra("task", task);
            startActivity(intent);
        });

        binding.btnSummarizeLesson.setOnClickListener(v -> callLlmUtility(
                "Summarise this lesson topic in simple words for a student: " + task.getLessonTopic(),
                "summary"
        ));

        binding.btnCreateFlashcards.setOnClickListener(v -> callLlmUtility(
                "Create exactly 3 flashcards for this topic: " + task.getLessonTopic(),
                "flashcards"
        ));

        binding.btnStudyPlan.setOnClickListener(v -> callLlmUtility(
                "Suggest a 7-day study plan based on these interests: " + sessionManager.getInterests(),
                "study_plan"
        ));

        binding.btnChangeInterests.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, InterestsActivity.class);
            startActivity(intent);
        });
    }

    private void callLlmUtility(String prompt, String type) {
        binding.tvPrompt.setText("Prompt:\n" + prompt);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvLlmResponse.setText("");
        binding.tvError.setVisibility(View.GONE);

        ApiClient.getService().generate(new LlmRequest(prompt, type)).enqueue(new Callback<LlmResponse>() {
            @Override
            public void onResponse(Call<LlmResponse> call, Response<LlmResponse> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String source = response.body().getSource() == null ? "backend" : response.body().getSource();
                    binding.tvLlmResponse.setText(
                            "Response (" + source + "):\n" + response.body().getResponse()
                    );
                } else {
                    binding.tvError.setVisibility(View.VISIBLE);
                    binding.tvError.setText("Failed to generate content from backend.");
                }
            }

            @Override
            public void onFailure(Call<LlmResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText("Backend error: " + t.getMessage());
            }
        });
    }
}