package com.example.llmchatbotapp;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    TextView txtWelcome;
    RecyclerView recyclerMessages;
    EditText edtMessage;
    Button btnSend;

    DatabaseHelper db;
    ChatAdapter adapter;
    ArrayList<ChatMessage> messageList;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra("username");

        txtWelcome = findViewById(R.id.txtWelcome);
        recyclerMessages = findViewById(R.id.recyclerMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        txtWelcome.setText("Welcome " + username + "!");

        db = new DatabaseHelper(this);

        messageList = db.getMessagesForUser(username);

        if (messageList.isEmpty()) {
            String welcomeMessage = "Hello " + username + "! How can I help you today?";
            String time = getCurrentTime();

            db.insertMessage(username, welcomeMessage, "bot", time);
            messageList.add(new ChatMessage(0, username, welcomeMessage, "bot", time));
        }

        adapter = new ChatAdapter(messageList);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(adapter);
        recyclerMessages.scrollToPosition(messageList.size() - 1);

        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String userMessage = edtMessage.getText().toString().trim();

        if (userMessage.isEmpty()) {
            Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
            return;
        }

        String time = getCurrentTime();

        ChatMessage userChatMessage = new ChatMessage(0, username, userMessage, "user", time);
        messageList.add(userChatMessage);
        db.insertMessage(username, userMessage, "user", time);

        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerMessages.scrollToPosition(messageList.size() - 1);
        edtMessage.setText("");

        getBotReply(userMessage);
    }

    private void getBotReply(String userMessage) {
        ChatRequest request = new ChatRequest(username, userMessage);

        ApiClient.getChatApi().sendMessage(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                String botReply;

                if (response.isSuccessful() && response.body() != null) {
                    botReply = response.body().getReply();
                } else {
                    botReply = "Sorry, I could not process that request.";
                }

                addBotMessage(botReply);
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                String fallback = "Backend is not connected. Please start the Flask server and try again.";
                addBotMessage(fallback);
            }
        });
    }

    private void addBotMessage(String botReply) {
        String time = getCurrentTime();

        ChatMessage botChatMessage = new ChatMessage(0, username, botReply, "bot", time);
        messageList.add(botChatMessage);
        db.insertMessage(username, botReply, "bot", time);

        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerMessages.scrollToPosition(messageList.size() - 1);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                .format(new Date());
    }
}