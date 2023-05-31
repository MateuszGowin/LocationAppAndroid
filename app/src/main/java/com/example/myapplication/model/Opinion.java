package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Opinion implements Parcelable {
    private Long id;
    private int rate;
    private String comment;
    private User user;
    private Date upload_date;

    public Opinion() {
    }

    public Opinion(Long id, int rate, String comment, User user, Date upload_date) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
        this.user = user;
        this.upload_date = upload_date;
    }

    protected Opinion(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        rate = in.readInt();
        comment = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        upload_date = new Date(in.readLong());
    }

    public static final Creator<Opinion> CREATOR = new Creator<Opinion>() {
        @Override
        public Opinion createFromParcel(Parcel in) {
            return new Opinion(in);
        }

        @Override
        public Opinion[] newArray(int size) {
            return new Opinion[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }

    @Override
    public String toString() {
        return "Opinion{" +
                "id=" + id +
                ", rate=" + rate +
                ", comment='" + comment + '\'' +
                ", user=" + user +
                ", date=" + upload_date +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeInt(rate);
        parcel.writeString(comment);
        parcel.writeLong(upload_date.getTime());
        parcel.writeParcelable(user,i);
    }
}
