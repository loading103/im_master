package com.android.nettylibrary.bean;


public class IMMessageReceiveEvent {
    private String state;//0是未连接  1是接收中 2是会话

    public String isIsloading() {
        return state;
    }

    public IMMessageReceiveEvent(String isloading) {
        this.state = isloading;
    }
}
