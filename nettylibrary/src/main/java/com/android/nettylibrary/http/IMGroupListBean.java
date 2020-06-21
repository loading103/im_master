package com.android.nettylibrary.http;


import com.android.nettylibrary.greendao.entity.IMGroupBean;

import java.util.List;

public class IMGroupListBean {
    private String code;
    private List<IMGroupBean> data;
    private String message;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setData(List<IMGroupBean> data) {
        this.data = data;
    }
    public List<IMGroupBean> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
