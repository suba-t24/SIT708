package com.example.llmlearningassistantapp.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LlmApiService {

    @POST("generate")
    Call<LlmResponse> generate(@Body LlmRequest request);

    @POST("register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("saveInterests")
    Call<BasicResponse> saveInterests(@Body SaveInterestsRequest request);

    @POST("saveQuizResult")
    Call<BasicResponse> saveQuizResult(@Body SaveQuizResultRequest request);

    @GET("getHistory")
    Call<HistoryResponse> getHistory(@Query("username") String username);

    @GET("getProfile")
    Call<ProfileResponse> getProfile(@Query("username") String username);

    @POST("saveUpgradeTier")
    Call<BasicResponse> saveUpgradeTier(@Body SaveUpgradeRequest request);
}