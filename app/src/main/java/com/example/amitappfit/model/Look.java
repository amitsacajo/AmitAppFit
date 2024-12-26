package com.example.amitappfit.model;

public class Look {
    private String name;
    private String top;
    private String bottom;
    private String shoes;

    // Constructor
    public Look(String name, String top, String bottom, String shoes) {
        this.name = name;
        this.top = top;
        this.bottom = bottom;
        this.shoes = shoes;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }
}
