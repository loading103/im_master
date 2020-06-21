package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.http.IMGroupNoticeBean;

public class IMUpdataGroupNoticeEvent {
    public IMGroupNoticeBean groupInforbean;
    public IMUpdataGroupNoticeEvent(IMGroupNoticeBean groupInforbean) {
        this.groupInforbean=groupInforbean;
    }
}
