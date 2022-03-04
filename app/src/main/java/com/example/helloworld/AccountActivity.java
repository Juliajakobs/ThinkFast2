package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    private Button mheimskButton;
    private Button mklarButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mheimskButton = (Button) findViewById(R.id.login);
        mheimskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountActivity.this, R.string.heimsk, Toast.LENGTH_SHORT).show();
            }
        });
        mklarButton = (Button) findViewById(R.id.sign_up);
        mklarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountActivity.this, R.string.klar, Toast.LENGTH_SHORT).show();
            }
        });
    }
}