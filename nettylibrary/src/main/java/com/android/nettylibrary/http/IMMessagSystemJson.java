package com.android.nettylibrary.http;

import java.io.Serializable;
import java.util.List;

public class IMMessagSystemJson implements Serializable{
    private     String    acceptCustomerId;             //高
    private     String    appId;             //高
    private     String    applyId;             //高
    private     String    customerId;             //高

    public String getAcceptCustomerId() {
        return acceptCustomerId;
    }

    public void setAcceptCustomerId(String acceptCustomerId) {
        this.acceptCustomerId = acceptCustomerId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
