package com.android.nettylibrary.http;

import java.util.List;

public class IMMessageIdBeans {
    private List<String> data;
    private String message;
    private String status;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
