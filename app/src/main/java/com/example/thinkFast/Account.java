package com.example.thinkFast;

public class Account {

    private String username;
    private String password;
    private String email;
    private String name;
    private Boolean isAdmin;

  //  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
  //  List<Scores> scores = new ArrayList<>();

  //  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
 //   Statistics statistics;

    public Account(String username, String password, String email, String name,boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = true;
    }

}