package com.example.amitappfit.model;

public class Look {
    private final String name;
    private final String top;
    private final String bottom;
    private final String shoes;

    public Look(String name, String top, String bottom, String shoes) {
        this.name = name;
        this.top = top;
        this.bottom = bottom;
        this.shoes = shoes;
    }

    public String getName() {
        return name;
    }

    public String getTop() {
        return top;
    }

    public String getBottom() {
        return bottom;
    }

    public String getShoes() {
        return shoes;
    }
}
