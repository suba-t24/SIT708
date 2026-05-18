package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityTaskBinding;
import com.example.llmlearningassistantapp.model.Question;
import com.example.llmlearningassistantapp.model.QuizTask;
import com.example.llmlearningassistantapp.network.ApiClient;
import com.example.llmlearningassistantapp.network.LlmRequest;
import com.example.llmlearningassistantapp.network.LlmResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {

    private ActivityTaskBinding binding;
    private QuizTask task;
    private final List<Question> questions = new ArrayList<>();
    private final ArrayList<Integer> selectedAnswers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setBackgroundTintList(binding.btnHintQ1, null);
        ViewCompat.setBackgroundTintList(binding.btnHintQ2, null);
        ViewCompat.setBackgroundTintList(binding.btnHintQ3, null);
        ViewCompat.setBackgroundTintList(binding.btnSubmitTask, null);

        task = (QuizTask) getIntent().getSerializableExtra("task");

        if (task == null) {
            finish();
            return;
        }

        questions.addAll(task.getQuestions());
        bindQuestions();

        binding.btnHintQ1.setOnClickListener(v ->
                generateHint(0, binding.tvPromptQ1, binding.tvHintQ1, binding.progressQ1, binding.tvErrorQ1));

        binding.btnHintQ2.setOnClickListener(v ->
                generateHint(1, binding.tvPromptQ2, binding.tvHintQ2, binding.progressQ2, binding.tvErrorQ2));

        binding.btnHintQ3.setOnClickListener(v ->
                generateHint(2, binding.tvPromptQ3, binding.tvHintQ3, binding.progressQ3, binding.tvErrorQ3));

        binding.btnSubmitTask.setOnClickListener(v -> submitTask());
    }

    private void bindQuestions() {
        setQuestion(binding.q1Title, binding.q1Option1, binding.q1Option2, binding.q1Option3, binding.q1Option4, questions.get(0));
        setQuestion(binding.q2Title, binding.q2Option1, binding.q2Option2, binding.q2Option3, binding.q2Option4, questions.get(1));
        setQuestion(binding.q3Title, binding.q3Option1, binding.q3Option2, binding.q3Option3, binding.q3Option4, questions.get(2));
    }

    private void setQuestion(TextView title, RadioButton o1, RadioButton o2, RadioButton o3, RadioButton o4, Question question) {
        title.setText(question.getQuestionText());
        o1.setText(question.getOptions().get(0));
        o2.setText(question.getOptions().get(1));
        o3.setText(question.getOptions().get(2));
        o4.setText(question.getOptions().get(3));
    }

    private void generateHint(int index, TextView promptView, TextView responseView,
                              ProgressBar progressBar, TextView errorView) {
        String prompt = "Generate a short hint without revealing the answer for this question: "
                + questions.get(index).getQuestionText();

        promptView.setText("Prompt:\n" + prompt);
        progressBar.setVisibility(View.VISIBLE);
        responseView.setText("");
        errorView.setVisibility(View.GONE);

        ApiClient.getService().generate(new LlmRequest(prompt, "hint")).enqueue(new Callback<LlmResponse>() {
            @Override
            public void onResponse(Call<LlmResponse> call, Response<LlmResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String source = response.body().getSource() == null ? "backend" : response.body().getSource();
                    responseView.setText("Response (" + source + "):\n" + response.body().getResponse());
                } else {
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText("Failed to generate hint from backend.");
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

    private void submitTask() {
        selectedAnswers.clear();

        selectedAnswers.add(getSelectedIndex(binding.groupQ1));
        selectedAnswers.add(getSelectedIndex(binding.groupQ2));
        selectedAnswers.add(getSelectedIndex(binding.groupQ3));

        if (selectedAnswers.contains(-1)) {
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(TaskActivity.this, ResultsActivity.class);
        intent.putExtra("task", task);
        intent.putIntegerArrayListExtra("answers", selectedAnswers);
        startActivity(intent);
    }

    private int getSelectedIndex(RadioGroup group) {
        int checkedId = group.getCheckedRadioButtonId();
        if (checkedId == -1) return -1;

        if (checkedId == group.getChildAt(0).getId()) return 0;
        if (checkedId == group.getChildAt(1).getId()) return 1;
        if (checkedId == group.getChildAt(2).getId()) return 2;
        if (checkedId == group.getChildAt(3).getId()) return 3;

        return -1;
    }
}