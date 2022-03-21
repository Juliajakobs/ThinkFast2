package com.example.thinkFast;

public class Scores {
    private Account account;
    private int score;


    public Scores(Account account, int score) {
        this.account = account;
        this.score = score;
    }

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
