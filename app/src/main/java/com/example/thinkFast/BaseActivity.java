package com.example.thinkFast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "HeaderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
    }

    public void logout(){

        SharedPreferences sharedpreferences = getSharedPreferences(AccountActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.d(TAG,"SharedPrefs: " + sharedpreferences.getString(AccountActivity.uName,"Should have a username"));
        editor.clear();
        editor.commit();
        Log.d(TAG,"SharedPrefs: " + sharedpreferences.getString(AccountActivity.uName,"null"));
        // Beina á forsíðu
        startActivity(new Intent(BaseActivity.this,AccountActivity.class));
    }
}