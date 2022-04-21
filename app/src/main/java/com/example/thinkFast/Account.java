package com.example.thinkFast;

import com.google.gson.annotations.SerializedName;
public class Account {
    @SerializedName("username")
    private String mUsername;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("name")
    private String mName;
    @SerializedName("isAdmin")
    private Boolean mIsAdmin;

  //  List<Scores> scores = new ArrayList<>();

  //  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
 //   Statistics statistics;

    //Constructor for Account
    public Account(String username, String password, String email, String name,boolean isAdmin) {
        this.mUsername = username;
        this.mPassword = password;
        this.mEmail = email;
        this.mName = name;
        this.mIsAdmin = isAdmin;
    }
   /* public void setScores(List<Scores> scores) {
        this.scores = scores;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }*/
    //General functions for the entity
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Boolean isAdmin() {
        return mIsAdmin;
    }

    public void setAdmin(boolean admin) {
        mIsAdmin = true;
    }

}