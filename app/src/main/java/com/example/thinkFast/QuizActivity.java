package com.example.thinkFast;

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

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Array;
import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {
    private Button mStatistics;
    private Button mPlayQuiz;

    // quiz setting, number of players and what category
    private RadioGroup rgPlayerNum;
    private RadioButton rbPlayerNum;
    private RadioGroup rgCategory;
    private RadioButton rbCategory1;
    private RadioButton rbCategory2;
    private RadioButton rbCategory3;
    private RadioButton rbCategory4;
    private int selCategory;
    private Button bPlay;

    // Question and answers
    private TextView questionText;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private Button ans4;
    private int questionCounter = 0;
    private int questionIndex = 0;
    private final int maxNumOfQuestions = 10;
    private String[] playerAnswersArray = new String[10];
    private String[] correctAnswersArray = new String[10];


    // End screen widget
    private ScrollView answerScroll;
    private LinearLayout playerAnswersColumn;
    private LinearLayout correctAnswersColumn;
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

        // Timer virkni
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

        // Play quiz
        mPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleMenu(false);
                visibleQuizSettings(true);

                // Set name of categories
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
                visibleQuizSettings(false);
                visibleQuizPlay(true);

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
        });

        // Answer button virkni
        for (Button button: new Button[]{ans1,ans2, ans3, ans4}) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (questionCounter < maxNumOfQuestions - 1) {
                        playerAnswersArray[questionCounter] = button.getText().toString();
                        correctAnswersArray[questionCounter] = questions[questionIndex].getCorrectAnswer();
                        getNextQuestion();
                        // If timer resets after a question, it goes here
                        // i = some time
                        //   mProgressbar.setProgress((int)i*100/(5000/1000));
                    } else {
                        visibleQuizPlay(false);
                        visibleEnd(true);
                        showAnswers();
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

    public void showAnswers() {
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

            textView1.setText(playerAnswersArray[i]);
            textView2.setText(correctAnswersArray[i]);

            if (textView1.getText().toString().equals(textView2.getText().toString())) {
                textView1.setTextColor(Color.GREEN);
                textView2.setTextColor(Color.GREEN);
            }
            else {
                textView1.setTextColor(Color.RED);
            }

            playerAnswersColumn.addView(textView1);
            correctAnswersColumn.addView(textView2);
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

        playerAnswersArray = new String[10];
        correctAnswersArray = new String[10];
        playerAnswersColumn.removeAllViewsInLayout();
        correctAnswersColumn.removeAllViewsInLayout();

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
        // Quiz Start screen
        mStatistics = (Button) findViewById(R.id.button_statistics);
        mPlayQuiz = (Button) findViewById(R.id.button_quiz);

        // Timer
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar);

        // Categories
        rbCategory1 = (RadioButton) findViewById(R.id.rb_c1);
        rbCategory2 = (RadioButton) findViewById(R.id.rb_c2);
        rbCategory3 = (RadioButton) findViewById(R.id.rb_c3);
        rbCategory4 = (RadioButton) findViewById(R.id.rb_c4);

        // Setting buttons
        rgPlayerNum = (RadioGroup) findViewById(R.id.rg_players);
        rgCategory = (RadioGroup) findViewById(R.id.rg_categories);
        bPlay = (Button)  findViewById(R.id.bQuizSettings);

        // Questions
        questionText = (TextView)  findViewById(R.id.tQuestion);
        ans1 = (Button) findViewById(R.id.bAns1);
        ans2 = (Button) findViewById(R.id.bAns2);
        ans3 = (Button) findViewById(R.id.bAns3);
        ans4 = (Button) findViewById(R.id.bAns4);

        // End screens
        answerScroll = (ScrollView) findViewById(R.id.answersScroll);
        playerAnswersColumn = (LinearLayout) findViewById(R.id.answerColumn1);
        correctAnswersColumn = (LinearLayout) findViewById(R.id.answerColumn2);
        bPlayAgain = (Button) findViewById(R.id.bPlayAgain);
        bEndQuiz = (Button) findViewById(R.id.bEndQuiz);
    }

    public void visibleMenu(Boolean b) {
        if (b) {
            mStatistics.setVisibility(View.VISIBLE);
            mPlayQuiz.setVisibility(View.VISIBLE);
        }
        else {
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
}