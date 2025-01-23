package com.example.amitappfit.model;

import androidx.annotation.NonNull;

public class Item {
    private String title;
    private String picBase64;

    public Item() {
    }

    public Item(String title, String picBase64) {
        this.title = title;
        this.picBase64 = picBase64;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicBase64() {
        return picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", picBase64='" + picBase64 + '\'' +
                '}';
    }
}
