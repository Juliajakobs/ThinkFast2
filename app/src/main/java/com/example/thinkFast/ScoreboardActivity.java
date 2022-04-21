package com.example.thinkFast;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import java.util.ArrayList;
import java.util.List;

//The scoreboard is not complete -functioning on dummy data
public class ScoreboardActivity extends BaseActivity {
    private TextView mTextView_scores;
    private TextView mTextView_names;
    private NetworkManager networkManager;
    private List<String> scores;
    private List <String> usernameList = new ArrayList<>();
    private List <String> scoreList = new ArrayList<>();
    private String TAG ="scoreboardActiviity";

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


        //Calling networkManager to get questions from DB
        networkManager = NetworkManager.getInstance(this);
        networkManager.getscores(new NetworkCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                scores = result;
               for(int i=0;i<scores.size();i++){
                   usernameList.add(scores.get(i).split(" ")[0]);
                   scoreList.add(scores.get(i).split(" ")[1]);
                }
                Log.d(TAG, "Account Username: " + scores.get(0).split(" ")[0]);
                Log.d(TAG, "Account Score: " + scores.get(0).split(" ")[1]);
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get scores: " + errorString);
            }
        });

        mTextView_scores = (TextView) findViewById(R.id.textView_scores);
        mTextView_names = (TextView) findViewById(R.id.textView_names);

        StringBuilder names = new StringBuilder();
        StringBuilder scores = new StringBuilder();
        for (int i = 0; i < usernameList.size(); i++) {
            names.append(usernameList.get(i)).append("\n");
            scores.append(scoreList.get(i)).append("\n");
        }
        // TODO: NEED TO DISPLAY IN TEXTVIEW
        mTextView_names.setText("user");
        mTextView_scores.setText("Score");
    }

}
