package com.example.thinkFast;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {
    private Button mStatistics;
    private Button mPlayQuiz;

    // quiz setting, number of players and what category
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
    private TextView questionText;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;
    private int questionCounter = 0;
    private int questionIndex = 0;
    private int selCategory=-1;
    // Id's for category and player radio buttons
    private static final int RB1_ID = 1;
    private static final int RB2_ID = 2;
    private static final int RB3_ID = 3;
    private static final int RB4_ID = 4;
    private static final int RBP1_ID = 1;
    private static final int RBP2_ID = 2;



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

            new Question(2, "Which option is an animal?", "Dog", "Pillow", "Water", "Dog", "Iris"),
            new Question(2, "Which option is en animal?", "Cat", "Pillow", "Grass", "Cat", "Aloe"),

            new Question(3, "Which option is a country?", "Iceland", "Africa", "Asia", "Iceland", "Europe"),
            new Question(3, "Which option is a country?", "GreenLand", "Africa", "Asia", "Greenland", "Europe"),

            new Question(4, "Which option is a color?", "Blue", "Blueberry", "Water", "Blue", "Iris"),
            new Question(4, "Which option is a color?", "Green", "Avocado", "Grass", "Green", "Aloe")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // Quiz or stats
        mStatistics = (Button) findViewById(R.id.button_statistics);
        mPlayQuiz = (Button) findViewById(R.id.button_quiz);
        // Set time for progress bar
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressbar.setProgress(i);
        mProgressbar.setVisibility(View.GONE);
        mCountDownTimer=new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                mProgressbar.setProgress((int)i*100/(5000/1000));
            }
            @Override
            public void onFinish() {
                //TODO: Redirect to next question
            }
        };

        mPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatistics.setVisibility(View.GONE);
                mPlayQuiz.setVisibility(View.GONE);

                rgPlayerNum.setVisibility(View.VISIBLE);
                rgCategory.setVisibility(View.VISIBLE);
                bPlay.setVisibility(View.VISIBLE);

                // Set name and ID of categories
                rbCategory1 = (RadioButton) findViewById(R.id.rb_c1);
                rbCategory1.setId(RB1_ID);
                rbCategory2 = (RadioButton) findViewById(R.id.rb_c2);
                rbCategory2.setId(RB2_ID);
                rbCategory3 = (RadioButton) findViewById(R.id.rb_c3);
                rbCategory3.setId(RB3_ID);
                rbCategory4 = (RadioButton) findViewById(R.id.rb_c4);
                rbCategory4.setId(RB4_ID);

                rbPlayer1=(RadioButton)findViewById(R.id.rg_p1);
                rbPlayer1.setId(RBP1_ID);
                rbPlayer2=(RadioButton)findViewById(R.id.rg_p2);
                rbPlayer2.setId(RBP2_ID);

                rbCategory1.setText(categories[0].getCategoryName());
                rbCategory2.setText(categories[1].getCategoryName());
                rbCategory3.setText(categories[2].getCategoryName());
                rbCategory4.setText(categories[3].getCategoryName());
            }
        });

        // Quiz settings

        rgPlayerNum = (RadioGroup) findViewById(R.id.rg_players);
        rgCategory = (RadioGroup) findViewById(R.id.rg_categories);
        bPlay = (Button)  findViewById(R.id.bQuizSettings);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get int of selected radio button from radio group
                int selPlayers=rgPlayerNum.getCheckedRadioButtonId();
                selCategory = rgCategory.getCheckedRadioButtonId();
                Log.d("myapp", "onClick: "+selPlayers);
                if(selCategory == -1) Toast.makeText(getApplicationContext(),
                        "You have to choose a category", Toast.LENGTH_SHORT).show();
                else {
                    // Hide setting buttons
                    rgPlayerNum.setVisibility(View.GONE);
                    rgCategory.setVisibility(View.GONE);
                    bPlay.setVisibility(View.GONE);


                    // Make question text visible along with answer buttons
                    questionText.setVisibility(View.VISIBLE);
                    ans1.setVisibility(View.VISIBLE);
                    ans2.setVisibility(View.VISIBLE);
                    ans3.setVisibility(View.VISIBLE);
                    ans4.setVisibility(View.VISIBLE);

                    // Only work with questions from chosen category
                    switch (selCategory) {
                        case 1:
                            questionIndex = 0;
                            break;
                        case 2:
                            questionIndex = 2;
                            break;
                        case 3:
                            questionIndex = 4;
                            break;
                        case 4:
                            questionIndex = 6;
                            break;
                    }

                    // Make text reflect the right question
                    questionText.setText(questions[questionIndex].getQuestionText());
                    ans1.setText(questions[questionIndex].getOptionA());
                    ans2.setText(questions[questionIndex].getOptionB());
                    ans3.setText(questions[questionIndex].getOptionC());
                    ans4.setText(questions[questionIndex].getOptionD());
                    mProgressbar.setVisibility(View.VISIBLE);
                    mCountDownTimer.start();
                }
            }
        });

        // Find question stuff in app
        questionText = (TextView)  findViewById(R.id.tQuestion);
        ans1 = (Button) findViewById(R.id.bAns1);
        ans2 = (Button) findViewById(R.id.bAns2);
        ans3 = (Button) findViewById(R.id.bAns3);
        ans4 = (Button) findViewById(R.id.bAns4);


        for (Button button : Arrays.asList(ans1, ans2, ans3, ans4)) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("myapp","value: "+selCategory);
                    if (questionCounter < 1 ) {
                        getNextQuestion();
                        i=-1;
                        // If timer resets after a question, it goes here
                        // i = some time
                     //   mProgressbar.setProgress((int)i*100/(5000/1000));
                    } else {
                        resetQuiz();
                    }
                }
            });
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
/*
        playerAnswersArray = new String[10];
        correctAnswersArray = new String[10];
        playerAnswersColumn.removeAllViewsInLayout();
        correctAnswersColumn.removeAllViewsInLayout();*/

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
}