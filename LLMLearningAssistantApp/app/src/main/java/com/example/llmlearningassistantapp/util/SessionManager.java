package com.example.llmlearningassistantapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionManager {

    private static final String PREF_NAME = "LLM_LEARNING_PREFS";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_INTERESTS = "interests";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(String username, String email, List<String> interests) {
        preferences.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_EMAIL, email)
                .putString(KEY_INTERESTS, TextUtils.join(",", interests))
                .apply();
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "Student");
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    public List<String> getInterests() {
        String value = preferences.getString(KEY_INTERESTS, "");
        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(value.split(","));
    }
}