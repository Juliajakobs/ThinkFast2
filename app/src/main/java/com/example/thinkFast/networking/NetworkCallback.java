package com.example.thinkFast.networking;

public interface NetworkCallback<T> {

    void onSuccess(T result);

    void onFailure(String errorString);
}

