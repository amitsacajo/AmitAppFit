package com.example.amitappfit.model;

import androidx.annotation.NonNull;

public class Look {
    private String id;
    private String name;
    private Item top;
    private Item bottom;
    private Item shoes;
    private String userId;



    public Look() {
    }

    public Look(String id, String name, Item top, Item bottom, Item shoes, String userId) {
        this.id = id;
        this.name = name;
        this.top = top;
        this.bottom = bottom;
        this.shoes = shoes;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item getTop() {
        return top;
    }

    public void setTop(Item top) {
        this.top = top;
    }

    public Item getBottom() {
        return bottom;
    }

    public void setBottom(Item bottom) {
        this.bottom = bottom;
    }

    public Item getShoes() {
        return shoes;
    }

    public void setShoes(Item shoes) {
        this.shoes = shoes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "Look{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", top=" + top +
                ", bottom=" + bottom +
                ", shoes=" + shoes +
                ", userId='" + userId + '\'' +
                '}';
    }
}
