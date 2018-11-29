package com.a7552_2c_2018.melliapp.singletons;


import com.a7552_2c_2018.melliapp.model.ActualBuy;
import com.a7552_2c_2018.melliapp.model.ActualFilters;
import com.a7552_2c_2018.melliapp.model.PostItem;
import com.a7552_2c_2018.melliapp.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class SingletonUser {

    private static SingletonUser userSingletonInstance;
    private UserInfo user;
    private String token;
    private ActualBuy buy;
    private ActualFilters filters;
    private List<PostItem> itemList;

    private SingletonUser() {
        user = new UserInfo();
        token = "";
        buy = new ActualBuy();
        filters = new ActualFilters();
        itemList = new ArrayList<>();
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

    public void setActualBuy(ActualBuy aux) {
        buy = aux;
    }

    public ActualBuy getActualBuy() {
        return buy;
    }

    public void setActualFilters(ActualFilters aux) {
        filters = aux;
    }

    public ActualFilters getActualFilters() {
        return filters;
    }

    public List<PostItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<PostItem> itemList) {
        this.itemList = itemList;
    }
}
