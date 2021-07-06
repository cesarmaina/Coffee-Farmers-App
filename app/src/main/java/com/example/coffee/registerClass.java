package com.example.coffee;

public class registerClass {
    public String id,cid,name,email,phone,pass,dates,image;

    public registerClass() {
    }

    public registerClass(String id, String cid, String name, String email, String phone, String pass, String dates, String image) {
        this.id = id;
        this.cid = cid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        this.dates = dates;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
