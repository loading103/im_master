package com.android.im.imbean;

import java.io.Serializable;
import java.util.List;

public class IMRedPickInformationBean implements Serializable {
    private String amount;
    private String avatar;
    private String isGrab;
    private boolean complete;
    private long createTime;

    private long endTime;
    private String nickName;
    private int number;
    private List<RecordList> recordList;
    private String redPacketId;
    private String remark;
    private SelfRecord selfRecord;
    private String state; //红包状态：0=未领完，1=已领完，2=部分退回，3=全部退回
    private String title;
    private String type;
    private String userId;
    private String userType;
    private String packetId;

    public String getIsGrab() {
        return isGrab;
    }

    public void setIsGrab(String isGrab) {
        this.isGrab = isGrab;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<RecordList> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordList> recordList) {
        this.recordList = recordList;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SelfRecord getSelfRecord() {
        return selfRecord;
    }

    public void setSelfRecord(SelfRecord selfRecord) {
        this.selfRecord = selfRecord;
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

    public class  RecordList implements Serializable {
        private String avatar;
        private String customerId;
        private String grabAmount;
        private String grabId;
        private long grabTime;
        private String nickName;
        private String thirdUid;
        private String userType;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getThirdUid() {
            return thirdUid;
        }

        public void setThirdUid(String thirdUid) {
            this.thirdUid = thirdUid;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }

    public class  SelfRecord implements Serializable {
        private String avatar;
        private String customerId;
        private String grabAmount;
        private String grabId;
        private long grabTime;
        private String nickName;
        private String thirdUid;
        private String userType;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getThirdUid() {
            return thirdUid;
        }

        public void setThirdUid(String thirdUid) {
            this.thirdUid = thirdUid;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}
