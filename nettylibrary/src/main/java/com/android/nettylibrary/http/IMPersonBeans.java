package com.android.nettylibrary.http;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

public class IMPersonBeans {

    private String lotteryBalance;  //客服-可分配的彩金

    private String groupType;  //客服-分组

    private String isCharacteristic;   //客服-是否使用标识

    private String lable;   //	客服-个性签名

    private String sex;  //客服-性别：1=男，2=女，3=其他

    private String usePageTemplate;  //客服-使用模板

    private String nickname;  //客服-昵称

    private String name;   //客服-名字

    private String type;

    private String characteristic;  //客服-标识

    private String avatar;    ////客服-头像

    private String level;

    private String uid;

    private String title;

    private String points;    ////会员等级

    private String gmtOffline;   //用户离线消息

    private String appId;

    private String  customerId;
    private String  acceptCustomerId;

    private String  applyRemark;
    private List<String>  ids;
    private String  applyId;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getAcceptCustomerId() {
        return acceptCustomerId;
    }

    public void setAcceptCustomerId(String acceptCustomerId) {
        this.acceptCustomerId = acceptCustomerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getGmtOffline() {
        return gmtOffline;
    }

    public void setGmtOffline(String gmtOffline) {
        this.gmtOffline = gmtOffline;
    }

    public String getLotteryBalance() {
        return lotteryBalance;
    }

    public void setLotteryBalance(String lotteryBalance) {
        this.lotteryBalance = lotteryBalance;
    }

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

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public class LoginSuccessBean {
        private String code;
        private IMLoginData data;  //token
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public IMLoginData getData() {
            return data;
        }

        public void setData(IMLoginData data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
