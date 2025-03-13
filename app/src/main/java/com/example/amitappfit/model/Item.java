package com.example.amitappfit.model;

public class Item {

    private String id;
    private String title;
    private String category;
    private String picBase64;

    public Item() {
    }



    public Item(String id, String title, String category, String picBase64) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.picBase64 = picBase64;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPicBase64() {
        return picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", picBase64='" + picBase64 + '\'' +
                '}';
    }
}
