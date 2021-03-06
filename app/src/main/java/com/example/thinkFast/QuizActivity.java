package com.example.thinkFast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import java.util.List;

public class QuizActivity extends BaseActivity {
    private static final String TAG = "QuizActivity";
    private Button mStatistics;
    private TextView mWelcomeUser;
    private Button bPlay;
    // Question and answers
    private TextView endScore;
    private TextView questionText;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;
    private int questionIndex;
    private int selPlayers;
    private int c_id;

    // Get ready Countdown timer
    private TextView getReady;
    private int counter;

    //Maximum number of questions
    private int maxNumOfQuestions;
    //Arrays that hold the players answers
    private String[] player1AnswersArray = new String[10];
    private String[] player2AnswersArray = new String[10];
    //Arrays that hold the correct answers
    private String[] correct1AnswersArray = new String[10];
    private String[] correct2AnswersArray = new String[10];
    //Variables that hold the players scores
    private int player1Score=0;
    private int player2Score=0;
    private int turn = 0;

    // End screen widget
    private ScrollView answerScroll;
    private LinearLayout answerColumn1;
    private LinearLayout answerColumn2;
    private Button bPlayAgain;
    private Button bEndQuiz;
    private Button bLogOutHeader;

    //Progress bar and timer variables
    private ProgressBar mProgressbar;
    private CountDownTimer mCountDownTimer;
    private int i=0;
    private NetworkManager networkManager;

    //Making a list for the questions in the DB
    private List<Question> questions;
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Bottom navigation
        bottomNavigation(R.id.quiz);
        //All findView connections are in this function
        setFindView();
        // Quiz starts after countdown (ready - set - start quiz)
        getReadyCountDown();
        // Get categoryID and number of players from SetupActivity
        Bundle extras = getIntent().getExtras();
        c_id = extras.getInt("categoryID");
        selPlayers = extras.getInt("selPlayers");
        maxNumOfQuestions = extras.getInt("selNumQuestions");

        //Calling networkManager to get questions from DB
         networkManager = NetworkManager.getInstance(this);
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

        // Log out button in header
        bLogOutHeader = (Button)findViewById(R.id.btn_logout_header);
        // Listener for log out button
        bLogOutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set name and ID of categories and player
                Log.d(TAG,"Logging out");
                logout();
            }
        });
        // Set up progress bar
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressbar.setProgress(i);
        mProgressbar.setVisibility(View.GONE);

        // Set up countdown timer for questions
        // Todo: make timer longer ?
        mCountDownTimer=new CountDownTimer(5000,400) {
            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                mProgressbar.setProgress((int)i*500/(5000/400));
            }
            // If time runs out get next question or end quiz
            @Override
            public void onFinish() {
                //Add "Timed out" as user answer if question is not answered in the time limit
                if (questionIndex < maxNumOfQuestions) {
                    correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                    if(player1AnswersArray[questionIndex]==null)player1AnswersArray[questionIndex]="Timed out";
                    if(selPlayers==2){
                        correct2AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                        if(player2AnswersArray[questionIndex]==null)player2AnswersArray[questionIndex]="Timed out";
                    }
                    getNextQuestion();
                }
                else {
                    visibleQuizPlay(false);
                    visibleEnd(true);
                    showAnswers();
                }
            }
        };
        bLogOutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set name and ID of categories and player
                Log.d(TAG,"LOGGING ot");
                logout();
            }
        });
        // Answer buttons - checks if user answer is correct and gets next question or ends quiz if last question was answered
        for (Button button: new Button[]{ans1,ans2, ans3, ans4}) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selPlayers == 2) {
                        if (turn == 1) {
                            //Getting the players answer from the button
                            player1AnswersArray[questionIndex] = button.getText().toString();
                            correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                            // Calculate score if answer is correct
                            if( player1AnswersArray[questionIndex].equals( correct1AnswersArray[questionIndex]))player1Score+=calculateScore(player1Score,i);
                        } else if (turn == 2) {
                            //Getting the players answer from the button
                            player2AnswersArray[questionIndex] = button.getText().toString();
                            correct2AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                            // Calculate score if answer is correct
                            if( player2AnswersArray[questionIndex].equals(correct2AnswersArray[questionIndex])) player2Score+=calculateScore(player2Score,i);
                        }
                    }
                    else {
                        //Getting the players answer from the button
                        player1AnswersArray[questionIndex] = button.getText().toString();
                        correct1AnswersArray[questionIndex] = questions.get(questionIndex).getCorrectAnswer();
                        // Calculate score if answer is correct
                        if( player1AnswersArray[questionIndex].equals( correct1AnswersArray[questionIndex]))player1Score+=calculateScore(player1Score,i);
                    }

                    if (questionIndex < maxNumOfQuestions-1) {
                        getNextQuestion();

                    } else {
                        if (selPlayers == 2 && turn == 1) {
                            getReadyCountDown();
                        }
                        else {
                            postScore(getAccount(),player1Score);
                            visibleQuizPlay(false);
                            visibleEnd(true);
                            if (selPlayers == 2) {
                                endScore.setText(getResources().getString(R.string.scoreFor2, player1Score, player2Score));
                            } else {
                                endScore.setText(getResources().getString(R.string.scoreFor1, player1Score));
                            }
                            showAnswers();
                        }
                    }
                }
            });
        }

        // End screen buttons functionality
        bPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(QuizActivity.this,SetupActivity.class));
                Intent in = new Intent(QuizActivity.this, SetupActivity.class);
                in.putExtra("wasLoggedIn",true);
                startActivity(in);
            }
        });
        //Quiz ends
        bEndQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(QuizActivity.this,SetupActivity.class);
                in.putExtra("wasLoggedIn",false);
                startActivity(in);
            }
        });
    }
    //Function for the countdown at the start
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
                    else getReady.setText(sharedpreferences.getString(AccountActivity.uName, "null")); // should be name
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
    //Function that displays the players answers vs the correct answers at the end
    public void showAnswers() {
        Log.d(TAG,"selPlayers: "+selPlayers);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        if (selPlayers == 1) {
            TextView playerAns = new TextView(this);
            TextView correctAns = new TextView(this);

            playerAns.setLayoutParams(param);
            correctAns.setLayoutParams(param);

            playerAns.setText(sharedpreferences.getString(AccountActivity.uName, "null"));
            correctAns.setText(R.string.correctAnswers);

            playerAns.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            correctAns.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            playerAns.setTextColor(Color.BLACK);
            correctAns.setTextColor(Color.BLACK);

            playerAns.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            correctAns.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            answerColumn1.addView(playerAns);
            answerColumn2.addView(correctAns);

            for (int i = 0; i < maxNumOfQuestions; i++) {
                // Add new textView dynamically to answerColumn 1 and 2
                TextView textView1 = new TextView(this);
                TextView textView2 = new TextView(this);

                textView1.setLayoutParams(param);
                textView2.setLayoutParams(param);

                textView1.setText(player1AnswersArray[i]);
                textView2.setText(correct1AnswersArray[i]);

                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                //If answer is correct --> then it's green else red
                if (textView1.getText().toString().equals(textView2.getText().toString())) {
                    textView1.setTextColor(Color.GREEN);
                    textView2.setTextColor(Color.GREEN);
                } else {
                    textView1.setTextColor(Color.RED);
                    textView2.setTextColor(Color.BLACK);
                }

                textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                answerColumn1.addView(textView1);
                answerColumn2.addView(textView2);
            }
            Log.d("myapp","playerScore: "+player1Score);
        }
        else {
            TextView player1Ans = new TextView(this);
            TextView player2Ans = new TextView(this);

            player1Ans.setLayoutParams(param);
            player2Ans.setLayoutParams(param);

            player1Ans.setText(R.string.p1);
            player2Ans.setText(R.string.p2);

            player1Ans.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            player2Ans.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            player1Ans.setTextColor(Color.BLACK);
            player2Ans.setTextColor(Color.BLACK);

            player1Ans.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            player2Ans.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            answerColumn1.addView(player1Ans);
            answerColumn2.addView(player2Ans);

            for (int i = 0; i < maxNumOfQuestions; i++) {
                // Add new textView dynamically to answerColumn 1 and 2
                TextView textView1 = new TextView(this);
                TextView textView2 = new TextView(this);

                textView1.setLayoutParams(param);
                textView2.setLayoutParams(param);

                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                textView1.setText(player1AnswersArray[i]);
                textView2.setText(player2AnswersArray[i]);
                Log.d(TAG,"PlayerArray: "+player1AnswersArray[i]+" correctAnswers1: "+correct1AnswersArray[i]);
                ////If answer is correct --> then it's green else red
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

                textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                answerColumn1.addView(textView1);
                answerColumn2.addView(textView2);
            }
        }
    }
    //Function that gets next question
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

    // Resets counter after each question/quiz
    public void resetCounter(){
        i=0;
        mCountDownTimer.cancel();
        mCountDownTimer.start();
        mProgressbar.setProgress(i);
    }
    //Calculate score
    public int calculateScore(int playerScore, int i){
        // Timer bonus scores
        if(i<=3)  playerScore = 100;
        else if(i<=6) playerScore = 75;
        else if(i<=9) playerScore = 50;
        else if(i<=12) playerScore = 25;
        return playerScore;
    }
    //Function that handles all findViewById
    public void setFindView() {
        // User stuff
        mWelcomeUser = (TextView) findViewById(R.id.velkominn_user);
        // Quiz Start screen
        mStatistics = (Button) findViewById(R.id.button_statistics);
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
        endScore = (TextView) findViewById(R.id.endScores);
        answerScroll = (ScrollView) findViewById(R.id.answersScroll);
        answerColumn1 = (LinearLayout) findViewById(R.id.answerColumn1);
        answerColumn2 = (LinearLayout) findViewById(R.id.answerColumn2);
        bPlayAgain = (Button) findViewById(R.id.bPlayAgain);
        bEndQuiz = (Button) findViewById(R.id.bEndQuiz);
    }

    public void visibleQuizSettings(Boolean b) {
        if (b) {
            bPlay.setVisibility(View.VISIBLE);
        }
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
            endScore.setVisibility(View.VISIBLE);
            answerScroll.setVisibility(View.VISIBLE);
            bPlayAgain.setVisibility(View.VISIBLE);
            bEndQuiz.setVisibility(View.VISIBLE);
        } else {
            endScore.setVisibility(View.GONE);
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
    private void postScore(Account account, int userScore){
        String score = Integer.toString(userScore);
        networkManager.postScore(account.getUsername(),score);
    }

    private Account getAccount(){
       String userName = sharedpreferences.getString(AccountActivity.uName,"null");
       String password = sharedpreferences.getString(AccountActivity.pWord,"null");
       String email = "";
       String name = "";
       Account account = new Account(userName,password,email,name,false);
       return account;
    }
}

