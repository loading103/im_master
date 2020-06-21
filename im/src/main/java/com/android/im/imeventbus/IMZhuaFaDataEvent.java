package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;

public class IMZhuaFaDataEvent {
    private IMConversationDetailBean bean;
    private String  conversonId;

    public IMZhuaFaDataEvent(IMConversationDetailBean bean, String conversonId) {
        this.bean = bean;
        this.conversonId = conversonId;
    }

    public IMConversationDetailBean getBean() {
        return bean;
    }

    public void setBean(IMConversationDetailBean bean) {
        this.bean = bean;
    }

    public String getConversonId() {
        return conversonId;
    }

    public void setConversonId(String conversonId) {
        this.conversonId = conversonId;
    }
}
