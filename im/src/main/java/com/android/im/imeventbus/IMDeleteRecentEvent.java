package com.android.im.imeventbus;


public class IMDeleteRecentEvent {
    private int position;

    public IMDeleteRecentEvent() {

    }

    public IMDeleteRecentEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
