package com.android.nettylibrary.http;

import java.io.Serializable;

public class IMUserInforBean {
    private String code;
    private UserInforData data;
    private String message;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setData(UserInforData data) {
        this.data = data;
    }
    public UserInforData getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }



    public class  UserInforData implements Serializable {
        private String level;
        private String nickName;
        private String ipAddress;
        private String avatar;
        private String type;
        private String title;
        private String hitRate;
        private String gmtRegister;
        private String lotteryBalance;
        private String customerId;
        private String name;
        private String thirdUid;
        private String username;
        private String points;
        private String isPayPwd;
        private String isViewHit;
        private String isViewTitle;
        private String signature;
        private String isFriendValid;
        private String bgUrl;
        private String isFriend;
        private String isKefu;
        private String isOnline;

        public String getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(String isOnline) {
            this.isOnline = isOnline;
        }

        public String getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(String isFriend) {
            this.isFriend = isFriend;
        }

        public String getIsKefu() {
            return isKefu;
        }

        public void setIsKefu(String isKefu) {
            this.isKefu = isKefu;
        }

        public String getBgUrl() {
            return bgUrl;
        }

        public void setBgUrl(String bgUrl) {
            this.bgUrl = bgUrl;
        }

        public String getIsFriendValid() {
            return isFriendValid;
        }

        public void setIsFriendValid(String isFriendValid) {
            this.isFriendValid = isFriendValid;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getIsViewHit() {
            return isViewHit;
        }

        public void setIsViewHit(String isViewHit) {
            this.isViewHit = isViewHit;
        }

        public String getIsViewTitle() {
            return isViewTitle;
        }

        public void setIsViewTitle(String isViewTitle) {
            this.isViewTitle = isViewTitle;
        }

        public String getIsPayPwd() {
            return isPayPwd;
        }

        public void setIsPayPwd(String isPayPwd) {
            this.isPayPwd = isPayPwd;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHitRate() {
            return hitRate;
        }

        public void setHitRate(String hitRate) {
            this.hitRate = hitRate;
        }

        public String getGmtRegister() {
            return gmtRegister;
        }

        public void setGmtRegister(String gmtRegister) {
            this.gmtRegister = gmtRegister;
        }

        public String getLotteryBalance() {
            return lotteryBalance;
        }

        public void setLotteryBalance(String lotteryBalance) {
            this.lotteryBalance = lotteryBalance;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThirdUid() {
            return thirdUid;
        }

        public void setThirdUid(String thirdUid) {
            this.thirdUid = thirdUid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public UserInforData(String customerId) {
            this.customerId = customerId;
        }
    }
}
