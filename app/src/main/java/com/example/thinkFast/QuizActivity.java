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

public class QuizActivity extends AppCompatActivity {
    private Button mStatistics;
    private Button mPlayQuiz;
    private TextView mWelcomeUser;
    private String Name;
    private String Email;
    private String UserName;

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
    private int questionCounter = 0;
    private int questionIndex = 0;
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

    // dummy data - categories
    private final Category[] categories = new Category[]{
            new Category("Sport", 1),
            new Category("Science", 2),
            new Category("Geography", 3),
            new Category("General", 4)
    };

    // dummy data - questions
    private final Question[] questions = new Question[]{
            new Question(1, "Which option is a sport?", "Soccer", "Chess", "Poker", "Soccer", "Painting"),
            new Question(1, "Which option is a sport?", "Football", "Drawing", "Music", "Football", "Crafting"),
            new Question(1, "Which option is a sport?", "Swimming", "Chess", "Poker", "Swimming", "Painting"),
            new Question(1, "Which option is a sport?", "Baseball", "Drawing", "Music", "Baseball", "Crafting"),
            new Question(1, "Which option is a sport?", "Hockey", "Chess", "Poker", "Hockey", "Painting"),
            new Question(1, "Which option is a sport?", "Basketball", "Drawing", "Music", "Basketball", "Crafting"),
            new Question(1, "Which option is a sport?", "Golf", "Chess", "Poker", "Golf", "Painting"),
            new Question(1, "Which option is a sport?", "Figure Skating", "Drawing", "Music", "Figure Skating", "Crafting"),
            new Question(1, "Which option is a sport?", "Volleyball", "Chess", "Poker", "Volleyball", "Painting"),
            new Question(1, "Which option is a sport?", "Martial Arts", "Drawing", "Music", "Martial Arts", "Crafting"),

            new Question(2, "Which option is an animal?", "Dog", "Pillow", "Water", "Dog", "Iris"),
            new Question(2, "Which option is en animal?", "Cat", "Bed", "Grass", "Cat", "Aloe"),
            new Question(2, "Which option is an animal?", "Bird", "Pillow", "Water", "Bird", "Iris"),
            new Question(2, "Which option is en animal?", "Fish", "Bed", "Grass", "Fish", "Aloe"),
            new Question(2, "Which option is an animal?", "Llama", "Pillow", "Water", "Llama", "Iris"),
            new Question(2, "Which option is en animal?", "Hippopotamus", "Bed", "Grass", "Hippopotamus", "Aloe"),
            new Question(2, "Which option is an animal?", "Swan", "Pillow", "Water", "Swan", "Iris"),
            new Question(2, "Which option is en animal?", "Rhinoceros", "Bed", "Grass", "Rhinoceros", "Aloe"),
            new Question(2, "Which option is an animal?", "Hamster", "Pillow", "Water", "Hamster", "Iris"),
            new Question(2, "Which option is en animal?", "Human", "Bed", "Grass", "Human", "Aloe"),

            new Question(3, "Which option is a country?", "Iceland", "Africa", "Asia", "Iceland", "Europe"),
            new Question(3, "Which option is a country?", "GreenLand", "Europe", "Africa", "Greenland", "Asia"),
            new Question(3, "Which option is a country?", "Finland", "Africa", "Asia", "Finland", "Europe"),
            new Question(3, "Which option is a country?", "Norway", "Europe", "Africa", "Norway", "Asia"),
            new Question(3, "Which option is a country?", "Denmark", "Africa", "Asia", "Denmark", "Europe"),
            new Question(3, "Which option is a country?", "Poland", "Europe", "Africa", "Poland", "Asia"),
            new Question(3, "Which option is a country?", "Germany", "Africa", "Asia", "Germany", "Europe"),
            new Question(3, "Which option is a country?", "Netherlands", "Europe", "Africa", "Netherlands", "Asia"),
            new Question(3, "Which option is a country?", "France", "Africa", "Asia", "France", "Europe"),
            new Question(3, "Which option is a country?", "Spain", "Europe", "Africa", "Spain", "Asia"),

            new Question(4, "Which option is a color?", "Blue", "Blueberry", "Water", "Blue", "Iris"),
            new Question(4, "Which option is a color?", "Green", "Avocado", "Grass", "Green", "Aloe"),
            new Question(4, "Which option is a color?", "Red", "Blueberry", "Water", "Red", "Iris"),
            new Question(4, "Which option is a color?", "Yellow", "Avocado", "Grass", "Yellow", "Aloe"),
            new Question(4, "Which option is a color?", "Black", "Blueberry", "Water", "Black", "Iris"),
            new Question(4, "Which option is a color?", "White", "Avocado", "Grass", "White", "Aloe"),
            new Question(4, "Which option is a color?", "Purple", "Blueberry", "Water", "Purple", "Iris"),
            new Question(4, "Which option is a color?", "Brown", "Avocado", "Grass", "Brown", "Aloe"),
            new Question(4, "Which option is a color?", "Beige", "Blueberry", "Water", "Beige", "Iris"),
            new Question(4, "Which option is a color?", "Orange", "Avocado", "Grass", "Orange", "Aloe")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Allt findView tengingar dotið er gert i thetta function
        setFindView();

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

        mCountDownTimer=new CountDownTimer(100000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressbar.setProgress((int)i*100/(5000/1000));
            }
            @Override
            public void onFinish() {
                //TODO: Calculate points
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
                rbCategory1.setId(RB1_ID);
                rbCategory2.setId(RB2_ID);
                rbCategory3.setId(RB3_ID);
                rbCategory4.setId(RB4_ID);

                rbPlayer1.setId(RBP1_ID);
                rbPlayer2.setId(RBP2_ID);

                rbCategory1.setText(categories[0].getCategoryName());
                rbCategory2.setText(categories[1].getCategoryName());
                rbCategory3.setText(categories[2].getCategoryName());
                rbCategory4.setText(categories[3].getCategoryName());
            }
        });

        // Play quiz button eftir settings dót virkni
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get int of selected radio button from radio group
                selCategory = rgCategory.getCheckedRadioButtonId();
                selPlayers = rgPlayerNum.getCheckedRadioButtonId();
                Log.d("myapp", "onClick: "+ selPlayers);
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
                    Log.d("myapp","value: " + selCategory);
                    Log.d("myapp","value: " + turn);
                    Log.d("myapp","value: " + questionCounter);
                    // Save answers in appropriate array
                    if (selPlayers == 2) {
                        if (turn == 1) {
                            player1AnswersArray[questionCounter] = button.getText().toString();
                            correct1AnswersArray[questionCounter] = questions[questionIndex].getCorrectAnswer();
                        } else if (turn == 2) {
                            player2AnswersArray[questionCounter] = button.getText().toString();
                            correct2AnswersArray[questionCounter] = questions[questionIndex].getCorrectAnswer();
                        }
                    }
                    else {
                        player1AnswersArray[questionCounter] = button.getText().toString();
                        correct1AnswersArray[questionCounter] = questions[questionIndex].getCorrectAnswer();
                    }

                    if (questionCounter < maxNumOfQuestions-1) {
                        getNextQuestion();
                        // If timer resets after a question, it goes here
                        // i = some time
                        //   mProgressbar.setProgress((int)i*100/(5000/1000));
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
                    else getReady.setText(Name);
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
        if (questionCounter > 0) {
            questionCounter = 0;
        }

        // Only work with questions from chosen category
        switch (selCategory){
            case 1:
                questionIndex = 0;
                break;
            case 2:
                questionIndex = 10;
                break;
            case 3:
                questionIndex = 20;
                break;
            case 4:
                questionIndex = 30;
                break;
        }

        // Make text reflect the right question
        questionText.setText(questions[questionIndex].getQuestionText());
        ans1.setText(questions[questionIndex].getOptionA());
        ans2.setText(questions[questionIndex].getOptionB());
        ans3.setText(questions[questionIndex].getOptionC());
        ans4.setText(questions[questionIndex].getOptionD());
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

            for (int i = 0; i < maxNumOfQuestions-1; i++) {
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

            for (int i = 0; i < maxNumOfQuestions-1; i++) {
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
        questionCounter += 1;
        questionIndex += 1;
        questionText.setText(questions[questionIndex].getQuestionText());
        ans1.setText(questions[questionIndex].getOptionA());
        ans2.setText(questions[questionIndex].getOptionB());
        ans3.setText(questions[questionIndex].getOptionC());
        ans4.setText(questions[questionIndex].getOptionD());
    }

    public void resetQuiz() {
        questionCounter = 0;
        questionIndex = 0;
        turn = 0;

        player1AnswersArray = new String[10];
        player2AnswersArray = new String[10];
        correct1AnswersArray = new String[10];
        correct2AnswersArray = new String[10];
        answerColumn1.removeAllViewsInLayout();
        answerColumn2.removeAllViewsInLayout();

        i = 0;
        mProgressbar.setProgress(i);
        mCountDownTimer=new CountDownTimer(100000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressbar.setProgress((int)i*100/(5000/1000));
            }
            @Override
            public void onFinish() {
                //Display next Question

            }
        };
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
        rbPlayer1=(RadioButton)findViewById(R.id.rg_p1);
        rbPlayer2=(RadioButton)findViewById(R.id.rg_p2);

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