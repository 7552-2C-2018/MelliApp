package com.a7552_2c_2018.melliapp.model;

public class ActualBuy {

    private String id;
    private String title;
    private int price;
    private boolean paysShipping;
    private int shippingPrice;
    private boolean paysWithCard;
    private String cardNumber;
    private String cardName;
    private String cardDate;
    private int cardCVV;
    private String street, cp, floor, dept, city;

    public ActualBuy() {
        this.id = "";
        this.title = "";
        this.price = 0;
        this.paysShipping = false;
        this.shippingPrice = 0;
        this.paysWithCard = false;
        this.cardNumber = "";
        this.cardName = "";
        this.cardDate = "";
        this.cardCVV = 0;
        this.street = "";
        this.cp = "";
        this.floor = "";
        this.dept = "";
        this.city = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPaysShipping() {
        return paysShipping;
    }

    public void setPaysShipping(boolean paysShipping) {
        this.paysShipping = paysShipping;
    }

    public int getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(int shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public boolean isPaysWithCard() {
        return paysWithCard;
    }

    public void setPaysWithCard(boolean paysWithCard) {
        this.paysWithCard = paysWithCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public int getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(int cardCVV) {
        this.cardCVV = cardCVV;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        return price + shippingPrice;
    }
}
