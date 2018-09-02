package com.a7552_2c_2018.melliapp;

public class UserInfo {

    private String facebookID;
    private String name;
    private String surname;
    private String email;
    private String photoURL;

    public UserInfo (){

    }

    public UserInfo(String facebookID, String name, String surname, String email) {
        this.facebookID = facebookID;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
