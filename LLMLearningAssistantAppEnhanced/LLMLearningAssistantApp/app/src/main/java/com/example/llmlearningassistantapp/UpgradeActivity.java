package com.example.llmlearningassistantapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.llmlearningassistantapp.databinding.ActivityUpgradeBinding;
import com.example.llmlearningassistantapp.network.ApiClient;
import com.example.llmlearningassistantapp.network.BasicResponse;
import com.example.llmlearningassistantapp.network.SaveUpgradeRequest;
import com.example.llmlearningassistantapp.util.HistoryManager;
import com.example.llmlearningassistantapp.util.PaymentsUtil;
import com.example.llmlearningassistantapp.util.SessionManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.AutoResolveHelper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeActivity extends AppCompatActivity {

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private ActivityUpgradeBinding binding;
    private HistoryManager historyManager;
    private SessionManager sessionManager;
    private PaymentsClient paymentsClient;

    private String pendingPlanName = "";
    private String pendingPlanPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpgradeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        historyManager = new HistoryManager(this);
        sessionManager = new SessionManager(this);
        paymentsClient = PaymentsUtil.createPaymentsClient(this);

        ViewCompat.setBackgroundTintList(binding.btnBack, null);
        ViewCompat.setBackgroundTintList(binding.btnStarter, null);
        ViewCompat.setBackgroundTintList(binding.btnIntermediate, null);
        ViewCompat.setBackgroundTintList(binding.btnAdvanced, null);

        binding.btnBack.setOnClickListener(v -> finish());

        checkGooglePayAvailability();

        binding.btnStarter.setOnClickListener(v -> requestPayment("Starter", "$1.99"));
        binding.btnIntermediate.setOnClickListener(v -> requestPayment("Intermediate", "$4.99"));
        binding.btnAdvanced.setOnClickListener(v -> requestPayment("Advanced", "$9.99"));
    }

    private void checkGooglePayAvailability() {
        try {
            JSONObject requestJson = PaymentsUtil.isReadyToPayRequest();
            IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(requestJson.toString());

            Task<Boolean> task = paymentsClient.isReadyToPay(request);
            task.addOnCompleteListener(completedTask -> {
                try {
                    boolean available = completedTask.getResult(ApiException.class);
                    if (!available) {
                        Toast.makeText(this, "Google Pay is not available on this device", Toast.LENGTH_SHORT).show();
                    }
                } catch (ApiException e) {
                    Toast.makeText(this, "Google Pay availability check failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Unable to check Google Pay availability", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPayment(String planName, String price) {
        pendingPlanName = planName;
        pendingPlanPrice = price;

        try {
            JSONObject paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);
            PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());

            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        this,
                        LOAD_PAYMENT_DATA_REQUEST_CODE
                );
            }

        } catch (Exception e) {
            Toast.makeText(this, "Unable to start Google Pay: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);

                    if (paymentData != null) {
                        Toast.makeText(this, "Payment successful: " + pendingPlanName, Toast.LENGTH_SHORT).show();
                        saveUpgradePlan(pendingPlanName);
                    }
                    break;

                case RESULT_CANCELED:
                    Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
                    break;

                case AutoResolveHelper.RESULT_ERROR:
                    com.google.android.gms.common.api.Status status =
                            AutoResolveHelper.getStatusFromIntent(data);

                    Toast.makeText(this,
                            "Google Pay error: " + (status != null ? status.getStatusMessage() : "Unknown error"),
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void saveUpgradePlan(String planName) {
        historyManager.saveAccountType(planName);
        sessionManager.saveTier(planName);

        ApiClient.getService()
                .saveUpgradeTier(new SaveUpgradeRequest(sessionManager.getUsername(), planName))
                .enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        Toast.makeText(UpgradeActivity.this, planName + " plan activated", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        Toast.makeText(UpgradeActivity.this, "Plan saved locally. Backend sync failed.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}