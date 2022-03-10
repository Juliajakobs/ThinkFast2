package com.example.thinkFast;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    private Button mStatistics;
    private Button mPlayQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mStatistics = (Button) findViewById(R.id.button_statistics);
        mPlayQuiz = (Button) findViewById(R.id.button_quiz);
    }

}