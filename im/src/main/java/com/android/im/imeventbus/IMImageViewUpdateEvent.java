package com.android.im.imeventbus;



public class IMImageViewUpdateEvent {
    private  String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IMImageViewUpdateEvent(String url) {
        this.url = url;
    }

    public IMImageViewUpdateEvent() {
    }
}
