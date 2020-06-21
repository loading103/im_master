package com.android.im.imbean;

import java.util.List;

public class IMSendRedPickRecordList {
    public String code;
    public String message;
    public IMSendRedPickRecordListBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(IMSendRedPickRecordListBean data) {
        this.data = data;
    }

    public IMSendRedPickRecordListBean getData() {
        return data;
    }

    public class IMSendRedPickRecordListBean {
        private List<IMRedPacketList> redPacketList;
        private int totalSize;
        private String nickName;
        private int sendNumber;
        private String avatar;
        private String userType;
        private int totalPageNum;
        private String sendTotalAmount;
        private int acceptBestNumber;
        private int acceptNumber;
        private String acceptTotalAmount;


        public int getAcceptBestNumber() {
            return acceptBestNumber;
        }

        public void setAcceptBestNumber(int acceptBestNumber) {
            this.acceptBestNumber = acceptBestNumber;
        }

        public int getAcceptNumber() {
            return acceptNumber;
        }

        public void setAcceptNumber(int acceptNumber) {
            this.acceptNumber = acceptNumber;
        }

        public String getAcceptTotalAmount() {
            return acceptTotalAmount;
        }

        public void setAcceptTotalAmount(String acceptTotalAmount) {
            this.acceptTotalAmount = acceptTotalAmount;
        }

        public List<IMRedPacketList> getRedPacketList() {
            return redPacketList;
        }

        public void setRedPacketList(List<IMRedPacketList> redPacketList) {
            this.redPacketList = redPacketList;
        }

        public int getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSendNumber() {
            return sendNumber;
        }

        public void setSendNumber(int sendNumber) {
            this.sendNumber = sendNumber;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public int getTotalPageNum() {
            return totalPageNum;
        }

        public void setTotalPageNum(int totalPageNum) {
            this.totalPageNum = totalPageNum;
        }

        public String getSendTotalAmount() {
            return sendTotalAmount;
        }

        public void setSendTotalAmount(String sendTotalAmount) {
            this.sendTotalAmount = sendTotalAmount;
        }
    }
    public class  IMRedPacketList{
        private int amount;
        private long createTime;
        private int gradNumber;
        private String redPacketId;
        private String state;
        private int totalNumber;
        private String type;
        private String nickName;
        private long grabTime;
        private String avatar;
        private String grabAmount;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getGrabTime() {
            return grabTime;
        }

        public void setGrabTime(long grabTime) {
            this.grabTime = grabTime;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getGrabAmount() {
            return grabAmount;
        }

        public void setGrabAmount(String grabAmount) {
            this.grabAmount = grabAmount;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getGradNumber() {
            return gradNumber;
        }

        public void setGradNumber(int gradNumber) {
            this.gradNumber = gradNumber;
        }

        public String getRedPacketId() {
            return redPacketId;
        }

        public void setRedPacketId(String redPacketId) {
            this.redPacketId = redPacketId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(int totalNumber) {
            this.totalNumber = totalNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
