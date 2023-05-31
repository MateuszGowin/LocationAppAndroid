package com.example.myapplication.model;

import com.example.myapplication.model.User;

import java.util.Date;

public class AddOpinionRequest {
    private int rate;
    private String comment;
    private Date upload_date;

    public AddOpinionRequest() {
    }

    public AddOpinionRequest(int rate, String comment, Date upload_date) {
        this.rate = rate;
        this.comment = comment;
        this.upload_date = upload_date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }
}
