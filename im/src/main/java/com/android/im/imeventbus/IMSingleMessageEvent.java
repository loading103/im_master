package com.android.im.imeventbus;


import com.android.nettylibrary.bean.IMMessageBean;

public class IMSingleMessageEvent {
    private IMMessageBean messageMessage;

    public IMSingleMessageEvent(IMMessageBean messageMessage) {
        this.messageMessage = messageMessage;
    }

    public IMMessageBean getMessageMessage() {
        return messageMessage;
    }

    public void setMessageMessage(IMMessageBean messageMessage) {
        this.messageMessage = messageMessage;
    }
}
