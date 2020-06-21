package com.android.im.imeventbus;

import com.android.nettylibrary.bean.IMMessageBean;

public class IMPushMessageEvent {
    public IMMessageBean message;

    public IMPushMessageEvent(IMMessageBean message) {
        this.message = message;
    }

    public IMMessageBean getContent() {
        return message;
    }

}
