package com.android.im.imeventbus;



public class IMMessageNoticeContentEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IMMessageNoticeContentEvent(String message) {
        this.message = message;
    }
}
