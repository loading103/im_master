package com.android.im.imbean;

/**
 *     "isGroup": "N",//是否为群红包
 *     "number": 1,//个数
 *     "reasons": "测试用户给客服发送-奖励红包",//原因
 *     "remark": "11086彩金红包",//备注
 *     "title": "测试用户给客服发送-恭喜发财",//标题
 *     "type": "1"//红包类型
 */
public class IMRedSendJsonBean {
    private String amount;//红包金额
    private boolean group;
    private String groupId;
    private String isGroup;
    private int number;
    private String reasons;
    private String remark;
    private String sendUserId;
    private String title;
    private String type;             //红包类型
    private String userType;        //发送红包的用户类型：1=客服，2=用户

    private String acceptUserId;//红包接收者ID
    private String acceptUserType;//红包接收者用户类型
    private String drawPassword;

    public String getDrawPassword() {
        return drawPassword;
    }

    public void setDrawPassword(String drawPassword) {
        this.drawPassword = drawPassword;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getAcceptUserType() {
        return acceptUserType;
    }

    public void setAcceptUserType(String acceptUserType) {
        this.acceptUserType = acceptUserType;
    }
}
