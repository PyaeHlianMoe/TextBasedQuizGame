package com.example.quizzer.Model;

public class Score {

    private String challengeId;
    private String userId;
    private Integer pointsObtained;
    private Integer pointsTaken;

    public Score (String challengeId, String userId, Integer pointsObtained, Integer pointsTaken){
        this.challengeId = challengeId;
        this.userId = userId;
        this.pointsObtained = pointsObtained;
        this.pointsTaken = pointsTaken;
    }
    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPointsObtained() {
        return pointsObtained;
    }

    public void setPointsObtained(Integer pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    public Integer getPointsTaken() {
        return pointsTaken;
    }

    public void setPointsTaken(Integer pointsTaken) {
        this.pointsTaken = pointsTaken;
    }





}
