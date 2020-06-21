package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMConversationBean;

public class IMUpdataGroupInforEvent {
    public IMConversationBean groupBean;
    public IMUpdataGroupInforEvent(IMConversationBean groupBean) {
        this.groupBean=groupBean;
    }
}
