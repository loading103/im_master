package com.android.im.imeventbus;



public class IMDeleteFriendSucessedEvent {
   private  String bean;

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public IMDeleteFriendSucessedEvent(String bean) {
        this.bean = bean;
    }
}
