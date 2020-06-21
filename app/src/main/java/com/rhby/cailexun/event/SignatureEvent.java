package com.rhby.cailexun.event;

/**
 * Created by Wolf on 2019/11/21.
 * Describe:
 */
public class SignatureEvent {
    private String msg;

    public SignatureEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
