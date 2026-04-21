package com.example.istream.ui.auth;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.istream.R;
import com.example.istream.database.AppDatabase;
import com.example.istream.database.User;
import com.example.istream.ui.home.HomeFragment;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText etUsername = view.findViewById(R.id.et_username);
        EditText etPassword = view.findViewById(R.id.et_password);
        Button btnLogin = view.findViewById(R.id.btn_login);
        Button btnSignup = view.findViewById(R.id.btn_signup_nav);

        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString();
            String pass = etPassword.getText().toString();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                User loggedInUser = AppDatabase.getInstance(requireContext()).userDao().login(user, pass);
                requireActivity().runOnUiThread(() -> {
                    if (loggedInUser != null) {
                        SharedPreferences prefs = requireActivity().getSharedPreferences("iStreamPrefs", Context.MODE_PRIVATE);
                        prefs.edit().putInt("CURRENT_USER_ID", loggedInUser.id).apply();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new HomeFragment())
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        btnSignup.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SignUpFragment())
                .addToBackStack(null).commit());

        return view;
    }
}