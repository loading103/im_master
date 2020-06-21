package com.android.im.imeventbus;



public class IMDeleteFriendSucessEvent {
   private  String bean;

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public IMDeleteFriendSucessEvent(String bean) {
        this.bean = bean;
    }
}
