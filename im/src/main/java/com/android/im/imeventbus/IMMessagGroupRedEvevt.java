package com.android.im.imeventbus;

import com.android.im.imbean.IMSingleRedBackBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;

public class IMMessagGroupRedEvevt {
    private IMSingleRedBackBean imRedBackBean;
    private IMGroupBean group;

    public IMSingleRedBackBean getImRedBackBean() {
        return imRedBackBean;
    }

    public void setImRedBackBean(IMSingleRedBackBean imRedBackBean) {
        this.imRedBackBean = imRedBackBean;
    }

    public IMGroupBean getGroup() {
        return group;
    }

    public void setGroup(IMGroupBean group) {
        this.group = group;
    }

    public IMMessagGroupRedEvevt(IMSingleRedBackBean imRedBackBean, IMGroupBean group) {
        this.imRedBackBean = imRedBackBean;
        this.group = group;
    }
}
