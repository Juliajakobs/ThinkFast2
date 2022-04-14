package com.example.thinkFast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;
import com.example.thinkFast.networking.RetrofitAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountActivity extends BaseActivity {

    private static final String TAG = "AccountActivity";
    private List<Account> accounts;
    //Initializing names
    private Button mLoginButton;
    private Button mSignUpButton;
    private Button bLogOutHeader;
    private EditText mUsername, mPassword,mEmail,mName;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String uName = "nameKey";
    public static final String pWord = "phoneKey";
    SharedPreferences sharedpreferences;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://quiz-app-b.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();



    // Dummy data - user and admin
    private Account[] mAccounts = new Account[]{
            new Account("user", "123", "api@hi.is", "User", false),
            new Account("admin", "1234", "admin@hi.is", "Admin", true),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Account selected
        bottomNavigationView.setSelectedItemId(R.id.account);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.account:
                   // TODO user activity
                        if(sharedpreferences.getString(AccountActivity.uName,"null")!="null"){
                            startActivity(new Intent(AccountActivity.this, SetupActivity.class));
                        }
                    case R.id.quiz:
                        // Only perform if logged in
                        if(sharedpreferences.getString(AccountActivity.uName,"null")!="null"){
                            startActivity(new Intent(AccountActivity.this, SetupActivity.class));
                        }

                        for (int i = 0; i < mAccounts.length; i++) {
                            // Check if user is registered and if admin
                            if (mUsername.getText().toString().equals(mAccounts[i].getUsername()) &&
                                    mPassword.getText().toString().equals(mAccounts[i].getPassword())) {
                                //If admin is logging in--> go to admin page
                                if(mUsername.getText().toString().equals("admin")){
                                    startActivity(new Intent(AccountActivity.this, AdminActivity.class));
                                }
                                else{
                                    //Sending information about user to QuizActivity
                                    Intent in = new Intent(AccountActivity.this, SetupActivity.class);
                                    startActivity(in);
                                }}
                        }
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.scoreboard:
                        startActivity(new Intent(AccountActivity.this,ScoreboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //Getting the input from user
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mEmail = (EditText) findViewById(R.id.name);
        mName = (EditText) findViewById(R.id.email);
        //Login
        mLoginButton = (Button) findViewById(R.id.login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username  = mUsername.getText().toString();
                String password  = mPassword.getText().toString();
                if (mUsername.getText().toString().isEmpty() && mPassword.getText().toString().isEmpty()) {
                    Toast.makeText(AccountActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginAccount(username,password);
/*
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(uName, username);
                editor.putString(pWord, password);
                editor.commit();
                loginAccount(mUsername.getText().toString(),mPassword.getText().toString());
                // Checked if user is registered - Log in process
                /*
                for (int i = 0; i < mAccounts.length; i++) {
                    // Check if user is registered and if admin
                    if (mUsername.getText().toString().equals(mAccounts[i].getUsername()) &&
                            mPassword.getText().toString().equals(mAccounts[i].getPassword())) {
                        //If admin is logging in--> go to admin page
                        if(mUsername.getText().toString().equals("admin")){
                            startActivity(new Intent(AccountActivity.this, AdminActivity.class));
                        }
                        else{
                        Log.d(TAG, mUsername.getText().toString() + " " + mAccounts[i].getUsername());
                        //Sending information about user to QuizActivity
                        Intent in = new Intent(AccountActivity.this, SetupActivity.class);
                        startActivity(in);
                    }}
                }
*/
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
    private void loginAccount(String username, String password){
        //Calling networkManager to get questions from DB
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getAccount(username,new NetworkCallback<List<Account>>() {
            @Override
            public void onSuccess(List<Account> result) {
                accounts = result;
                for(int i=0; i< accounts.size();i++) {
                    if(accounts.get(i).getUsername().equals(username) && accounts.get(i).getPassword().equals(password)) {
                        //If admin is logging in--> go to admin page
                        if(mUsername.getText().toString().equals("admin")){
                            startActivity(new Intent(AccountActivity.this, AdminActivity.class));
                        }
                        // Else start set activity as user
                        else startActivity(new Intent(AccountActivity.this, SetupActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get accounts: " + errorString);
            }
        });

    }

    private void loginAccount(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://quiz-app-b.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Account loginAccount = new Account(username,password,null,null,false);
        Call<Account> call = retrofitAPI.loginPost(loginAccount);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account responseFromAPI = response.body();
                Log.d(TAG, "user logged in: "+ responseFromAPI.getUsername());

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(uName, mUsername.getText().toString());
                editor.putString(pWord, mPassword.getText().toString());
                editor.commit();

                if(mUsername.getText().toString().equals("admin")){
                    startActivity(new Intent(AccountActivity.this, AdminActivity.class));
                } else {
                    Intent in = new Intent(AccountActivity.this, SetupActivity.class);
                    startActivity(in);
                }
                Toast.makeText(AccountActivity.this, "hello " + responseFromAPI.getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d(TAG, "error: "+ t.getMessage());
                Toast.makeText(AccountActivity.this, "wrong username or password",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postAccount(String username, String password, String email, String name) {
       /* Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://quiz-app-b.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Account account = new Account(username,password,email,name, false);
        Call<Account> call = retrofitAPI.createPost(account);

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account responseFromAPI = response.body();
                Toast.makeText(AccountActivity.this, "Thank you for signing up, " + responseFromAPI.getUsername(),Toast.LENGTH_SHORT).show();

                //String responseString = "Response Code : " + response.code() + "username: "+ responseFromAPI.getUsername();
                Log.d(TAG, "user created: "+ responseFromAPI.getUsername());
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d(TAG, "error: "+ t.getMessage());
                Toast.makeText(AccountActivity.this, "username already exists!",Toast.LENGTH_SHORT).show();
            }
        });
        // Log out button for header
        bLogOutHeader = (Button)findViewById(R.id.btn_logout_header);
        // Listener for Log out
        bLogOutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set name and ID of categories and player
                Log.d("AccountActivity","Logging out");
                logout();
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