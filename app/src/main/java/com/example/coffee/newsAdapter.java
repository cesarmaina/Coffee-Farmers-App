package com.example.coffee;

public class newsAdapter {
    public  String userId,text,image,date;

    public newsAdapter() {
    }

    public newsAdapter(String userId, String text, String image, String date) {
        this.userId = userId;
        this.text = text;
        this.image = image;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
