package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivitySignupBinding;
import com.example.llmlearningassistantapp.network.ApiClient;
import com.example.llmlearningassistantapp.network.AuthResponse;
import com.example.llmlearningassistantapp.network.RegisterRequest;
import com.example.llmlearningassistantapp.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setBackgroundTintList(binding.btnCreateAccount, null);

        binding.btnCreateAccount.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String confirmEmail = binding.etConfirmEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(confirmEmail) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.equals(confirmEmail)) {
            Toast.makeText(this, "Emails do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getService()
                .register(new RegisterRequest(username, email, password))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            new SessionManager(SignupActivity.this)
                                    .saveBasicUser(username, email, "Starter");

                            Intent intent = new Intent(SignupActivity.this, InterestsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(SignupActivity.this, "Backend error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}