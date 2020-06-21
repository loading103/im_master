package com.android.im.imbean;

public class IMApplyInforBean {
    private String level;
    private String nickName;
    private FriendApply friendApply;
    private String isViewHit;
    private String ipAddress;
    private String avatar;
    private String type;
    private String title;
    private String hitRate;
    private String points;
    private String gmtRegister;
    private String lotteryBalance;
    private String isViewTitle;
    private String gmtOffline;
    private String customerId;
    private String name;
    private String signature;
    private String isFriendValid;
    private String isKefu;
    private String thirdUid;
    private String username;
    private String bgUrl;

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public FriendApply getFriendApply() {
        return friendApply;
    }

    public void setFriendApply(FriendApply friendApply) {
        this.friendApply = friendApply;
    }

    public String getIsViewHit() {
        return isViewHit;
    }

    public void setIsViewHit(String isViewHit) {
        this.isViewHit = isViewHit;
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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
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

    public String getIsViewTitle() {
        return isViewTitle;
    }

    public void setIsViewTitle(String isViewTitle) {
        this.isViewTitle = isViewTitle;
    }

    public String getGmtOffline() {
        return gmtOffline;
    }

    public void setGmtOffline(String gmtOffline) {
        this.gmtOffline = gmtOffline;
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

    public String getIsFriendValid() {
        return isFriendValid;
    }

    public void setIsFriendValid(String isFriendValid) {
        this.isFriendValid = isFriendValid;
    }

    public String getIsKefu() {
        return isKefu;
    }

    public void setIsKefu(String isKefu) {
        this.isKefu = isKefu;
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

    public class FriendApply {
        private String acceptCustomerId;
        private String applyId;
        private String applyRemark;
        private String appId;
        private long gmtUpdate;
        private String customerId;
        private String remark;
        private long gmtCreate;
        private String applyStatus;
        private String logicalDeletion;

        public String getAcceptCustomerId() {
            return acceptCustomerId;
        }

        public void setAcceptCustomerId(String acceptCustomerId) {
            this.acceptCustomerId = acceptCustomerId;
        }

        public String getApplyId() {
            return applyId;
        }

        public void setApplyId(String applyId) {
            this.applyId = applyId;
        }

        public String getApplyRemark() {
            return applyRemark;
        }

        public void setApplyRemark(String applyRemark) {
            this.applyRemark = applyRemark;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public long getGmtUpdate() {
            return gmtUpdate;
        }

        public void setGmtUpdate(long gmtUpdate) {
            this.gmtUpdate = gmtUpdate;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getApplyStatus() {
            return applyStatus;
        }

        public void setApplyStatus(String applyStatus) {
            this.applyStatus = applyStatus;
        }

        public String getLogicalDeletion() {
            return logicalDeletion;
        }

        public void setLogicalDeletion(String logicalDeletion) {
            this.logicalDeletion = logicalDeletion;
        }
    }
}
