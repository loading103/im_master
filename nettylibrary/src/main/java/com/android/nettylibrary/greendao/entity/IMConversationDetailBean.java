package com.android.nettylibrary.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class IMConversationDetailBean implements Serializable {
    private static final long serialVersionUID = -3254059512199127298L;
    @Id(autoincrement = true)
    public  Long _id;

    private String companyId;      //最后的一条消息

    private int msgType;

    private int  lastMsgSendType;   //0是发送，1是接收

    private String receiverId;

    private String senderId;

    private String groupId;

    private int sendstate;   //最后的一条消息发送的状态 0是成功  1是发送中   2是失败

    private long timestamp;

    private String data;

    private String fingerprint;//

    private String userId;

    private String width;

    private String hight;

    private  boolean   getredpick;  //是不是领取了红包

    private String ownurl;

    private String ownfirstFrame;
    //当前使用的用户
    private String currentUid;

    private boolean isReaded;

    private  String  conversationId;     //会话Id

    private  boolean  isVoiceRead;    //语音已读未读

    private  String  imagepath;    //图片缓存地址

    private  long  pictureTime;    //图片缓存地址

    @Generated(hash = 761370096)
    public IMConversationDetailBean(Long _id, String companyId, int msgType,
            int lastMsgSendType, String receiverId, String senderId, String groupId,
            int sendstate, long timestamp, String data, String fingerprint,
            String userId, String width, String hight, boolean getredpick,
            String ownurl, String ownfirstFrame, String currentUid,
            boolean isReaded, String conversationId, boolean isVoiceRead,
            String imagepath, long pictureTime) {
        this._id = _id;
        this.companyId = companyId;
        this.msgType = msgType;
        this.lastMsgSendType = lastMsgSendType;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.groupId = groupId;
        this.sendstate = sendstate;
        this.timestamp = timestamp;
        this.data = data;
        this.fingerprint = fingerprint;
        this.userId = userId;
        this.width = width;
        this.hight = hight;
        this.getredpick = getredpick;
        this.ownurl = ownurl;
        this.ownfirstFrame = ownfirstFrame;
        this.currentUid = currentUid;
        this.isReaded = isReaded;
        this.conversationId = conversationId;
        this.isVoiceRead = isVoiceRead;
        this.imagepath = imagepath;
        this.pictureTime = pictureTime;
    }

    @Generated(hash = 1878626656)
    public IMConversationDetailBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getLastMsgSendType() {
        return this.lastMsgSendType;
    }

    public void setLastMsgSendType(int lastMsgSendType) {
        this.lastMsgSendType = lastMsgSendType;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getSendstate() {
        return this.sendstate;
    }

    public void setSendstate(int sendstate) {
        this.sendstate = sendstate;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHight() {
        return this.hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public boolean getGetredpick() {
        return this.getredpick;
    }

    public void setGetredpick(boolean getredpick) {
        this.getredpick = getredpick;
    }

    public String getOwnurl() {
        return this.ownurl;
    }

    public void setOwnurl(String ownurl) {
        this.ownurl = ownurl;
    }

    public String getOwnfirstFrame() {
        return this.ownfirstFrame;
    }

    public void setOwnfirstFrame(String ownfirstFrame) {
        this.ownfirstFrame = ownfirstFrame;
    }

    public String getCurrentUid() {
        return this.currentUid;
    }

    public void setCurrentUid(String currentUid) {
        this.currentUid = currentUid;
    }

    public boolean getIsReaded() {
        return this.isReaded;
    }

    public void setIsReaded(boolean isReaded) {
        this.isReaded = isReaded;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public boolean getIsVoiceRead() {
        return this.isVoiceRead;
    }

    public void setIsVoiceRead(boolean isVoiceRead) {
        this.isVoiceRead = isVoiceRead;
    }

    public String getImagepath() {
        return this.imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public long getPictureTime() {
        return this.pictureTime;
    }

    public void setPictureTime(long pictureTime) {
        this.pictureTime = pictureTime;
    }


}
