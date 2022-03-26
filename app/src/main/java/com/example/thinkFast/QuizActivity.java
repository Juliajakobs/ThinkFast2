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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private Button mStatistics;
    private TextView mWelcomeUser;


    private static final String TAG = "QuizActivity";

    private Button bPlay;

    // Question and answers
    private TextView questionText;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;
    private Button mScoreboard;
    private int resetIndex;
    private int questionIndex;
    private int selPlayers;
    private int c_id;

    // Get ready Countdown timer
    private TextView getReady;
    private int counter;


    private final int maxNumOfQuestions = 5;
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


    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // Allt findView tengingar dotið er gert i thetta function
        setFindView();
        // Quiz starts after countdown (ready - set - start quiz)
        getReadyCountDown();
        // Get categoryID and number of players from SetupActivity
        Bundle extras = getIntent().getExtras();
        c_id = extras.getInt("categoryID");
        selPlayers = extras.getInt("selPlayers");

        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getQuestionsByCategory(c_id,new NetworkCallback<List<Question>>() {
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

      // mWelcomeUser = (TextView) findViewById(R.id.velkominn_user);
        // Set up progress bar
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressbar.setProgress(i);
        mProgressbar.setVisibility(View.GONE);

        // Set time for progress bar
        mProgressbar.setProgress(i);

        // Set up countdown timer for questions
        mCountDownTimer=new CountDownTimer(5000,400) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressbar.setProgress((int)i*500/(5000/400));
            }
            @Override
            public void onFinish() {
                //Add "Timed out" as user answer if question is not answered in the time limit
                if (questionIndex < maxNumOfQuestions-1) {
                    correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                    if(player1AnswersArray[questionIndex]==null)player1AnswersArray[questionIndex]="Timed out";
                    if(selPlayers==2){
                        correct2AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                        if(player2AnswersArray[questionIndex]==null)player2AnswersArray[questionIndex]="Timed out";
                    }
                    getNextQuestion();
                }
                else {
                    showAnswers();
                }

            }
        };

        mScoreboard = (Button) findViewById(R.id.btn_scoreboard);
        mScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizActivity.this, ScoreboardActivity.class));
            }
        });

        //Gera StatisticsActivity? eða er ehv að gera það
      /* mStatistics.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(QuizActivity.this, StatisticsActivity.class));
           }
           });*/


        // Answer button virkni
        for (Button button: new Button[]{ans1,ans2, ans3, ans4}) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selPlayers == 2) {
                        if (turn == 1) {
                            player1AnswersArray[questionIndex] = button.getText().toString();
                            correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                            Log.d(TAG, "player 1 ans " + player1AnswersArray[questionIndex]);
                            // Calculate score if answer is correct
                            if( player1AnswersArray[questionIndex].equals( correct1AnswersArray[questionIndex]))player1Score+=calculateScore(player1Score,i);
                        } else if (turn == 2) {
                            player2AnswersArray[questionIndex] = button.getText().toString();
                            correct2AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                            Log.d(TAG, "player 2 ans " + player2AnswersArray[questionIndex]);
                            // Calculate score if answer is correct
                            if( player2AnswersArray[questionIndex].equals(correct2AnswersArray[questionIndex])) player2Score+=calculateScore(player2Score,i);
                        }
                    }
                    else {
                        player1AnswersArray[questionIndex] = button.getText().toString();
                        correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                        // Calculate score if answer is correct
                        Log.d(TAG, "player 1 ans " + player1AnswersArray[questionIndex]);
                        if( player1AnswersArray[questionIndex].equals( correct1AnswersArray[questionIndex]))player1Score+=calculateScore(player1Score,i);
                    }

                    Log.d(TAG, "question number " + questionIndex);
                    if (questionIndex < maxNumOfQuestions-1) {
                        getNextQuestion();

                    } else {
                        if (selPlayers == 2 && turn == 1) {
                            getReadyCountDown();
                        }
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
                    else getReady.setText("placeholder"); // ætti að vera name
                }
                counter--;
            }
            @Override
            public void onFinish() {
                visibleGetReadyCountDown(false);
                visibleQuizPlay(true);
                resetCounter();
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
        mProgressbar.setVisibility(View.VISIBLE);
        mCountDownTimer.start();
    }

    public void showAnswers() {
        Log.d(TAG,"selPlayers: "+selPlayers);
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

           // playerAns.setText(Name);
            correctAns.setText(R.string.correctAnswers);

            playerAns.setTextColor(Color.BLACK);
            correctAns.setTextColor(Color.BLACK);

            answerColumn1.addView(playerAns);
            answerColumn2.addView(correctAns);

            for (int i = 0; i < maxNumOfQuestions; i++) {
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

            for (int i = 0; i < maxNumOfQuestions; i++) {
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
                Log.d(TAG,"PLayerayrray: "+player1AnswersArray[i]+" correctanswers1: "+correct1AnswersArray[i]);
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
        mProgressbar.setVisibility(View.VISIBLE);
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
       // mPlayQuiz = (Button) findViewById(R.id.button_quiz);

        // Timer
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar);

        // CountDown for getting ready
        getReady = (TextView) findViewById(R.id.getReadyCountDown);
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
        }
        else {
            mWelcomeUser.setVisibility(View.GONE);
            mStatistics.setVisibility(View.GONE);
        }
    }

    public void visibleQuizSettings(Boolean b) {
        if (b) {
            bPlay.setVisibility(View.VISIBLE);
        }
     /*   else {
            rgPlayerNum.setVisibility(View.GONE);
            rgCategory.setVisibility(View.GONE);
            bPlay.setVisibility(View.GONE);
        }*/
    }

    public void visibleQuizPlay(Boolean b) {
        if (b) {
            questionText.setVisibility(View.VISIBLE);
            ans1.setVisibility(View.VISIBLE);
            ans2.setVisibility(View.VISIBLE);
            ans3.setVisibility(View.VISIBLE);
            ans4.setVisibility(View.VISIBLE);
        } else {
            questionText.setVisibility(View.GONE);
            ans1.setVisibility(View.GONE);
            ans2.setVisibility(View.GONE);
            ans3.setVisibility(View.GONE);
            ans4.setVisibility(View.GONE);
        }
    }

    public void visibleEnd(Boolean b) {
        mProgressbar.setVisibility(View.GONE);
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

