package com.directlineex.brsingh.directlineandroidexample;

import java.util.ArrayList;

/**
 * Created by brsingh on 1/6/2017.
 */
public class ChatMessage {
    private long id;
    private boolean isMe;
    private String message;
    private Long userId;
    private String dateTime;

    private ArrayList<String> btnMsgs;

    public void setBtnMsgs(ArrayList<String> btnMsgs) { this.btnMsgs = btnMsgs; }
    public ArrayList<String> getBtnMsgs() { return btnMsgs; }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public boolean getIsme() {
        return isMe;
    }
    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }
}