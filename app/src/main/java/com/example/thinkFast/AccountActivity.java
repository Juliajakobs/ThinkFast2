package com.example.thinkFast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mUsername, mPassword,mEmail,mName;
    // Dummy data - user and admin
    private Account[] mAccounts = new Account[]{
            new Account("user", "123", "api@hi.is", "User", false),
            new Account("admin", "1234", "admin@hi.is", "Admin", true),
            new Account("julia", "12", "juj7@hi.is", "Júlía", false),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        // Login/register
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mEmail = (EditText) findViewById(R.id.name);
        mName = (EditText) findViewById(R.id.email);

        mLoginButton = (Button) findViewById(R.id.login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checked if user is registered
                for (int i = 0; i < mAccounts.length; i++) {
                    // Check if user is admin
                    if (mUsername.getText().toString().equals(mAccounts[i].getUsername()) &&
                            mPassword.getText().toString().equals(mAccounts[i].getPassword())) {
                        if(mUsername.getText().toString().equals("admin")){
                            startActivity(new Intent(AccountActivity.this, AdminActivity.class));
                        }
                        else{
                        Log.d("MyApp", mUsername.getText().toString() + " " + mAccounts[i].getUsername());
                        //Sending information about user to QuizActivity
                        Intent in = new Intent(AccountActivity.this, QuizActivity.class);
                        in.putExtra("username", mAccounts[i].getUsername());
                        in.putExtra("name", mAccounts[i].getName());
                        in.putExtra("email", mAccounts[i].getEmail());

                        startActivity(in);
                    }} /*else {
                        Toast.makeText(AccountActivity.this, R.string.sign_up_msg, Toast.LENGTH_SHORT).show();
                        Log.d("MyApp", "USER NOT FOUND "+mUsername.getText().toString() + " " + mAccounts[i].getUsername());
                    }*/
                }

            }
        });
        mSignUpButton = (Button) findViewById(R.id.sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mName.setVisibility(View.VISIBLE);
                mEmail.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
                if(mUsername.getText().length()!=0 && mEmail.getText().length()!=0 &&mName.getText().length()!=0&& mEmail.getText().length()!=0){
                   mAccounts=saveAccount(mAccounts.length,mAccounts,new Account(mUsername.getText().toString(),mPassword.getText().toString(),mName.getText().toString(),mEmail.getText().toString(),false));
                    startActivity(new Intent(AccountActivity.this, QuizActivity.class));
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