package com.example.thinkFast;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Category {
    @SerializedName("ID")
    private long mID;
    @SerializedName("categoryName")
    private String mCategoryName;
    @SerializedName("categoryID")
    private int mCategoryID;

    private List<Question> questions= new ArrayList<>();

    //Constructor for Category
    public Category(String categoryName, int categoryID) {
        this.mCategoryName = categoryName;
        this.mCategoryID = categoryID;
    }
    //General functions for the entity
    public Category() {}

    public List<Question> getQuestions() { return questions; }

    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public long getID() { return mID; }

    public void setID(long ID) { this.mID = ID; }

    public String getCategoryName() { return mCategoryName; }

    public void setCategoryName(String categoryName) { this.mCategoryName = categoryName; }

    public int getCategoryID() { return mCategoryID; }

    public void setCategoryID(int categoryID) { this.mCategoryID = categoryID; }
}