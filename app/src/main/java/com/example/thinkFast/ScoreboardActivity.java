package com.example.thinkFast;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//The scoreboard is not complete -functioning on dummy data
public class ScoreboardActivity extends AppCompatActivity {
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

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.scoreboard);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.account:
                        startActivity(new Intent(ScoreboardActivity.this,AccountActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.quiz:
                        //TODO Only perform if logged in
                        startActivity(new Intent(ScoreboardActivity.this,SetupActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.scoreboard:
                        return true;
                }
                return false;
            }
        });

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
