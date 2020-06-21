package com.android.nettylibrary.http;

public class IMMessageNoFriendJson {
    private String   text;                   //文字
    private String   type;                   //类型
    private String   sysCode;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
}
