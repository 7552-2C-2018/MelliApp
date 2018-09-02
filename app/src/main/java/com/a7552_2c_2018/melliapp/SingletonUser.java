package com.a7552_2c_2018.melliapp;


public class SingletonUser {

    private static SingletonUser userSingletonInstance;
    public UserInfo user;

    private SingletonUser() {
        user = new UserInfo();
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


}
