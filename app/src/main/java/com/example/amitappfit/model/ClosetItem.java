package com.example.amitappfit.model;

public class ClosetItem {
    private String name;
    private String category;
    private int imageResId;

    public ClosetItem(String name, String category, int imageResId) {
        this.name = name;
        this.category = category;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getImageResId() {
        return imageResId;
    }
}
