package com.backbencherslab.gymbuddy.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import com.backbencherslab.gymbuddy.constants.Constants;


public class Gift extends Application implements Constants, Parcelable {

    private long id, giftFrom, giftTo;
    private int createAt, giftFromUserVip, giftFromUserVerify, giftAnonymous, giftId;
    private String timeAgo, date, message, imgUrl, giftFromUserUsername, giftFromUserFullname, giftFromUserPhoto;

    public Gift() {

    }

    public Gift(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setGiftFromUserId(jsonData.getLong("giftFrom"));
                this.setGiftToUserId(jsonData.getLong("giftTo"));
                this.setGiftId(jsonData.getInt("giftId"));
                this.setGiftFromUserVerify(jsonData.getInt("giftFromUserVerify"));
                this.setGiftFromUserVip(jsonData.getInt("giftFromUserVerify"));
                this.setGiftAnonymous(jsonData.getInt("giftAnonymous"));

                this.setGiftFromUserUsername(jsonData.getString("giftFromUserUsername"));
                this.setGiftFromUserFullname(jsonData.getString("giftFromUserFullname"));
                this.setGiftFromUserPhotoUrl(jsonData.getString("giftFromUserPhoto"));

                this.setMessage(jsonData.getString("message"));
                this.setImgUrl(jsonData.getString("imgUrl"));
                this.setCreateAt(jsonData.getInt("createAt"));
                this.setDate(jsonData.getString("date"));
                this.setTimeAgo(jsonData.getString("timeAgo"));
            }

        } catch (Throwable t) {

            Log.e("Gift", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Gift", jsonData.toString());
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGiftFromUserId() {

        return giftFrom;
    }

    public void setGiftFromUserId(long giftFrom) {

        this.giftFrom = giftFrom;
    }

    public long getGiftToUserId() {

        return giftTo;
    }

    public void setGiftToUserId(long giftTo) {

        this.giftTo = giftTo;
    }

    public void setGiftAnonymous(int giftAnonymous) {

        this.giftAnonymous = giftAnonymous;
    }

    public void setGiftId(int giftId) {

        this.giftId = giftId;
    }

    public int getGiftFromUserVerify() {

        return giftFromUserVerify;
    }

    public void setGiftFromUserVerify(int giftFromUserVerify) {

        this.giftFromUserVerify = giftFromUserVerify;
    }

    public void setGiftFromUserVip(int giftFromUserVip) {

        this.giftFromUserVip = giftFromUserVip;
    }

    public void setCreateAt(int createAt) {
        this.createAt = createAt;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGiftFromUserUsername() {
        return giftFromUserUsername;
    }

    public void setGiftFromUserUsername(String giftFromUserUsername) {

        this.giftFromUserUsername = giftFromUserUsername;
    }

    public String getGiftFromUserFullname() {

        return giftFromUserFullname;
    }

    public void setGiftFromUserFullname(String giftFromUserFullname) {

        this.giftFromUserFullname = giftFromUserFullname;
    }

    public String getGiftFromUserPhotoUrl() {

        if (this.giftFromUserPhoto == null) {

            this.giftFromUserPhoto = "";
        }

        return this.giftFromUserPhoto;
    }

    public void setGiftFromUserPhotoUrl(String giftFromUserPhoto) {

        this.giftFromUserPhoto = giftFromUserPhoto;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static final Creator CREATOR = new Creator() {

        public Gift createFromParcel(Parcel in) {

            return new Gift();
        }

        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };
}
