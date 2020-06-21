package com.android.im.imeventbus;


import com.android.nettylibrary.http.IMUserInforBean;

public class IMNumberEvent {
    private int number;

    public IMNumberEvent(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
