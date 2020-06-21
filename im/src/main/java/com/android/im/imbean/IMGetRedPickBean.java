package com.android.im.imbean;

import java.io.Serializable;

public class IMGetRedPickBean  implements Serializable {
    private String grabAmount;
    private String grabId;
    private long grabTime;
    private String number;
    private String redPacketId;
    private String title;
    private String type;//红包类型：1=固定金额，2=随机金额
    private String amount;
    private String avatar;
    private boolean complete;
    private long createTime;
    private long endTime;
    private String remark;
    private String userId;
    private String userType;






    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getGrabAmount() {
        return grabAmount;
    }

    public void setGrabAmount(String grabAmount) {
        this.grabAmount = grabAmount;
    }

    public String getGrabId() {
        return grabId;
    }

    public void setGrabId(String grabId) {
        this.grabId = grabId;
    }

    public long getGrabTime() {
        return grabTime;
    }

    public void setGrabTime(long grabTime) {
        this.grabTime = grabTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
