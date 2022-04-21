package com.example.thinkFast;

import android.os.Bundle;
import android.widget.TextView;

//The scoreboard is not complete -functioning on dummy data
public class ScoreboardActivity extends BaseActivity {
    private TextView mTextView_scores;
    private TextView mTextView_names;

    // dummy data
    private Account[] mAccounts = new Account[]{
            new Account("user", "123", "api@hi.is", "User", false),
            new Account("admin", "1234", "admin@hi.is", "Admin", true),
    };

    private Scores[] mScores = new Scores[] {
            new Scores(mAccounts[0], 3),
            new Scores(mAccounts[1], 7)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        // Bottom navigation
        bottomNavigation(R.id.scoreboard);

        mTextView_scores = (TextView) findViewById(R.id.textView_scores);
        mTextView_names = (TextView) findViewById(R.id.textView_names);
        StringBuilder names = new StringBuilder();
        StringBuilder scores = new StringBuilder();
        for (int i = 0; i < mScores.length; i++) {
            int score = mScores[i].getScore();
            String name = mScores[i].getAccount().getUsername();
            names.append(name).append("\n");
            scores.append(score).append("\n");
        }
        //Displaying the dummy data on the scoreboard
        mTextView_names.setText(names.toString());
        mTextView_scores.setText(scores.toString());
    }

}
