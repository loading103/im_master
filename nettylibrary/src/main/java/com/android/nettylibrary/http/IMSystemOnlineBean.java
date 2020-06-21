package com.android.nettylibrary.http;

import java.io.Serializable;

public class IMSystemOnlineBean implements Serializable{
    private Meta meta;
    private String sysCode;
    private String text;
    private String type;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class Meta {
        private String groupType;
        private String isCharacteristic;
        private String signature;
        private String sex;
        private String mobile;
        private String avatar;
        private String characteristic;
        private String lotteryBalance;
        private String usePageTemplate;
        private String nickname;
        private String name;
        private String memberId;
        private String status;

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

        public String getIsCharacteristic() {
            return isCharacteristic;
        }

        public void setIsCharacteristic(String isCharacteristic) {
            this.isCharacteristic = isCharacteristic;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCharacteristic() {
            return characteristic;
        }

        public void setCharacteristic(String characteristic) {
            this.characteristic = characteristic;
        }

        public String getLotteryBalance() {
            return lotteryBalance;
        }

        public void setLotteryBalance(String lotteryBalance) {
            this.lotteryBalance = lotteryBalance;
        }

        public String getUsePageTemplate() {
            return usePageTemplate;
        }

        public void setUsePageTemplate(String usePageTemplate) {
            this.usePageTemplate = usePageTemplate;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
