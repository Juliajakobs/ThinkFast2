package com.example.thinkFast;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
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
    private List<String> scores = new ArrayList<String>();
    private List<String> usernameList = new ArrayList<String>();
    private List<String> scoreList = new ArrayList<String>();
    private String TAG ="scoreboardActiviity";
    private LinearLayout nameCol;
    private LinearLayout scoreCol;

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
                   Log.d(TAG, "Account userList: " + usernameList.get(i));
                   Log.d(TAG, "Account ScoreList: " + scoreList.get(i));
                }
                Log.d(TAG, "Account Username: " + scores.get(0).split(" ")[0]);
                Log.d(TAG, "Account Score: " + scores.get(0).split(" ")[1]);

                mTextView_scores = (TextView) findViewById(R.id.textView_scores);
                mTextView_names = (TextView) findViewById(R.id.textView_names);
                scoreCol = (LinearLayout) findViewById(R.id.scoreColumnScore);
                nameCol = (LinearLayout) findViewById(R.id.scoreColumnName);

                // TODO: NEED TO DISPLAY IN TEXTVIEW
                mTextView_names.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                mTextView_scores.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

                mTextView_names.setText("user");
                mTextView_scores.setText("Score");

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                for (int i = 0; i < usernameList.size(); i++) {
                    // Add new textView dynamically to answerColumn 1 and 2
                    TextView textView1 = new TextView(ScoreboardActivity.this);
                    TextView textView2 = new TextView(ScoreboardActivity.this);

                    textView1.setLayoutParams(param);
                    textView2.setLayoutParams(param);

                    textView1.setText(usernameList.get(i));
                    textView2.setText(scoreList.get(i));

                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                    textView2.setTextColor(Color.BLACK);

                    textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    nameCol.addView(textView1);
                    scoreCol.addView(textView2);
                }
                Log.d("myapp","userLength: "+usernameList.size());
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get scores: " + errorString);
            }
        });
    }
}
