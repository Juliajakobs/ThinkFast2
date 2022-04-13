package com.example.thinkFast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;
import com.example.thinkFast.networking.RetrofitAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "AccountActivity";

    //Initializing names
    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mUsername, mPassword,mEmail,mName;
    // Dummy data - user and admin
    private Account newAccount;
    private Account[] mAccounts = new Account[]{
            new Account("user", "123", "api@hi.is", "User", false),
            new Account("admin", "1234", "admin@hi.is", "Admin", true),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //Getting the input from user
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mEmail = (EditText) findViewById(R.id.name);
        mName = (EditText) findViewById(R.id.email);
        //Login
        mLoginButton = (Button) findViewById(R.id.login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checked if user is registered
                for (int i = 0; i < mAccounts.length; i++) {
                    // Check if user is registered and if admin
                    if (mUsername.getText().toString().equals(mAccounts[i].getUsername()) &&
                            mPassword.getText().toString().equals(mAccounts[i].getPassword())) {
                        //If admin is logging in--> go to admin page
                        if(mUsername.getText().toString().equals("admin")){
                            startActivity(new Intent(AccountActivity.this, AdminActivity.class));
                        }
                        else{
                        Log.d("MyApp", mUsername.getText().toString() + " " + mAccounts[i].getUsername());
                        //Sending information about user to QuizActivity
                        Intent in = new Intent(AccountActivity.this, SetupActivity.class);
                        in.putExtra("username", mAccounts[i].getUsername());
                        in.putExtra("name", mAccounts[i].getName());
                        in.putExtra("email", mAccounts[i].getEmail());

                        startActivity(in);
                    }}
                }

            }
        });

        //Sign up
        mSignUpButton = (Button) findViewById(R.id.sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName.setVisibility(View.VISIBLE);
                mEmail.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
                //Creating a new account - not complete!
                if(mUsername.getText().toString().isEmpty() && mEmail.getText().toString().isEmpty() &&mName.getText().toString().isEmpty()&& mEmail.getText().toString().isEmpty()){
                    Toast.makeText(AccountActivity.this, "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                    //mAccounts=saveAccount(mAccounts.length,mAccounts,new Account(mUsername.getText().toString(),mPassword.getText().toString(),mName.getText().toString(),mEmail.getText().toString(),false));
                    //startActivity(new Intent(AccountActivity.this, SetupActivity.class));
                }
                postAccount(mUsername.getText().toString(), mPassword.getText().toString(),mEmail.getText().toString(), mName.getText().toString());
            }
        });
    }

    private void postAccount(String username, String password, String email, String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://quiz-app-b.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Account account = new Account(username,password,email,name, false);
        Call<Account> call = retrofitAPI.createPost(account);

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Toast.makeText(AccountActivity.this, "You have signed up!",Toast.LENGTH_SHORT).show();

                Account responseFromAPI = response.body();

                //String responseString = "Response Code : " + response.code() + "username: "+ responseFromAPI.getUsername();
                Log.d(TAG, "user created: "+ responseFromAPI.getUsername());
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d(TAG, "error: "+ t.getMessage());
            }
        });
    }

    // Helper function to save account in dummy data array
    public static Account[] saveAccount(int n, Account accounts[], Account acc) {
        int i;
        Account newArray[] = new Account[n + 1];
        for (i = 0; i < n; i++)
        newArray[i] = accounts[i];
        newArray[n] = acc;
        return newArray;
    }
}