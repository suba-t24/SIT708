package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityResultsBinding;
import com.example.llmlearningassistantapp.model.Question;
import com.example.llmlearningassistantapp.model.QuizTask;
import com.example.llmlearningassistantapp.network.ApiClient;
import com.example.llmlearningassistantapp.network.LlmRequest;
import com.example.llmlearningassistantapp.network.LlmResponse;
import com.example.llmlearningassistantapp.util.HistoryManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsActivity extends AppCompatActivity {

    private ActivityResultsBinding binding;
    private QuizTask task;
    private ArrayList<Integer> answers;
    private HistoryManager historyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        historyManager = new HistoryManager(this);

        ViewCompat.setBackgroundTintList(binding.btnExplainQ1, null);
        ViewCompat.setBackgroundTintList(binding.btnExplainQ2, null);
        ViewCompat.setBackgroundTintList(binding.btnExplainQ3, null);
        ViewCompat.setBackgroundTintList(binding.btnContinue, null);

        task = (QuizTask) getIntent().getSerializableExtra("task");
        answers = getIntent().getIntegerArrayListExtra("answers");

        if (task == null || answers == null) {
            finish();
            return;
        }

        showResults();

        binding.btnExplainQ1.setOnClickListener(v ->
                explainAnswer(0, binding.tvPromptR1, binding.tvExplanation1, binding.progressR1, binding.tvErrorR1));

        binding.btnExplainQ2.setOnClickListener(v ->
                explainAnswer(1, binding.tvPromptR2, binding.tvExplanation2, binding.progressR2, binding.tvErrorR2));

        binding.btnExplainQ3.setOnClickListener(v ->
                explainAnswer(2, binding.tvPromptR3, binding.tvExplanation3, binding.progressR3, binding.tvErrorR3));

        binding.btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, DashboardActivity.class);
            intent.putExtra("refreshTask", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void showResults() {
        List<Question> questions = task.getQuestions();

        binding.tvQuestion1.setText("Q1: " + questions.get(0).getQuestionText());
        binding.tvQuestion2.setText("Q2: " + questions.get(1).getQuestionText());
        binding.tvQuestion3.setText("Q3: " + questions.get(2).getQuestionText());

        saveAndShowResult(0, binding.tvStatus1);
        saveAndShowResult(1, binding.tvStatus2);
        saveAndShowResult(2, binding.tvStatus3);
    }

    private void saveAndShowResult(int index, TextView statusView) {
        Question q = task.getQuestions().get(index);

        int selectedIndex = answers.get(index);
        int correctIndex = q.getCorrectIndex();

        String selectedAnswer = q.getOptions().get(selectedIndex);
        String correctAnswer = q.getOptions().get(correctIndex);

        boolean isCorrect = selectedIndex == correctIndex;

        statusView.setText(isCorrect ? "Correct" : "Incorrect");

        historyManager.saveAttempt(
                q.getQuestionText(),
                selectedAnswer,
                correctAnswer,
                isCorrect
        );
    }

    private void explainAnswer(int index, TextView promptView, TextView responseView,
                               ProgressBar progressBar, TextView errorView) {
        Question q = task.getQuestions().get(index);
        int selected = answers.get(index);

        String selectedAnswer = q.getOptions().get(selected);
        String correctAnswer = q.getOptions().get(q.getCorrectIndex());

        String prompt = "Explain why this answer is "
                + (selected == q.getCorrectIndex() ? "correct" : "incorrect")
                + ". Question: " + q.getQuestionText()
                + ". Selected answer: " + selectedAnswer
                + ". Correct answer: " + correctAnswer;

        promptView.setText("Prompt:\n" + prompt);
        progressBar.setVisibility(View.VISIBLE);
        responseView.setText("");
        errorView.setVisibility(View.GONE);

        ApiClient.getService().generate(new LlmRequest(prompt, "explain_answer")).enqueue(new Callback<LlmResponse>() {
            @Override
            public void onResponse(Call<LlmResponse> call, Response<LlmResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String source = response.body().getSource() == null ? "backend" : response.body().getSource();
                    responseView.setText("Response (" + source + "):\n" + response.body().getResponse());
                } else {
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText("Failed to explain answer from backend.");
                }
            }

            @Override
            public void onFailure(Call<LlmResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                errorView.setText("Backend error: " + t.getMessage());
            }
        });
    }
}