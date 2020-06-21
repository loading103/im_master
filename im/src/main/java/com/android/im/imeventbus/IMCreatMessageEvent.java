package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMConversationBean;

public class IMCreatMessageEvent {
    private IMConversationBean messageMessage;

    public IMCreatMessageEvent(IMConversationBean messageMessage) {
        this.messageMessage = messageMessage;
    }

    public IMConversationBean getMessageMessage() {
        return messageMessage;
    }

    public void setMessageMessage(IMConversationBean messageMessage) {
        this.messageMessage = messageMessage;
    }
}
