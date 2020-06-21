package com.android.nettylibrary.greendao.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
//friendApply/findFriend
@Entity
public class IMPersonBean implements Serializable {
    private static final long serialVersionUID = -8132139458311739752L;
    @Id(autoincrement = true)
    private Long _id;   //id,表的主键并自增长

    private String signature;   //	客服-个性签名

    private String sex;  //客服-性别：1=男，2=女，3=其他

    private String mobile;

    private String avatar;    ////客服-头像

    private String level;    ////会员等级

    private String points;    ////会员等级

    private String title;    ////会员-头衔

    private String lable;    ////	用户标签

    private String isOnline;   //用户离线
    private String memberId;
    private String  status;
    private  String  lastmessage;

    private  int  lastMsgSendType;
    // TEXT = 0 文本消息;    PICTURE = 1图片消息;    VOICE = 2语音消息;      VIDEO = 3视频消息;   FILE = 4 文件消息;
    // REDENVELOPES = 5红包消息;        DOCUMENTARY = 6跟单消息;          REDBILL = 7红单消息;        PLAN = 8计划消息；
    private  int  lastMsgType;

    private   int  lastsendstate;   //最后消息发送状态0成功  1发送中 2 失败

    private  long  lastMessageTime;   //最后消息时间
    
    private   String  lastmessageId;   //最后消息发送状态

    //消息是不是置顶
    private boolean isSetTop;  //是不是置顶

    private long setTopTime;     //置顶的时间（置顶的部分由置顶的时间戳来排序）

    private String letter;

    private boolean ischoosed;     //分享时候是不是选中

    private String isKefu;

    private String customerId;

    private String nickName;

    private String type;

    private String isBlock;

    private String accountStatus; //账号状态，0是正常 1是警告 2是冻结

    private String bgUrl;

    @Generated(hash = 911417868)
    public IMPersonBean(Long _id, String signature, String sex, String mobile, String avatar,
            String level, String points, String title, String lable, String isOnline, String memberId,
            String status, String lastmessage, int lastMsgSendType, int lastMsgType, int lastsendstate,
            long lastMessageTime, String lastmessageId, boolean isSetTop, long setTopTime,
            String letter, boolean ischoosed, String isKefu, String customerId, String nickName,
            String type, String isBlock, String accountStatus, String bgUrl) {
        this._id = _id;
        this.signature = signature;
        this.sex = sex;
        this.mobile = mobile;
        this.avatar = avatar;
        this.level = level;
        this.points = points;
        this.title = title;
        this.lable = lable;
        this.isOnline = isOnline;
        this.memberId = memberId;
        this.status = status;
        this.lastmessage = lastmessage;
        this.lastMsgSendType = lastMsgSendType;
        this.lastMsgType = lastMsgType;
        this.lastsendstate = lastsendstate;
        this.lastMessageTime = lastMessageTime;
        this.lastmessageId = lastmessageId;
        this.isSetTop = isSetTop;
        this.setTopTime = setTopTime;
        this.letter = letter;
        this.ischoosed = ischoosed;
        this.isKefu = isKefu;
        this.customerId = customerId;
        this.nickName = nickName;
        this.type = type;
        this.isBlock = isBlock;
        this.accountStatus = accountStatus;
        this.bgUrl = bgUrl;
    }

    @Generated(hash = 856309832)
    public IMPersonBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPoints() {
        return this.points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLable() {
        return this.lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getIsOnline() {
        return this.isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastmessage() {
        return this.lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public int getLastMsgSendType() {
        return this.lastMsgSendType;
    }

    public void setLastMsgSendType(int lastMsgSendType) {
        this.lastMsgSendType = lastMsgSendType;
    }

    public int getLastMsgType() {
        return this.lastMsgType;
    }

    public void setLastMsgType(int lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public int getLastsendstate() {
        return this.lastsendstate;
    }

    public void setLastsendstate(int lastsendstate) {
        this.lastsendstate = lastsendstate;
    }

    public long getLastMessageTime() {
        return this.lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastmessageId() {
        return this.lastmessageId;
    }

    public void setLastmessageId(String lastmessageId) {
        this.lastmessageId = lastmessageId;
    }

    public boolean getIsSetTop() {
        return this.isSetTop;
    }

    public void setIsSetTop(boolean isSetTop) {
        this.isSetTop = isSetTop;
    }

    public long getSetTopTime() {
        return this.setTopTime;
    }

    public void setSetTopTime(long setTopTime) {
        this.setTopTime = setTopTime;
    }

    public String getLetter() {
        return this.letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean getIschoosed() {
        return this.ischoosed;
    }

    public void setIschoosed(boolean ischoosed) {
        this.ischoosed = ischoosed;
    }

    public String getIsKefu() {
        return this.isKefu;
    }

    public void setIsKefu(String isKefu) {
        this.isKefu = isKefu;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsBlock() {
        return this.isBlock;
    }

    public void setIsBlock(String isBlock) {
        this.isBlock = isBlock;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getBgUrl() {
        return this.bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

}
