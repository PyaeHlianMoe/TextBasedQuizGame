package com.example.quizzer.Model;

import com.google.firebase.firestore.Exclude;
import java.io.Serializable;

public class Challenge implements Serializable{



    private String creatorId, receiverId, module, topic , difficulty, type;
    private Integer mark;
    private boolean isComplete;
    private String id;

    public Challenge(){

    }

    public Challenge(String creatorId, String receiverId, String module, String topic, String difficulty, String type, Integer mark, boolean isComplete) {
        this.creatorId = creatorId;
        this.receiverId = receiverId;
        this.module = module;
        this.topic = topic;
        this.difficulty = difficulty;
        this.type = type;
        this.mark = mark;
        this.isComplete = isComplete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getModule() {
        return module;
    }

    public String getTopic() {
        return topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getType() {
        return type;
    }

    public Integer getMark() {
        return mark;
    }

    public boolean getIsComplete() {
        return isComplete;
    }
}
