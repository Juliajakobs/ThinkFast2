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

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    private Button mStatistics;
    private Button mPlayQuiz;
    private TextView mWelcomeUser;

    // quiz setting, number of players and what category
    private RadioGroup rgPlayerNum;
    private RadioButton rbPlayerNum;
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
    private int catNum = 0;

    private ProgressBar mProgressbar;
    private CountDownTimer mCountDownTimer;
    private int i=0;

    // dummy data - categories
    private Category[] categories = new Category[]{
            new Category("Sport", 1),
            new Category("Science", 2),
            new Category("Geography", 3),
            new Category("General", 4)
    };

    // dummy data - questions
    private Question[] questions = new Question[]{
            new Question(1, "Which option is a sport?", "Soccer", "Chess", "Poker", "Soccer", "Painting"),
            new Question(1, "Which option is a sport?", "Football", "Drawing", "Music", "Football", "Crafting"),

            new Question(2, "Which option is an animal?", "Dog", "Pillow", "Water", "Blue", "Iris"),
            new Question(2, "Which option is en animal?", "Cat", "Pillow", "Grass", "Green", "Aloe"),

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
        //Welcome user
        mWelcomeUser = (TextView) findViewById(R.id.velkominn_user);
        // Timer
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressbar.setProgress(i);
        //Getting information about logged in user from AccountActivity
        Bundle extras = getIntent().getExtras();
        String Name = extras.getString("name");
        String Email = extras.getString("email");
        String UserName = extras.getString("username");
        mWelcomeUser.setText("Welcome " + Name);
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

        mPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatistics.setVisibility(View.GONE);
                mPlayQuiz.setVisibility(View.GONE);
                mWelcomeUser.setVisibility(View.GONE);


                rgPlayerNum.setVisibility(View.VISIBLE);
                rgCategory.setVisibility(View.VISIBLE);
                bPlay.setVisibility(View.VISIBLE);

                // Set name of categories
                rbCategory1 = (RadioButton) findViewById(R.id.rb_c1);
                rbCategory2 = (RadioButton) findViewById(R.id.rb_c2);
                rbCategory3 = (RadioButton) findViewById(R.id.rb_c3);
                rbCategory4 = (RadioButton) findViewById(R.id.rb_c4);

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
                // Get selected radio button from radio group
                int selPlayerNum = rgPlayerNum.getCheckedRadioButtonId();
                int selCategory = rgCategory.getCheckedRadioButtonId();


                // if button from both groups has been selected, "start quiz"
                if (selCategory != -1 && selPlayerNum != -1) {
                    // Find radio button by returned id
                    // ekki notað alveg strax
                    rbPlayerNum = (RadioButton) findViewById(selPlayerNum);
                    chosenCategory = (RadioButton) findViewById(selCategory);
                    Log.d("mayapp","value: "+chosenCategory);

                    // Hide setting buttons
                    rgPlayerNum.setVisibility(View.GONE);
                    rgCategory.setVisibility(View.GONE);
                    bPlay.setVisibility(View.GONE);

                    // Start quiz, display question text and buttons
                    // Make them visible
                    questionText.setVisibility(View.VISIBLE);
                    ans1.setVisibility(View.VISIBLE);
                    ans2.setVisibility(View.VISIBLE);
                    ans3.setVisibility(View.VISIBLE);
                    ans4.setVisibility(View.VISIBLE);

                    // make sure we are in the right category
                    switch(catNum) {
                        case 1:
                            catNum = 2;
                            break;
                        case 2:
                            catNum = 4;
                            break;
                        case 3:
                            catNum = 6;
                            break;
                        default:
                            break;
                    }

                    // Make text reflect the right question
                    questionText.setText(questions[catNum+questionCounter].getQuestionText());
                    ans1.setText(questions[catNum+questionCounter].getOptionA());
                    ans2.setText(questions[catNum+questionCounter].getOptionB());
                    ans3.setText(questions[catNum+questionCounter].getOptionC());
                    ans4.setText(questions[catNum+questionCounter].getOptionD());
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


        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }



    /*
    public static Question[] getNextQuestion(Question[] questions, Question q, int count, int cat) {
        Account newArray[] = new Account[n + 1];
        if (count)
            newArray[i] = accounts[i];
        newArray[n] = acc;
        return newArray;
    }*/
}