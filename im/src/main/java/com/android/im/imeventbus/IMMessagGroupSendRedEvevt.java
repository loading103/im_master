package com.android.im.imeventbus;

import com.android.nettylibrary.http.IMMessageDataJson;

public class IMMessagGroupSendRedEvevt {
    private IMMessageDataJson bean;

    public IMMessageDataJson getBean() {
        return bean;
    }

    public void setBean(IMMessageDataJson bean) {
        this.bean = bean;
    }

    public IMMessagGroupSendRedEvevt(IMMessageDataJson bean) {
        this.bean = bean;
    }
}
