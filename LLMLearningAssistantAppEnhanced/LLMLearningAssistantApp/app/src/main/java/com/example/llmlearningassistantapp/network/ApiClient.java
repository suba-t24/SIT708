package com.example.llmlearningassistantapp.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.llmlearningassistantapp.network.LlmApiService;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:5001/";
    private static LlmApiService service;

    public static LlmApiService getService() {
        if (service == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(90, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(90, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(LlmApiService.class);
        }

        return service;
    }
}