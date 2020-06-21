package com.android.im.imnet.bean;

public class IMLoginData {
    private  String  authParam;  //第三方传入的会员ID
    private  String  signature;  //签名字符串
    private  String  nonceStr;  //随机字符串
    private  String  timestamp;

    public String getAuthParam() {
        return authParam;
    }

    public void setAuthParam(String authParam) {
        this.authParam = authParam;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public IMLoginData( ) {

    }

    public IMLoginData(String authParam, String signature, String nonceStr, String timestamp) {
        this.authParam = authParam;
        this.signature = signature;
        this.nonceStr = nonceStr;
        this.timestamp = timestamp;
    }
}