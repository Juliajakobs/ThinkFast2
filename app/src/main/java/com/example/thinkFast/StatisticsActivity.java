package com.example.thinkFast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {
    private TextView mGamesPlayed;
    private TextView mAnsweredQuestions;
    private TextView mAnsweredCorrectly;
    private String Name;


    //Dummy data
    private Account[] mAccounts = new Account[]{
            new Account("user", "123", "api@hi.is", "User", false),
            new Account("admin", "1234", "admin@hi.is", "Admin", true),
    };

    private Statistics[] mStats = new Statistics[]{
            new Statistics(mAccounts[0], 0, 20, 15, 2),
            new Statistics(mAccounts[1], 1, 30, 19, 3),
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        mGamesPlayed = (TextView) findViewById(R.id.games_played);
        if (mStats[0].getGamesPlayed() == 1) {
            mGamesPlayed.setText("...played " + mStats[0].getGamesPlayed() + " game!");
        } else {
            mGamesPlayed.setText("...played " + mStats[0].getGamesPlayed() + " games!");
        }

        mAnsweredQuestions = (TextView) findViewById(R.id.answered_questions);
        mAnsweredQuestions.setText("...answered " + mStats[0].getQuestionsAnswered() + " questions in total!");

        mAnsweredCorrectly = (TextView) findViewById(R.id.answered_correctly);
        if(mStats[0].getAnsweredCorrectly() == 1) {
            mAnsweredCorrectly.setText("...answered " + mStats[0].getAnsweredCorrectly() + " question correctly!");
        }
        else{
            mAnsweredCorrectly.setText("...answered " + mStats[0].getAnsweredCorrectly() + " questions correctly!");
        }



    }
}