package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityLoginBinding;
import com.example.llmlearningassistantapp.network.ApiClient;
import com.example.llmlearningassistantapp.network.AuthResponse;
import com.example.llmlearningassistantapp.network.LoginRequest;
import com.example.llmlearningassistantapp.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setBackgroundTintList(binding.btnLogin, null);

        binding.btnLogin.setOnClickListener(v -> loginUser());

        binding.tvSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }

    private void loginUser() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getService()
                .login(new LoginRequest(username, password))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            AuthResponse.UserDto user = response.body().getUser();

                            String email = user == null ? "" : user.getEmail();
                            String tier = user == null ? "Starter" : user.getUpgradeTier();

                            new SessionManager(LoginActivity.this)
                                    .saveBasicUser(username, email, tier);

                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Backend error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}