package com.android.im.imeventbus;


public class IMCancerCollectionEvent {
    private int position;

    public IMCancerCollectionEvent() {

    }

    public IMCancerCollectionEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
