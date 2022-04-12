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
import android.view.MenuItem;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {
    //Initializing names
    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mUsername, mPassword,mEmail,mName;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String uName = "nameKey";
    public static final String pWord = "phoneKey";
    SharedPreferences sharedpreferences;


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
                        return true;
                    case R.id.quiz:
                        //TODO Only perform if logged in
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

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(uName, username);
                editor.putString(pWord, password);
                editor.commit();

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
                if(mUsername.getText().length()!=0 && mEmail.getText().length()!=0 &&mName.getText().length()!=0&& mEmail.getText().length()!=0){
                   mAccounts=saveAccount(mAccounts.length,mAccounts,new Account(mUsername.getText().toString(),mPassword.getText().toString(),mName.getText().toString(),mEmail.getText().toString(),false));
                    startActivity(new Intent(AccountActivity.this, SetupActivity.class));
                }
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