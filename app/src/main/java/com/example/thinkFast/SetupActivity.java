package com.example.thinkFast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import java.util.List;

public class SetupActivity extends AppCompatActivity {
    private static final String TAG = "SetupActivity";
    private Button mStatistics;
    private Button mPlayQuiz;
    private TextView mWelcomeUser;
    private TextView getReady;
    private RadioGroup rgPlayerNum;
    private RadioButton rbPlayer1;
    private RadioButton rbPlayer2;
    private RadioGroup rgCategory;
    private RadioButton rbCategory1;
    private RadioButton rbCategory2;
    private RadioButton rbCategory3;
    private RadioButton rbCategory4;
    private RadioButton chosenCategory;
    private Button bPlay;
    private int counter;
    private int turn = 0;

    private int selCategory = -1;
    private int selPlayers = -1;

    private String Name;
    private String Email;
    private String UserName;

    private static final int RB1_ID = 0;
    private static final int RB2_ID = 1;
    private static final int RB3_ID = 2;
    private static final int RB4_ID = 3;
    private static final int RBP1_ID = 1;
    private static final int RBP2_ID = 2;

    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mPlayQuiz = (Button) findViewById(R.id.button_quiz);
        mStatistics = (Button) findViewById(R.id.button_statistics);
        mWelcomeUser = (TextView) findViewById(R.id.velkominn_user);
        rgCategory = (RadioGroup) findViewById(R.id.rg_categories);
        rbCategory1 = (RadioButton) findViewById(R.id.rb_c1);
        rbCategory1.setId(RB1_ID);
        rbCategory2 = (RadioButton) findViewById(R.id.rb_c2);
        rbCategory2.setId(RB2_ID);
        rbCategory3 = (RadioButton) findViewById(R.id.rb_c3);
        rbCategory3.setId(RB3_ID);
        rbCategory4 = (RadioButton) findViewById(R.id.rb_c4);
        rbCategory4.setId(RB4_ID);

        rgPlayerNum = (RadioGroup) findViewById(R.id.rg_players);
        rbPlayer1=(RadioButton)findViewById(R.id.rg_p1);
        rbPlayer2=(RadioButton)findViewById(R.id.rg_p2);

        Bundle extras = getIntent().getExtras();
        Name = extras.getString("name");
        Email = extras.getString("email");
        UserName = extras.getString("username");

        // CountDown for getting ready
        getReady = (TextView) findViewById(R.id.getReadyCountDown);
        bPlay = (Button)  findViewById(R.id.bQuizSettings);

        //Creating a random welcome message for user
        int max = 4;
        int min = 1;
        int range = max - min + 1;
        int random = (int) (Math.random()* range) + min;
        switch(random){
            case 1: mWelcomeUser.setText("Welcome " + Name +  "!");
                break;
            case 2: mWelcomeUser.setText("Time to think fast " + Name + "!");
                break;
            case 3: mWelcomeUser.setText(Name + " are you ready to ruuumble?");
                break;
            case 4: mWelcomeUser.setText("Get your thinking hat on "  + Name + "!");
        }

        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getCategories(new NetworkCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                categories = result;
                Log.d(TAG, "First category:" + categories.get(0).getCategoryName());
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get questions: " + errorString);
            }

        });
    // Listener to start setting up a quiz - where to choose number of players and category
        mPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set name and ID of categories and player
                rbPlayer1=(RadioButton)findViewById(R.id.rg_p1);
                rbPlayer1.setId(RBP1_ID);
                rbPlayer2=(RadioButton)findViewById(R.id.rg_p2);
                rbPlayer2.setId(RBP2_ID);

                rbCategory1.setText(categories.get(0).getCategoryName());
                rbCategory2.setText(categories.get(1).getCategoryName());
                rbCategory3.setText(categories.get(2).getCategoryName());
                rbCategory4.setText(categories.get(3).getCategoryName());
                rgPlayerNum.setVisibility(View.VISIBLE);
                rgCategory.setVisibility(View.VISIBLE);
                bPlay.setVisibility(View.VISIBLE);
                mPlayQuiz.setVisibility(View.GONE);
                mStatistics.setVisibility(View.GONE);
            }
        });

        // Listener for button that starts quiz
        // Will start quizactivity if a category and number of players has been selected.
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get int of selected radio button from radio group
                selCategory = rgCategory.getCheckedRadioButtonId();
                selPlayers = rgPlayerNum.getCheckedRadioButtonId();
                // Missing input handling
                if(selCategory == -1) Toast.makeText(getApplicationContext(),
                        "You have to choose a category", Toast.LENGTH_SHORT).show();
                else if(selPlayers == -1) Toast.makeText(getApplicationContext(),
                        "You have to choose the number of players", Toast.LENGTH_SHORT).show();
                // Start quiz
                else if(selCategory !=-1 && selPlayers !=-1)  {
                    mStatistics.setVisibility(View.GONE);
                    mPlayQuiz.setVisibility(View.GONE);
                    Intent in = new Intent(SetupActivity.this, QuizActivity.class);
                    in.putExtra("categoryID",selCategory);
                    in.putExtra("selPlayers",selPlayers);
                    startActivity(in);
                    Log.d("app","category: "+selCategory+" players: "+selPlayers);
                }
            }
        });
    }
}