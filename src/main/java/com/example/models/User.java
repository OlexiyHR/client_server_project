package com.example.models;

public class User {
    private String login;
    private String password;


    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getters for each attribute
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public String toString() {
        return "Login: " + login + "\n" +
                "Password : " + password + "\n";
    }
}
