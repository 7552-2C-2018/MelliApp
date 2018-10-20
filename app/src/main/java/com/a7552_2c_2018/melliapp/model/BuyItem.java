package com.a7552_2c_2018.melliapp.model;

public class BuyItem {
    private String image;
    private String title;
    private String status;
    private String facebookId;
    private String publDate;

    public BuyItem() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getPublDate() {
        return publDate;
    }

    public void setPublDate(String publDate) {
        this.publDate = publDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
