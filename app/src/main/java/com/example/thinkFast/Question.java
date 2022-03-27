package com.example.thinkFast;

import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("ID")
    private long mID;
    @SerializedName("categoryID")
    private int mCategoryID;
    @SerializedName("questionText")
    public String mQuestionText;
    @SerializedName("correctAnswer")
    private String mCorrectAnswer;
    @SerializedName("optionA")
    private String mOptionA;
    @SerializedName("optionB")
    private String mOptionB;
    @SerializedName("optionC")
    private String mOptionC;
    @SerializedName("optionD")
    private String mOptionD;

    public Question(){}

    //Constructor for Question
    public Question(int categoryID, String questionText, String correctAnswer, String optionA, String optionB, String optionC, String optionD) {
        this.mCategoryID = categoryID;
        this.mQuestionText = questionText;
        this.mCorrectAnswer = correctAnswer;
        this.mOptionA = optionA;
        this.mOptionB = optionB;
        this.mOptionC = optionC;
        this.mOptionD = optionD;
    }
    //General functions for the entity
    public long getID() { return mID; }

    public void setID(long ID) { this.mID = ID; }

    public int getCategoryID() { return mCategoryID; }

    public void setCategoryID(int categoryID) { this.mCategoryID = categoryID; }

    public String getQuestionText() { return mQuestionText; }

    public void setQuestionText(String questionText) { this.mQuestionText = questionText; }

    public String getCorrectAnswer() { return mCorrectAnswer; }

    public void setCorrectAnswer(String correctAnswer) { this.mCorrectAnswer = correctAnswer; }

    public String getOptionA() { return mOptionA; }

    public void setOptionA(String optionA) { this.mOptionA = optionA; }

    public String getOptionB() { return mOptionB; }

    public void setOptionB(String optionB) { this.mOptionB = optionB; }

    public String getOptionC() { return mOptionC; }

    public void setOptionC(String optionC) { this.mOptionC = optionC; }

    public String getOptionD() { return mOptionD; }

    public void setOptionD(String optionD) { this.mOptionD = optionD; }
}

