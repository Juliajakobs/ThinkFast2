package com.example.thinkFast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private Button mStatistics;
    private Button mPlayQuiz;
    private TextView mWelcomeUser;
    private String Name;
    private String Email;
    private String UserName;

    private static final String TAG = "QuizActivity";
    // quiz setting, number of players and what category
    private RadioGroup rgPlayerNum;
    private RadioButton rbPlayer1;
    private RadioButton rbPlayer2;
    private RadioGroup rgCategory;
    private RadioButton rbCategory1;
    private RadioButton rbCategory2;
    private RadioButton rbCategory3;
    private RadioButton rbCategory4;
    private Button bPlay;

    // Question and answers
    private TextView questionText;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;
    private Button mScoreboard;
    private int questionIndex;
    private int selCategory = -1;
    private int selPlayers = -1;
    // Id's for category and player radio buttons
    private static final int RB1_ID = 1;
    private static final int RB2_ID = 2;
    private static final int RB3_ID = 3;
    private static final int RB4_ID = 4;
    private static final int RBP1_ID = 1;
    private static final int RBP2_ID = 2;
    // Get ready Countdown timer
    private TextView getReady;
    private int counter;


    private final int maxNumOfQuestions = 10;
    private String[] player1AnswersArray = new String[10];
    private String[] player2AnswersArray = new String[10];
    private String[] correct1AnswersArray = new String[10];
    private String[] correct2AnswersArray = new String[10];
    private int player1Score=0;
    private int player2Score=0;
    private int turn = 0;


    // End screen widget
    private ScrollView answerScroll;
    private LinearLayout answerColumn1;
    private LinearLayout answerColumn2;
    private Button bPlayAgain;
    private Button bEndQuiz;

    private ProgressBar mProgressbar;
    private CountDownTimer mCountDownTimer;
    private int i=0;

    private List<Category> categories;

    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Allt findView tengingar dotið er gert i thetta function
        setFindView();

        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getQuestionsByCategory(1,new NetworkCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> result) {
                questions = result;
                Log.d(TAG, "First question:" + questions.get(0).getQuestionText());
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get questions: " + errorString);
            }
        });

        networkManager.getCategories(new NetworkCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                categories = result;
                Log.d(TAG, "First category:" + categories.get(0).getCategoryName());

                rbCategory1.setText(categories.get(0).getCategoryName());
                rbCategory2.setText(categories.get(1).getCategoryName());
                rbCategory3.setText(categories.get(2).getCategoryName());
                rbCategory4.setText(categories.get(3).getCategoryName());
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get questions: " + errorString);
            }

        });

        // Set time for progress bar
        mProgressbar.setProgress(i);

        //Getting information about logged in user from AccountActivity
        Bundle extras = getIntent().getExtras();
        Name = extras.getString("name");
        Email = extras.getString("email");
        UserName = extras.getString("username");
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

        // Set time for progress bar
        mProgressbar.setProgress(i);

        mCountDownTimer=new CountDownTimer(5000,400) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressbar.setProgress((int)i*500/(5000/400));
            }
            @Override
            public void onFinish() {
                // her er i = 12
                //Add "Timed out" as user answer if question is not answered in the time limit
                if (selPlayers == 2) {
                    if (turn == 1) {
                        player1AnswersArray[questionIndex] = "Timed out";
                        correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                    } else if (turn == 2) {
                        player2AnswersArray[questionIndex] = "Timed out";
                        correct2AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                    }
                }
                else {
                    player1AnswersArray[questionIndex] = "Timed out";
                    correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                }

                if (questionIndex < maxNumOfQuestions) {
                    getNextQuestion();
                }
                else {
                    if (selPlayers == 2 && turn == 1) getReadyCountDown();
                    else {
                        visibleQuizPlay(false);
                        visibleEnd(true);
                        showAnswers();
                    }
                }

            }
        };

        mScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizActivity.this, ScoreboardActivity.class));
            }
        });

        // Play quiz, a.k.a quiz settings
        mPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleMenu(false);
                visibleQuizSettings(true);

                // Set name and ID of categories and player
                rbPlayer1.setId(RBP1_ID);
                rbPlayer2.setId(RBP2_ID);

                rbCategory1.setId(RB1_ID);
                rbCategory2.setId(RB2_ID);
                rbCategory3.setId(RB3_ID);
                rbCategory4.setId(RB4_ID);
            }
        });
        //Gera StatisticsActivity? eða er ehv að gera það
      /* mStatistics.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(QuizActivity.this, StatisticsActivity.class));
           }
           });*/

        // Play quiz button eftir settings dót virkni
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get int of selected radio button from radio group
                selCategory = rgCategory.getCheckedRadioButtonId();
                selPlayers = rgPlayerNum.getCheckedRadioButtonId();
                if(selCategory == -1) Toast.makeText(getApplicationContext(),
                        "You have to choose a category", Toast.LENGTH_SHORT).show();
                else if(selPlayers == -1) Toast.makeText(getApplicationContext(),
                        "You have to choose the number of players", Toast.LENGTH_SHORT).show();
                else {
                    // CountDown function, quiz starts after countdown
                    getReadyCountDown();
                }
            }
        });

        // Answer button virkni
        for (Button button: new Button[]{ans1,ans2, ans3, ans4}) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selPlayers == 2) {
                        if (turn == 1) {
                            player1AnswersArray[questionIndex] = button.getText().toString();
                            correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                            // Calculate score if answer is correct
                            if( player1AnswersArray[questionIndex].equals( correct1AnswersArray[questionIndex])) player1Score += calculateScore(player1Score,i);
                        } else if (turn == 2) {
                            player2AnswersArray[questionIndex] = button.getText().toString();
                            correct2AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                            // Calculate score if answer is correct
                            if( player2AnswersArray[questionIndex].equals(correct2AnswersArray[questionIndex])) player2Score += calculateScore(player2Score,i);
                        }
                    }
                    else {
                        player1AnswersArray[questionIndex] = button.getText().toString();
                        correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                        // Calculate score if answer is correct
                        if( player1AnswersArray[questionIndex].equals( correct1AnswersArray[questionIndex])) player1Score += calculateScore(player1Score,i);
                    }

                    if (questionIndex < maxNumOfQuestions-1) getNextQuestion();
                    else {
                        if (selPlayers == 2 && turn == 1) getReadyCountDown();
                        else {
                            visibleQuizPlay(false);
                            visibleEnd(true);
                            showAnswers();
                        }
                    }
                }
            });
        }

        // End screen buttons virkni
        bPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetQuiz();
                visibleEnd(false);
                visibleQuizPlay(true);
                getReadyCountDown();
            }
        });

        bEndQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetQuiz();
                visibleEnd(false);
                visibleQuizSettings(true);
            }
        });
    }

    public void getReadyCountDown() {
        visibleQuizSettings(false);
        visibleQuizPlay(false);
        visibleGetReadyCountDown(true);
        counter = 4;
        turn++;

        // Countdown timer
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                // Set appropriate countdown text
                if (counter == 4) getReady.setText(R.string.getReady3);
                if (counter == 3) getReady.setText(R.string.getReady2);
                if (counter == 2) {
                    if (selPlayers == 2) {
                        if (turn == 1) getReady.setText(R.string.getReady1Player1);
                        if (turn == 2) getReady.setText(R.string.getReady1Player2);
                    }
                    else getReady.setText(R.string.app_name);
                }
                counter--;
            }
            @Override
            public void onFinish() {
                visibleGetReadyCountDown(false);
                visibleQuizPlay(true);
                playQuiz();
            }
        }.start();
    }

    public void playQuiz() {
        questionIndex = 0;

        // Make text reflect the right question
        questionText.setText(questions.get(questionIndex).getQuestionText());
        ans1.setText(questions.get(questionIndex).getOptionA());
        ans2.setText(questions.get(questionIndex).getOptionB());
        ans3.setText(questions.get(questionIndex).getOptionC());
        ans4.setText(questions.get(questionIndex).getOptionD());
        mCountDownTimer.start();
    }

    public void showAnswers() {
        if (selPlayers == 1) {
            TextView playerAns = new TextView(this);
            TextView correctAns = new TextView(this);

            playerAns.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            correctAns.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            playerAns.setText(Name);
            correctAns.setText(R.string.correctAnswers);

            playerAns.setTextColor(Color.BLACK);
            correctAns.setTextColor(Color.BLACK);

            answerColumn1.addView(playerAns);
            answerColumn2.addView(correctAns);

            for (int i = 0; i < player1AnswersArray.length; i++) {
                // Add new textView dynamically to answerColumn 1 and 2
                TextView textView1 = new TextView(this);
                TextView textView2 = new TextView(this);

                textView1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView1.setText(player1AnswersArray[i]);
                textView2.setText(correct1AnswersArray[i]);

                if (textView1.getText().toString().equals(textView2.getText().toString())) {
                    textView1.setTextColor(Color.GREEN);
                    textView2.setTextColor(Color.GREEN);
                } else {
                    textView1.setTextColor(Color.RED);
                    textView2.setTextColor(Color.BLACK);
                }

                answerColumn1.addView(textView1);
                answerColumn2.addView(textView2);
            }
            Log.d("myapp","playerscore: "+player1Score);
        }
        else {
            TextView player1Ans = new TextView(this);
            TextView player2Ans = new TextView(this);

            player1Ans.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            player2Ans.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            player1Ans.setText(R.string.p1);
            player2Ans.setText(R.string.p2);

            player1Ans.setTextColor(Color.BLACK);
            player2Ans.setTextColor(Color.BLACK);

            answerColumn1.addView(player1Ans);
            answerColumn2.addView(player2Ans);

            for (int i = 0; i < player1AnswersArray.length; i++) {
                // Add new textView dynamically to answerColumn 1 and 2
                TextView textView1 = new TextView(this);
                TextView textView2 = new TextView(this);

                textView1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                textView1.setText(player1AnswersArray[i]);
                textView2.setText(player2AnswersArray[i]);

                if (player1AnswersArray[i].equals(correct1AnswersArray[i])) {
                    textView1.setTextColor(Color.GREEN);
                } else {
                    textView1.setTextColor(Color.RED);
                }

                if (player2AnswersArray[i].equals(correct2AnswersArray[i])) {
                    textView2.setTextColor(Color.GREEN);
                } else {
                    textView2.setTextColor(Color.RED);
                }

                answerColumn1.addView(textView1);
                answerColumn2.addView(textView2);

            }
        }
    }

    public void getNextQuestion() {
        resetCounter();
        mProgressbar.setProgress(i);
        questionIndex++;
        questionText.setText(questions.get(questionIndex).getQuestionText());
        ans1.setText(questions.get(questionIndex).getOptionA());
        ans2.setText(questions.get(questionIndex).getOptionB());
        ans3.setText(questions.get(questionIndex).getOptionC());
        ans4.setText(questions.get(questionIndex).getOptionD());
    }

    public void resetQuiz() {
        questionIndex = 0;
        turn = 0;
        resetCounter();
        player1AnswersArray = new String[10];
        player2AnswersArray = new String[10];
        correct1AnswersArray = new String[10];
        correct2AnswersArray = new String[10];
        answerColumn1.removeAllViewsInLayout();
        answerColumn2.removeAllViewsInLayout();
    }

    // Resets counter after each question/quiz
    public void resetCounter(){
        i=0;
        mCountDownTimer.cancel();
        mCountDownTimer.start();
        mProgressbar.setProgress(i);
    }

    public int calculateScore(int playerScore, int i){
        // Timer bonus scores
        if(i<=3)  playerScore = 100;
        else if(i<=6) playerScore = 75;
        else if(i<=9) playerScore = 50;
        else if(i<=12) playerScore = 25;
        return playerScore;
    }

    public void setFindView() {
        // User stuff
        mWelcomeUser = (TextView) findViewById(R.id.velkominn_user);

        // Quiz Start screen
        mStatistics = (Button) findViewById(R.id.button_statistics);
        mPlayQuiz = (Button) findViewById(R.id.button_quiz);

        // Timer
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar);

        // CountDown for getting ready
        getReady = (TextView) findViewById(R.id.getReadyCountDown);

        // Categories
        rbCategory1 = (RadioButton) findViewById(R.id.rb_c1);
        rbCategory2 = (RadioButton) findViewById(R.id.rb_c2);
        rbCategory3 = (RadioButton) findViewById(R.id.rb_c3);
        rbCategory4 = (RadioButton) findViewById(R.id.rb_c4);

        // Setting buttons
        rgCategory = (RadioGroup) findViewById(R.id.rg_categories);
        rbCategory1 = (RadioButton) findViewById(R.id.rb_c1);
        rbCategory2 = (RadioButton) findViewById(R.id.rb_c2);
        rbCategory3 = (RadioButton) findViewById(R.id.rb_c3);
        rbCategory4 = (RadioButton) findViewById(R.id.rb_c4);

        rgPlayerNum = (RadioGroup) findViewById(R.id.rg_players);
        rbPlayer1 = (RadioButton)findViewById(R.id.rg_p1);
        rbPlayer2 = (RadioButton)findViewById(R.id.rg_p2);

        bPlay = (Button)  findViewById(R.id.bQuizSettings);

        // Questions
        questionText = (TextView)  findViewById(R.id.tQuestion);
        ans1 = (Button) findViewById(R.id.bAns1);
        ans2 = (Button) findViewById(R.id.bAns2);
        ans3 = (Button) findViewById(R.id.bAns3);
        ans4 = (Button) findViewById(R.id.bAns4);

        // End screens
        answerScroll = (ScrollView) findViewById(R.id.answersScroll);
        answerColumn1 = (LinearLayout) findViewById(R.id.answerColumn1);
        answerColumn2 = (LinearLayout) findViewById(R.id.answerColumn2);
        bPlayAgain = (Button) findViewById(R.id.bPlayAgain);
        bEndQuiz = (Button) findViewById(R.id.bEndQuiz);

        // Scoreboard
        mScoreboard = (Button) findViewById(R.id.btn_scoreboard);
    }

    public void visibleMenu(Boolean b) {
        if (b) {
            mWelcomeUser.setVisibility(View.VISIBLE);
            mStatistics.setVisibility(View.VISIBLE);
            mPlayQuiz.setVisibility(View.VISIBLE);
        }
        else {
            mWelcomeUser.setVisibility(View.GONE);
            mStatistics.setVisibility(View.GONE);
            mPlayQuiz.setVisibility(View.GONE);
        }
    }

    public void visibleQuizSettings(Boolean b) {
        if (b) {
            rgPlayerNum.setVisibility(View.VISIBLE);
            rgCategory.setVisibility(View.VISIBLE);
            bPlay.setVisibility(View.VISIBLE);
        }
        else {
            rgPlayerNum.setVisibility(View.GONE);
            rgCategory.setVisibility(View.GONE);
            bPlay.setVisibility(View.GONE);
        }
    }

    public void visibleQuizPlay(Boolean b) {
        if (b) {
            mProgressbar.setVisibility(View.VISIBLE);
            questionText.setVisibility(View.VISIBLE);
            ans1.setVisibility(View.VISIBLE);
            ans2.setVisibility(View.VISIBLE);
            ans3.setVisibility(View.VISIBLE);
            ans4.setVisibility(View.VISIBLE);
        } else {
            mProgressbar.setVisibility(View.GONE);
            questionText.setVisibility(View.GONE);
            ans1.setVisibility(View.GONE);
            ans2.setVisibility(View.GONE);
            ans3.setVisibility(View.GONE);
            ans4.setVisibility(View.GONE);
        }
    }

    public void visibleEnd(Boolean b) {
        mCountDownTimer.cancel();
        if (b) {
            answerScroll.setVisibility(View.VISIBLE);
            bPlayAgain.setVisibility(View.VISIBLE);
            bEndQuiz.setVisibility(View.VISIBLE);
        } else {
            answerScroll.setVisibility(View.GONE);
            bPlayAgain.setVisibility(View.GONE);
            bEndQuiz.setVisibility(View.GONE);
        }
    }

    public void visibleGetReadyCountDown(Boolean b) {
        if (b) {
            getReady.setVisibility(View.VISIBLE);
        } else {
            getReady.setVisibility(View.GONE);
        }
    }
}

