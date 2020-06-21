package com.android.im.imbean;

import java.io.Serializable;

public class IMChoosePicBean implements Serializable {
    private     int res;
    private     String url;
    private int  position;
    private boolean choosed;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public IMChoosePicBean(int position, String url) {
        this.position=position;
        this.url = url;
    }
}
