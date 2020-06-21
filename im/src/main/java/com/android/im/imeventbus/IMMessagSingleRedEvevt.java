package com.android.im.imeventbus;

import com.android.im.imbean.IMBetDetailBean;
import com.android.im.imbean.IMSingleRedBackBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;

import java.util.List;

public class IMMessagSingleRedEvevt {
    private IMSingleRedBackBean imRedBackBean;
    private IMPersonBean customer;

    public IMSingleRedBackBean getImRedBackBean() {
        return imRedBackBean;
    }

    public void setImRedBackBean(IMSingleRedBackBean imRedBackBean) {
        this.imRedBackBean = imRedBackBean;
    }

    public IMPersonBean getCustomer() {
        return customer;
    }

    public void setCustomer(IMPersonBean customer) {
        this.customer = customer;
    }

    public IMMessagSingleRedEvevt(IMSingleRedBackBean imRedBackBean, IMPersonBean customer) {
        this.imRedBackBean = imRedBackBean;
        this.customer = customer;
    }
}
