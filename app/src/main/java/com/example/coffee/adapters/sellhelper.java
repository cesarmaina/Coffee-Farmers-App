package com.example.coffee.adapters;

public class sellhelper {
    String name;
    String cost;
    String quantity;
    String muri;
    String date;
    String category;
    String id;
    String count;
    public sellhelper() {
    }

    public sellhelper(String name, String cost, String quantity, String muri, String date, String category,String id, String count) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.muri = muri;
        this.date = date;
        this.category=category;
        this.id = id;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMuri() {
        return muri;
    }

    public void setMuri(String muri) {
        this.muri = muri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(){
        this.category=category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
