package com.android.nettylibrary.bean;

import java.io.Serializable;

public class IMSendingStateBean implements Serializable {
    private String fingerId ;//公司编码

    private  long time;//发送者id

    private  String conversationId;//发送者id

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFingerId() {
        return fingerId;
    }

    public void setFingerId(String fingerId) {
        this.fingerId = fingerId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public IMSendingStateBean(String fingerId, long time, String conversationId) {
        this.fingerId = fingerId;
        this.time = time;
        this.conversationId = conversationId;
    }
}
