package com.a7552_2c_2018.melliapp.model;

public class PostItem {
    private String image;
    private Integer price;
    private String desc;
    private String facebookId;
    private String publDate;

    public PostItem() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}
