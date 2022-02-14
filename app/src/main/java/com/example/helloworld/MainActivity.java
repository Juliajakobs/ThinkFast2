package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mheimskButton;
    private Button mklarButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mheimskButton = (Button) findViewById(R.id.heimsk_button);
        mheimskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.heimsk, Toast.LENGTH_SHORT).show();
            }
        });
        mklarButton = (Button) findViewById(R.id.klar_button);
        mklarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.klar, Toast.LENGTH_SHORT).show();
            }
        });
    }
}