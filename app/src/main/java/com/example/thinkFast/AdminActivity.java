package com.example.thinkFast;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Button mDeleteButton;
    private List<Question> questions;

    //Changes the view to admin view
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView yourTextView = (TextView) findViewById(R.id.textView3);

        //Gets questions from database and displays them - not fully complete
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getQuestions(new NetworkCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> result) {
                String multiLineText = "";
                questions = result;
                for(int i =0; i<questions.size(); i++ ) {
                    String question = questions.get(i).getQuestionText();
                    multiLineText = multiLineText + question + "\n" + "\n";
                    yourTextView.setText(multiLineText);
                }
            }

            @Override
            public void onFailure(String errorString) {
                Log.e("lol", "Failed to get questions: " + errorString);
            }
        });


        //Delete button - not complete
        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Should delete selected question from database
                Toast.makeText(AdminActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
