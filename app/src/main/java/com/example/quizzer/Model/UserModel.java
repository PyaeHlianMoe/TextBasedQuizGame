package com.example.quizzer.Model;

public class UserModel {
    private String displayName;
    private String email;

    public UserModel(String displayName, String email){
        this.displayName = displayName;
        this.email = email;

    }

    public  UserModel(){

    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
