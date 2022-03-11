package com.example.thinkFast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity{

    private Button cat1;
    private Button cat2;
    private Button cat3;
    private Button cat4;

    // dummy data - categories
    private Category[] categories = new Category[]{
            new Category("Sport", 1),
            new Category("Science", 2),
            new Category("Geography", 3),
            new Category("General", 4)
    };

    // dummy data - question
    private Question[] questions = new Question[]{
            new Question(4, "Which option is a color?", "Yellow", "Banana", "Sun", "Yellow", "Sunflower")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
    }

}