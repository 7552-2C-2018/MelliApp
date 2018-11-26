package com.a7552_2c_2018.melliapp.model;

public class ActualFilters {

    private boolean condSelected;
    private boolean priceSelected;
    private boolean categSelected;
    private boolean shipSelected;
    private boolean distSelected;
    private boolean onlyNew;
    private boolean onlyUsed;
    private int minPrice;
    private int maxPrice;
    private String categ;
    private boolean shipYes;
    private boolean shipNo;
    private int maxDist;

    public ActualFilters() {
        this.condSelected = false;
        this.priceSelected = false;
        this.categSelected = false;
        this.distSelected = false;
        this.shipSelected = false;
    }

    public boolean isCondSelected() {
        return condSelected;
    }

    public void setCondSelected(boolean condSelected) {
        this.condSelected = condSelected;
    }

    public boolean isPriceSelected() {
        return priceSelected;
    }

    public void setPriceSelected(boolean priceSelected) {
        this.priceSelected = priceSelected;
    }

    public boolean isCategSelected() {
        return categSelected;
    }

    public void setCategSelected(boolean categSelected) {
        this.categSelected = categSelected;
    }

    public boolean isDistSelected() {
        return distSelected;
    }

    public void setDistSelected(boolean distSelected) {
        this.distSelected = distSelected;
    }

    public boolean isOnlyNew() {
        return onlyNew;
    }

    public void setOnlyNew(boolean onlyNew) {
        this.onlyNew = onlyNew;
    }

    public boolean isOnlyUsed() {
        return onlyUsed;
    }

    public void setOnlyUsed(boolean onlyUsed) {
        this.onlyUsed = onlyUsed;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public int getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(int maxDist) {
        this.maxDist = maxDist;
    }

    public boolean isShipSelected() {
        return shipSelected;
    }

    public void setShipSelected(boolean shipSelected) {
        this.shipSelected = shipSelected;
    }

    public boolean isShipYes() {
        return shipYes;
    }

    public void setShipYes(boolean shipYes) {
        this.shipYes = shipYes;
    }

    public boolean isShipNo() {
        return shipNo;
    }

    public void setShipNo(boolean shipNo) {
        this.shipNo = shipNo;
    }

    public boolean anyFilterOn(){
        return isShipSelected() || isCategSelected() || isPriceSelected() ||
                isCondSelected() || isDistSelected();
    }
}
