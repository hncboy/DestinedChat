package com.iceboy.destinedchat.event;

/**
 * Created by hncboy on 2018/6/15.
 * 传输添加好友的event类型
 */
public class AddFriendEvent {

    private String friendName;
    private String reason;

    public AddFriendEvent(String mFriendName, String mReason) {
        this.friendName = mFriendName;
        this.reason = mReason;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
