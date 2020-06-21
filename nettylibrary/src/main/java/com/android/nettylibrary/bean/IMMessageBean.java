package com.android.nettylibrary.bean;

import com.android.nettylibrary.protobuf.MessageProtobuf;
import com.android.nettylibrary.utils.IMStringUtil;

/**
 * messagege=type: TEXT
 * 2019-08-06 10:32:16.803 10408-10484/com.android.myimprotect I/System.out: companyId: 1
 * 2019-08-06 10:32:16.803 10408-10484/com.android.myimprotect I/System.out: senderId: "11"
 * 2019-08-06 10:32:16.803 10408-10484/com.android.myimprotect I/System.out: receiverId: "11"
 * 2019-08-06 10:32:16.804 10408-10484/com.android.myimprotect I/System.out: fingerprint: "41739e1d-e6b5-4ddf-b370-332344d0328b"
 * 2019-08-06 10:32:16.804 10408-10484/com.android.myimprotect I/System.out: data: "1253"
 * 2019-08-06 10:32:16.804 10408-10484/com.android.myimprotect I/System.out: timestamp: 1565058757071
 * 2019-08-06 10:32:16.804 10408-10484/com.android.myimprotect I/System.out: deviceId: "2050cdd24634a7cf"
 */

public class IMMessageBean implements Cloneable{

    private MessageProtobuf.ImMessage.TypeEnum type;//消息类型

    private MessageProtobuf.ImMessage.UserStatusEnum userStatus;//用户状态

    private String clientId ;//公司编码

    private  String senderId;//发送者id

    private String receiverId ;//接收者id

    private String groupId ;//群id

    private  String fingerprint ;//消息指纹

    private  String data;//数据

    private long timestamp;//服务端时间戳

    private  String deviceId;//设备id

    private  String myOwnUrl;


    public String getMyOwnUrl() {
        return myOwnUrl;
    }

    public void setMyOwnUrl(String myOwnUrl) {
        this.myOwnUrl = myOwnUrl;
    }

    private MessageProtobuf.ImMessage.ResponseStatusEnum responseStatus;//返回状态
    /**
     * TEXT = 0;//文本消息
     * 		PICTURE = 1;//图片消息
     * 		VOICE = 2;//语音消息
     * 		VIDEO = 3;//视频消息
     * 		FILE = 4;//文件消息
     * 		REDENVELOPES = 5;/消息
     * 		DOCUMENTARY = 6;//跟单消息
     * 		REDBILL = 7;//红单消息
     * 		PLAN = 8;//计划消息
     */
    private  int msgType;


    private String token;

    public IMMessageBean() {
    }
    public IMMessageBean(MessageProtobuf.ImMessage.TypeEnum type, MessageProtobuf.ImMessage.UserStatusEnum userStatus, String clientId, String senderId, String receiverId, String groupId, String fingerprint, String data, long timestamp, String deviceId, MessageProtobuf.ImMessage.ResponseStatusEnum responseStatus, int msgType, String token) {
        this.type = type;
        this.userStatus = userStatus;
        this.clientId = clientId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.groupId = groupId;
        this.fingerprint = fingerprint;
        this.data = data;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.responseStatus = responseStatus;
        this.msgType = msgType;
        this.token = token;
    }

    public MessageProtobuf.ImMessage.TypeEnum getType() {
        return type;
    }

    public void setType(MessageProtobuf.ImMessage.TypeEnum type) {
        this.type = type;
    }

    public MessageProtobuf.ImMessage.UserStatusEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(MessageProtobuf.ImMessage.UserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public MessageProtobuf.ImMessage.ResponseStatusEnum getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(MessageProtobuf.ImMessage.ResponseStatusEnum responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IMMessageBean)) {
            return false;
        }

        return IMStringUtil.equals(this.fingerprint, ((IMMessageBean) obj).getFingerprint());
    }

    @Override
    public int hashCode() {
        try {
            return this.getFingerprint().hashCode();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public String toString() {
        return "IMPersonBean{" +
                "type=" + type +
                ", userStatus=" + userStatus +
                ", clientId='" + clientId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", data='" + data + '\'' +
                ", timestamp=" + timestamp +
                ", deviceId='" + deviceId + '\'' +
                ", responseStatus=" + responseStatus +
                ", msgType=" + msgType +
                ", token='" + token + '\'' +
                '}';
    }
}
