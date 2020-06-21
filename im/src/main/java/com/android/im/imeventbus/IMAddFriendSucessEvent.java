package com.android.im.imeventbus;


import com.android.nettylibrary.http.IMUserInforBean;

public class IMAddFriendSucessEvent {
   private  IMUserInforBean.UserInforData bean;

    public IMUserInforBean.UserInforData getBean() {
        return bean;
    }

    public void setBean(IMUserInforBean.UserInforData bean) {
        this.bean = bean;
    }

    public IMAddFriendSucessEvent(IMUserInforBean.UserInforData bean) {
        this.bean = bean;
    }
}
