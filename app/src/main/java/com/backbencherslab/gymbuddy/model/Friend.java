package com.backbencherslab.gymbuddy.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import com.backbencherslab.gymbuddy.constants.Constants;


public class Friend extends Application implements Constants, Parcelable {

    private long id, friendTo, friendUserId;

    private int verify, vip;

    private String friendUserUsername, friendUserFullname, friendUserPhoto, friendLocation, timeAgo;

    private Boolean online = false;

    public Friend() {


    }

    public Friend(JSONObject jsonData) {

        try {

            if (!jsonData.getBoolean("error")) {

                this.setId(jsonData.getLong("id"));
                this.setFriendUserId(jsonData.getLong("friendUserId"));
                this.setFriendUserVip(jsonData.getInt("friendUserVip"));
                this.setFriendUserVerify(jsonData.getInt("friendUserVerify"));
                this.setFriendUserUsername(jsonData.getString("friendUserUsername"));
                this.setFriendUserFullname(jsonData.getString("friendUserFullname"));
                this.setFriendUserPhotoUrl(jsonData.getString("friendUserPhoto"));
                this.setFriendLocation(jsonData.getString("friendLocation"));
                this.setFriendTo(jsonData.getLong("friendTo"));
                this.setTimeAgo(jsonData.getString("timeAgo"));
                this.setOnline(jsonData.getBoolean("friendUserOnline"));
            }

        } catch (Throwable t) {

            Log.e("Friend", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Friend", jsonData.toString());
        }
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void setFriendTo(long friendTo) {

        this.friendTo = friendTo;
    }

    public void setFriendUserId(long friendUserId) {

        this.friendUserId = friendUserId;
    }

    public long getFriendUserId() {

        return this.friendUserId;
    }

    public void setFriendUserVip(int friendUserVip) {

        this.vip = friendUserVip;
    }

    public void setFriendUserVerify(int friendUserVerify) {

        this.verify = friendUserVerify;
    }

    public Boolean isVerify() {

        if (this.verify > 0) {

            return true;
        }

        return false;
    }

    public void setFriendUserUsername(String friendUserUsername) {

        this.friendUserUsername = friendUserUsername;
    }

    public String getFriendUserUsername() {

        return this.friendUserUsername;
    }

    public void setFriendUserFullname(String friendUserFullname) {

        this.friendUserFullname = friendUserFullname;
    }

    public String getFriendUserFullname() {

        return this.friendUserFullname;
    }

    public void setFriendUserPhotoUrl(String friendUserPhoto) {

        this.friendUserPhoto = friendUserPhoto;
    }

    public String getFriendUserPhotoUrl() {

        return this.friendUserPhoto;
    }

    public void setFriendLocation(String friendLocation) {

        this.friendLocation = friendLocation;
    }

    public String getFriendLocation() {

        return this.friendLocation;
    }

    public void setTimeAgo(String ago) {

        this.timeAgo = ago;
    }

    public String getTimeAgo() {

        return this.timeAgo;
    }

    public void setOnline(Boolean online) {

        this.online = online;
    }

    public Boolean isOnline() {

        return this.online;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static final Creator CREATOR = new Creator() {

        public Friend createFromParcel(Parcel in) {

            return new Friend();
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}
