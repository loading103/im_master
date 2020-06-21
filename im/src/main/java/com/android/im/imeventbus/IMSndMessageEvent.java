package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;

public class IMSndMessageEvent {
    private String conversationId;
    private IMConversationDetailBean data;
    private boolean issend;

    public boolean isIssend() {
        return issend;
    }

    public void setIssend(boolean issend) {
        this.issend = issend;
    }

    public String getCustomId() {
        return conversationId;
    }

    public void setCustomId(String customId) {
        this.conversationId = customId;
    }

    public IMConversationDetailBean getData() {
        return data;
    }

    public void setData(IMConversationDetailBean data) {
        this.data = data;
    }

    public IMSndMessageEvent(String conversationId, IMConversationDetailBean data) {
        this.conversationId = conversationId;
        this.data = data;
    }
}
