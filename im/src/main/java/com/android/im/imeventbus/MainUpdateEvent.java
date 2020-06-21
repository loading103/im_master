package com.android.im.imeventbus;

/**
 * Created by Wolf on 2019/11/21.
 * Describe:
 */
public class MainUpdateEvent {
    private String msg;

    public MainUpdateEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
