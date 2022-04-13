package com.example.thinkFast.networking;
import com.example.thinkFast.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("signup")
    Call<Account> createPost(@Body Account account);

}
