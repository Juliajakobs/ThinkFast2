package com.example.thinkFast;

import com.google.gson.annotations.SerializedName;

public class Scores {
    @SerializedName("account")
    private Account account;
    @SerializedName("score")
    private int score;

    //Constructor for scores
    public Scores(Account account, int score) {
        this.account = account;
        this.score = score;
    }
    //General functions for the entity
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
