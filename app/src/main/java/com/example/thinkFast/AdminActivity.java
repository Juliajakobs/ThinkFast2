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

public class AdminActivity extends BaseActivity {
    private static final String TAG = "AdminActivity";
    private List<Question> questions;
    private TextView textView;
    private Button bLogOutHeader;



    //Changes the view to admin view
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //Initializing the textview
        TextView yourTextView = (TextView) findViewById(R.id.textView3);
        //The layout
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.admin_layout);
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



        //Adds a delete button by each question
        int buttonX = 735;
        int buttonY = 305;
        //Harðkóðaður fjöldinn af spurningum
        for(int i = 0; i<50; i++){
        Button mDelete = new Button(this);
            mDelete.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
            mDelete.setText(getString(R.string.delete));
            mDelete.setX(buttonX);
            //Positioning the buttons-want to do it so it corresponds to question size
            mDelete.setY(buttonY + i * 160);
            mDelete.setTag("deleteButton " + (i + 1));
            //Should delete question from db- should connect to the tag rather than every button? to know
            //which question is being deleted-not complete- need db
            int finalI = i;
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    networkManager.getQuestions(new NetworkCallback<List<Question>>() {
                        @Override
                        public void onSuccess(List<Question> result) {
                            questions = result;
                            Object tag =  mDelete.getTag();
                            long id = questions.get(finalI).getID();
                            Log.d("lol", "þetta er tag" + tag);
                        }

                        @Override
                        public void onFailure(String errorString) {
                            Log.e("lol", "Failed to get questions: " + errorString);
                        }
                    });
                    //Should delete selected question from database
                    Toast.makeText(AdminActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
                }
            });

        //add button to the layout
        layout.addView(mDelete);
        }
        // Listener for log out button
        bLogOutHeader = (Button)findViewById(R.id.btn_logout_header);
        bLogOutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Logging out admin");
                logout();
            }
        });

    }
}
