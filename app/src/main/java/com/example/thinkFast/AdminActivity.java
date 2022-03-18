package com.example.thinkFast;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    private Button mDeleteButton;

    private Question[] mQuestions = new Question[]{
            new Question(0, "Who portrays the character 'Paul' in the film Dune(2021)?", "Timothée Chalamet", "Jason Momoa", "Timothée Chalamet", "Harry Styles", "Dave Bautista"),
            new Question(0, "Which member of the band 'One Direction' famously left the band on the 25th of March 2015?", "Zayn", "Harry", "Louis", "Neil", "Zayn"),
            new Question(0, "Who won 'Best Actor' at the Oscars in 2021?", "Anthony Hopkins", "Leonardo DiCaprio", "Anthony Hopkins", "Bob", "Adam Sandler"),
            new Question(0, "How many seasons are there of the show 'The Bold and the Beautiful'?", "35", "30", "120", "35", "45"),
            new Question(0, "What is the name of Wario‘s sidekick in the Super Mario franchise?","Waluigi", "Luigi", "Waluigi", "Bowser", "Toad"),
            new Question(0, "What spell in Harry potter can unlock doors? ", "Alohamora", "Alohamora", "Lumos", "Wingardium Leviosa", "Expelliarmus"),
            new Question(0, "Which British actor will play Batman in the upcoming reboot?", "Robert Pattinson", "Robert Pattinson", "Ricky Gervais", "Martin Freeman", "James Buckley"),
            new Question(0, "Which Harry Potter book does not exist? Harry Potter and.. ", "the Sparkling Wands", "the Philosopher's Stone", "the Order of the Phoenix", "the Half-Blood Prince", "the Sparkling Wands"),
            new Question(0, "Who wrote 'The Lord of the Rings'?", "J.R.R Tolkien", "Dan Brown", "J.K Rowling", "J.R.R Tolkien", "Milan Kundera"),
            new Question(0, "Who of the following has not been a judge on 'The Great British Bake Off'?", "Jenny Omars", "Jenny Omars", "Prue Leith", "Paul Hollywood", "Mary Berry"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        TextView yourTextView = (TextView) findViewById(R.id.textView3); //however your textview is defined in your xml
        //Displaya dummy data spurningar inn í admin
        String multiLineText = "";
        for(int i =0; i< mQuestions.length; i++) {
            String question = mQuestions[i].getQuestionText();
            multiLineText = multiLineText + question + "\n" + "\n";
            yourTextView.setText(multiLineText);
        }

        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, R.string.delete, Toast.LENGTH_SHORT).show();
            }
        });
    }
}