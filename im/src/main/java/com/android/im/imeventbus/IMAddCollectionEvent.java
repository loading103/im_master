package com.android.im.imeventbus;


import com.android.im.imbean.AddCollectionBean;
import com.android.nettylibrary.http.IMUserInforBean;

public class IMAddCollectionEvent {
    private  AddCollectionBean bean;

    public IMAddCollectionEvent(AddCollectionBean bean) {
        this.bean = bean;
    }

    public AddCollectionBean getBean() {
        return bean;
    }

    public void setBean(AddCollectionBean bean) {
        this.bean = bean;
    }
}
