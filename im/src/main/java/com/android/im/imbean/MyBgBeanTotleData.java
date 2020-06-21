package com.android.im.imbean;


import java.io.Serializable;

public class MyBgBeanTotleData implements Serializable {
    private String  picurl;
    private int type;

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyBgBeanTotleData(String picurl, int type) {
        this.picurl = picurl;
        this.type = type;
    }
}
