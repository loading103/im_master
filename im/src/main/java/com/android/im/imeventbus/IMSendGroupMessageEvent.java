package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;

public class IMSendGroupMessageEvent {
    private String groupId;
    private IMConversationDetailBean data;

    public IMSendGroupMessageEvent(String groupId, IMConversationDetailBean data) {
        this.groupId = groupId;
        this.data = data;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public IMConversationDetailBean getData() {
        return data;
    }

    public void setData(IMConversationDetailBean data) {
        this.data = data;
    }
}
