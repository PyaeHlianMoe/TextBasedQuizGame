package com.example.quizzer.Model;

public class Student {

    private String userId;
    private String userName;
    private String email;
    private String type;


    public Student(String studentId, String name, String email,String type){
        this.userId = studentId;
        this.userName = name;
        this.email = email;
        this.type =  type;
    }

    public String getStudentId() {
        return userId;
    }

    public void setStudentId(String studentId) {
        this.userId = studentId;
    }

    public String getName() { return userName; }

    public void setName(String name) {
        this.userName = name;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }


}
