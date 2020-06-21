package com.android.nettylibrary.http;


import com.android.nettylibrary.greendao.entity.IMPersonBean;

import java.util.List;

public class IMConverListBean {
    private String code;
    private String message;
    private List<IMPersonBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<IMPersonBean> getData() {
        return data;
    }

    public void setData(List<IMPersonBean> data) {
        this.data = data;
    }
}
