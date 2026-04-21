package com.example.istream.ui.auth;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        EditText etFullName = view.findViewById(R.id.et_fullname);
        EditText etUsername = view.findViewById(R.id.et_reg_username);
        EditText etPassword = view.findViewById(R.id.et_reg_password);
        EditText etConfirm = view.findViewById(R.id.et_confirm_password);
        Button btnCreate = view.findViewById(R.id.btn_create_account);

        btnCreate.setOnClickListener(v -> {
            if (!etPassword.getText().toString().equals(etConfirm.getText().toString())) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User();
            newUser.fullName = etFullName.getText().toString();
            newUser.username = etUsername.getText().toString();
            newUser.password = etPassword.getText().toString();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                AppDatabase.getInstance(requireContext()).userDao().insertUser(newUser);
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                });
            });
        });
        return view;
    }
}