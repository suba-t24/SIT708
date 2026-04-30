package com.example.llmlearningassistantapp.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LlmApiService {
    @POST("generate")
    Call<LlmResponse> generate(@Body LlmRequest request);
}
