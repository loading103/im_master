package com.android.im.imeventbus;


import com.android.nettylibrary.bean.IMMessageBean;

public class IMGroupMessageEvent {
    private IMMessageBean messageMessage;
    private boolean isgroup;
    public IMGroupMessageEvent(IMMessageBean messageMessage,boolean isgroup) {
        this.messageMessage = messageMessage;
        this.isgroup = isgroup;
    }

    public boolean isIsgroup() {
        return isgroup;
    }

    public void setIsgroup(boolean isgroup) {
        this.isgroup = isgroup;
    }

    public IMMessageBean getMessageMessage() {
        return messageMessage;
    }

    public void setMessageMessage(IMMessageBean messageMessage) {
        this.messageMessage = messageMessage;
    }
}
