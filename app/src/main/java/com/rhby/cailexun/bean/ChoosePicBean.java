package com.rhby.cailexun.bean;

import java.io.Serializable;

public class ChoosePicBean implements Serializable {
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

    public ChoosePicBean(int position,int res) {
        this.position=position;
        this.res = res;
    }
}
