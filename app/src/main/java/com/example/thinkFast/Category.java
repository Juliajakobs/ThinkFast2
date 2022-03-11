package com.example.thinkFast;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private long ID;
    private String categoryName;
    private int categoryID;

    private List<Question> questions= new ArrayList<>();

    public Category(String categoryName, int categoryID) {
        this.categoryName = categoryName;
        this.categoryID = categoryID;
    }

    public Category() {}

    public List<Question> getQuestions() { return questions; }

    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public long getID() { return ID; }

    public void setID(long ID) { this.ID = ID; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getCategoryID() { return categoryID; }

    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
}