package com.example.thinkFast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import java.util.List;

public class SetupActivity extends BaseActivity {
    //Initializing buttons and such
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
    private Button bPlay;
    private Button bLogOutHeader;

    //Initializing the selected category and number of players
    private int selCategory = -1;
    private int selPlayers = -1;

    //ID's for the category radio buttons
    private static final int RB1_ID = 0;
    private static final int RB2_ID = 1;
    private static final int RB3_ID = 2;
    private static final int RB4_ID = 3;
    //ID's for one player or two player
    private static final int RBP1_ID = 1;
    private static final int RBP2_ID = 2;
    private boolean wasLoggedIn;
    //Initializing a list for the categories
    private List<Category> categories;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        // Bottom navigation
        bottomNavigation();
        Bundle extras = getIntent().getExtras();
        wasLoggedIn= extras.getBoolean("wasLoggedIn");
        //More initialization
        mPlayQuiz = (Button) findViewById(R.id.button_quiz);
        mStatistics = (Button) findViewById(R.id.button_statistics);
        bLogOutHeader = (Button)findViewById(R.id.btn_logout_header);
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

        // CountDown for getting ready
        getReady = (TextView) findViewById(R.id.getReadyCountDown);
        bPlay = (Button)  findViewById(R.id.bQuizSettings);
        //bLogout =(Button) findViewById(R.id.bLogout);
        Log.d(TAG,"was logged in: "+wasLoggedIn);
        if(wasLoggedIn)displayQuizSettings();
        //Creating a random welcome message for user
        int max = 6;
        int min = 1;
        int range = max - min + 1;
        int random = (int) (Math.random()* range) + min;
        String username = getUsername();
        switch(random){
            case 1: mWelcomeUser.setText("Welcome " + username +"!");
                break;
            case 2: mWelcomeUser.setText("Time to think fast, " + username + "!");
                break;
            case 3: mWelcomeUser.setText(username + " are you ready to ruuumble?");
                break;
            case 4: mWelcomeUser.setText("Get your thinking hat on "  + username + "!");
                break;
            case 5: mWelcomeUser.setText(username + " are you sure you are ready to think fast?");
                break;
            case 6: mWelcomeUser.setText("Time to delve into your thinking pool " + username +"!");
        }
        //Calling the networkManager to access the chosen category
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
              displayQuizSettings();
            }
        });

        // Listener for button that starts quiz
        // Will start QuizActivity if a category and number of players has been selected.
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
                    Log.d(TAG,"category: "+selCategory+" players: "+selPlayers);
                }
            }
        });
        //Listener for button that opens statistics
        mStatistics = (Button) findViewById(R.id.button_statistics);
        mStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetupActivity.this, StatisticsActivity.class));
            }
        });

        // Listener for Log out
        bLogOutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set name and ID of categories and player
                Log.d(TAG,"Logging out");
                logout();
            }
        });
    }

    public String getUsername(){
        SharedPreferences sharedpreferences = getSharedPreferences(AccountActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String uname=sharedpreferences.getString(AccountActivity.uName,"null");
        Log.d(TAG,"SharedPrefs: " + sharedpreferences.getString(AccountActivity.uName,"null"));
        return uname;
    }

    public void displayQuizSettings(){
        rbPlayer1=(RadioButton)findViewById(R.id.rg_p1);
        rbPlayer1.setId(RBP1_ID);
        rbPlayer2=(RadioButton)findViewById(R.id.rg_p2);
        rbPlayer2.setId(RBP2_ID);

        //rbCategory1.setText(categories.get(0).getCategoryName()); // Entertainment
        rbCategory1.setText("Entertainment");
        //rbCategory2.setText(categories.get(1).getCategoryName()); // General Knowledge
        rbCategory2.setText("General Knowledge");
        //rbCategory3.setText(categories.get(2).getCategoryName()); // Geography
        rbCategory3.setText("Geography");
        //rbCategory4.setText(categories.get(3).getCategoryName()); // Sports
        rbCategory4.setText("Sports");
        rgPlayerNum.setVisibility(View.VISIBLE);
        rgCategory.setVisibility(View.VISIBLE);
        bPlay.setVisibility(View.VISIBLE);
        mPlayQuiz.setVisibility(View.GONE);
        mStatistics.setVisibility(View.GONE);
        mWelcomeUser.setVisibility(View.GONE);
    }

    public void close(View view){
        finish();
    }
}