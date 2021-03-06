package com.example.thinkFast.networking;

import com.example.thinkFast.Account;
import com.example.thinkFast.Scores;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("signup")
    Call<Account> createPost(@Body Account account);

    @POST("loginAPI")
    Call<Account> loginPost(@Body Account account);

    @POST("saveScore")
    Call<Scores> saveScore(@Body Scores score);

}
