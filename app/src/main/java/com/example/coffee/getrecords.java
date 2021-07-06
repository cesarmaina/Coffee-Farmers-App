package com.example.coffee;

public class getrecords {
    private  String grade1,grade2,mbuni,date,userid;

    public getrecords() {
    }

    public getrecords(String grade1, String grade2, String mbuni, String date, String userid) {
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.mbuni = mbuni;
        this.date = date;
        this.userid = userid;
    }

    public String getGrade1() {
        return grade1;
    }

    public void setGrade1(String grade1) {
        this.grade1 = grade1;
    }

    public String getGrade2() {
        return grade2;
    }

    public void setGrade2(String grade2) {
        this.grade2 = grade2;
    }

    public String getMbuni() {
        return mbuni;
    }

    public void setMbuni(String mbuni) {
        this.mbuni = mbuni;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUseridd() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
