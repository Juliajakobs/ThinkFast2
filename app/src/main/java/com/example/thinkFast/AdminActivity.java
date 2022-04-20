package com.example.thinkFast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinkFast.networking.NetworkCallback;
import com.example.thinkFast.networking.NetworkManager;

import org.w3c.dom.Text;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Button mDeleteButton;
    private Button mStatistics;
    private List<Question> questions;
    private TextView textView;


    //Changes the view to admin view
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //Initializing the textview and making it scrollable
        TextView yourTextView = (TextView) findViewById(R.id.textView3);
        yourTextView.setMovementMethod(new ScrollingMovementMethod());
        //The layout
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.admin_layout);
        textView = (TextView) findViewById(R.id.textView3);

        //Should add a button by each question- not finished
        int buttonX = 735;
        int buttonY = 305;
        //Mig langar að gera forlykkjuna utan um allar spurningarnar í db - hvernig?
        for(int i = 0; i<10; i++){
        Button mDelete = new Button(this);
            mDelete.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
            mDelete.setText(getString(R.string.delete));
            mDelete.setX(buttonX);
            //Láta þetta inn í texviewið frekar og miða við stærðina á því einhvernveginn?
            mDelete.setY(buttonY + i * 160);
            //Þetta virkar en veit ekki hvort þetta megi lol og þetta er ljótt
            //mDelete.setBackgroundColor(getResources().getColor(R.color.purple_500));

            mDelete.setTag("deleteButton " + (i + 1));
            //Should delete question from db- should connect to the tag rather than every button? to know
            //which question is being deleted-not complete- need db
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Should delete selected question from database
                    Toast.makeText(AdminActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
                }
            });

        //add button to the layout
        layout.addView(mDelete);}

        //Gets questions from database and displays them
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getQuestions(new NetworkCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> result) {
                String multiLineText = "";
                questions = result;
                for(int i =0; i<questions.size(); i++ ) {
                    String question = questions.get(i).getQuestionText();
                    multiLineText = multiLineText + question + "\n" + "\n";
                    yourTextView.setText(multiLineText);
                }
            }

            @Override
            public void onFailure(String errorString) {
                Log.e("lol", "Failed to get questions: " + errorString);
            }
        });

        //Placeholder basically
        mStatistics = (Button) findViewById(R.id.button_statistics);
        mStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, StatisticsActivity.class));
            }
        });
        //Delete button - not complete


    }
}
