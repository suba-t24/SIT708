package com.example.llmlearningassistantapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityProfileBinding;
import com.example.llmlearningassistantapp.util.HistoryManager;
import com.example.llmlearningassistantapp.util.SessionManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.llmlearningassistantapp.util.QRCodeUtils;

import java.io.File;
import java.io.FileOutputStream;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SessionManager sessionManager;
    private HistoryManager historyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        historyManager = new HistoryManager(this);

        ViewCompat.setBackgroundTintList(binding.btnHistory, null);
        ViewCompat.setBackgroundTintList(binding.btnUpgrade, null);
        ViewCompat.setBackgroundTintList(binding.btnShareProfile, null);
        ViewCompat.setBackgroundTintList(binding.btnBack, null);

        loadProfile();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnHistory.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, HistoryActivity.class)));

        binding.btnUpgrade.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, UpgradeActivity.class)));

        binding.btnShareProfile.setOnClickListener(v -> showQrShareSheet());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
    }

    private void loadProfile() {
        binding.tvUsername.setText(sessionManager.getUsername());
        binding.tvEmail.setText(sessionManager.getEmail());

        binding.tvTotalQuestions.setText(String.valueOf(historyManager.getTotalQuestions()));
        binding.tvCorrectAnswers.setText(String.valueOf(historyManager.getCorrectAnswers()));
        binding.tvIncorrectAnswers.setText(String.valueOf(historyManager.getIncorrectAnswers()));
        binding.tvAccountType.setText(historyManager.getAccountType());
    }

    private void showQrShareSheet() {
        BottomSheetDialog sheet = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.dialog_qr_share, null);
        sheet.setContentView(sheetView);

        ImageView imgQr = sheetView.findViewById(R.id.imgQrCode);
        ProgressBar progressQr = sheetView.findViewById(R.id.progressQr);
        TextView tvProfileData = sheetView.findViewById(R.id.tvQrProfileData);
        Button btnShareQr = sheetView.findViewById(R.id.btnShareQrImage);
        Button btnShareText = sheetView.findViewById(R.id.btnShareText);
        Button btnClose = sheetView.findViewById(R.id.btnCloseDialog);

        String profileText = buildProfileText();
        String qrContent = buildQrContent();

        tvProfileData.setText(profileText);

        final Bitmap[] qrBitmapHolder = {null};

        btnClose.setOnClickListener(v -> sheet.dismiss());

        btnShareText.setOnClickListener(v -> {
            sheet.dismiss();
            shareAsText(profileText);
        });

        new Thread(() -> {
            try {
                Bitmap qrBitmap = QRCodeUtils.generate(qrContent, 600);
                qrBitmapHolder[0] = qrBitmap;

                runOnUiThread(() -> {
                    progressQr.setVisibility(View.GONE);
                    imgQr.setImageBitmap(qrBitmap);
                    imgQr.setVisibility(View.VISIBLE);
                    btnShareQr.setEnabled(true);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressQr.setVisibility(View.GONE);
                    tvProfileData.setText("Unable to generate QR code.");
                });
            }
        }).start();

        btnShareQr.setOnClickListener(v -> {
            if (qrBitmapHolder[0] != null) {
                sheet.dismiss();
                shareQrImage(qrBitmapHolder[0], profileText);
            }
        });

        sheet.show();
    }

    private String buildProfileText() {
        return "LLM Learning Assistant Profile\n"
                + "Name: " + sessionManager.getUsername() + "\n"
                + "Email: " + sessionManager.getEmail() + "\n"
                + "Account Type: " + historyManager.getAccountType() + "\n"
                + "Total Questions: " + historyManager.getTotalQuestions() + "\n"
                + "Correct Answers: " + historyManager.getCorrectAnswers() + "\n"
                + "Incorrect Answers: " + historyManager.getIncorrectAnswers();
    }

    private String buildQrContent() {
        return "LLM Learning Assistant Profile\n"
                + "User: " + sessionManager.getUsername() + "\n"
                + "Account: " + historyManager.getAccountType() + "\n"
                + "Total: " + historyManager.getTotalQuestions() + "\n"
                + "Correct: " + historyManager.getCorrectAnswers() + "\n"
                + "Incorrect: " + historyManager.getIncorrectAnswers();
    }

    private void shareQrImage(Bitmap bitmap, String caption) {
        try {
            File imagesDir = new File(getCacheDir(), "images");
            imagesDir.mkdirs();

            File qrFile = new File(imagesDir, "learning_profile_qr.png");
            FileOutputStream fos = new FileOutputStream(qrFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    qrFile
            );

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Share QR Code"));

        } catch (Exception e) {
            shareAsText(caption);
        }
    }

    private void shareAsText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "Share Profile"));
    }
}