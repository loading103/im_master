package com.android.im.imeventbus;


import com.android.im.imbean.IMHttpCommonBean;

public class IMCreatGroupEvent {
    private IMHttpCommonBean messageMessage;

    public IMCreatGroupEvent(IMHttpCommonBean messageMessage) {
        this.messageMessage = messageMessage;
    }

    public IMHttpCommonBean getMessageMessage() {
        return messageMessage;
    }

    public void setMessageMessage(IMHttpCommonBean messageMessage) {
        this.messageMessage = messageMessage;
    }
}
