package com.example.coffee.adapters;

public class loandbhelper {
    String id1,mail,principal,interest,loan,time,dates,status;

    public loandbhelper() {
    }

    public loandbhelper(String Id,String mail, String principal, String interest, String loan, String time, String dates, String status) {
        this.id1=Id;
        this.mail = mail;
        this.principal = principal;
        this.interest = interest;
        this.loan = loan;
        this.time = time;
        this.dates = dates;
        this.status = status;
    }

    public String getId1() {
        return id1;
    }
    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }


    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
