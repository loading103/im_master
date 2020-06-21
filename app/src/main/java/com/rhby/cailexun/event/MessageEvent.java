package com.rhby.cailexun.event;

/**
 * Created by Wolf on 2019/11/21.
 * Describe:
 */
public class MessageEvent {
    private String msg;

    public MessageEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
