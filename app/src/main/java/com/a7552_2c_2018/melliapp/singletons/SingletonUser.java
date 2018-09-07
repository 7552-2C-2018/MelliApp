package com.a7552_2c_2018.melliapp.singletons;


import com.a7552_2c_2018.melliapp.model.UserInfo;

public class SingletonUser {

    private static SingletonUser userSingletonInstance;
    private UserInfo user;
    private String token;

    private SingletonUser() {
        user = new UserInfo();
        token = "";
    }


    public static synchronized SingletonUser getInstance() {
        if (userSingletonInstance == null) {
            userSingletonInstance = new SingletonUser();
        }
        return userSingletonInstance;
    }

    public void setUser(UserInfo aux) {
        user = aux;
    }

    public UserInfo getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
