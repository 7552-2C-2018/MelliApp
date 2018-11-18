package com.a7552_2c_2018.melliapp.model;

public class Question {
    private long date;
    private String question;
    private Boolean hasResponse;
    private long respDate;
    private String response;

    public Question() {
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

    public Boolean getHasResponse() {
        return hasResponse;
    }

    public void setHasResponse(Boolean hasResponse) {
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
