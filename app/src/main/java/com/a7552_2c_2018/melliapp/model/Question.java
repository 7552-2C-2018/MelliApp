package com.a7552_2c_2018.melliapp.model;

public class Question {
    private String id;
    private String postId;
    private String userId;
    private long date;
    private String question;
    private boolean hasResponse;
    private boolean canAnswer;
    private long respDate;
    private String response;

    public Question() {
        hasResponse = false;
        canAnswer = false;
    }

    public boolean getCanAnswer() {
        return canAnswer;
    }

    public void setCanAnswer(boolean canAnswer) {
        this.canAnswer = canAnswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean getHasResponse() {
        return hasResponse;
    }

    public void setHasResponse(boolean hasResponse) {
        this.hasResponse = hasResponse;
    }

    public long getRespDate() {
        return respDate;
    }

    public void setRespDate(long respDate) {
        this.respDate = respDate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
