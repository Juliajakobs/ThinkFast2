package com.example.thinkFast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thinkFast.networking.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountActivity extends BaseActivity {

    private static final String TAG = "AccountActivity";
    //Initializing names
    private Button mLoginButton;
    private Button mSignUpButton;
    private Button bLogOutHeader;
    private EditText mUsername, mPassword,mEmail,mName;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String uName = "nameKey";
    public static final String pWord = "phoneKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        bottomNavigation(R.id.account);
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
                }
                postAccount(mUsername.getText().toString(), mPassword.getText().toString(),mEmail.getText().toString(), mName.getText().toString());
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
                    Log.d(TAG, "þetta er að keyra");
                    startActivity(new Intent(AccountActivity.this, AdminActivity.class));
                } else {
                    Intent in = new Intent(AccountActivity.this, SetupActivity.class);
                    in.putExtra("wasLoggedIn",false);
                    startActivity(in);
                }
                Toast.makeText(AccountActivity.this, "Hello " + responseFromAPI.getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d(TAG, "error: "+ t.getMessage());
                Toast.makeText(AccountActivity.this, "wrong username or password",Toast.LENGTH_SHORT).show();
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

}