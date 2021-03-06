package com.a7552_2c_2018.melliapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PostItem {
    private String image;
    private Integer price;
    private String desc;
    private String id;
    private double latitude = 0.0;
    private double longitude = 0.0;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getJsonForm(){
        JSONObject json = new JSONObject();
        try {
            json.put("price", getPrice());
            json.put("id", getId());
            json.put("image", getImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }
}
