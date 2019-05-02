package com.example.quizzer.Model;

import java.util.List;

public class QuizSet {

    private boolean completed;
    private List<Question> questions;
    private String quizId;
    private String title;

    public QuizSet()
    {

    }

    public QuizSet(boolean completed, List<Question> questions, String quizId, String title) {
        this.completed = completed;
        this.questions = questions;
        this.quizId = quizId;
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
