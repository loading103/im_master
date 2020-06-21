package com.android.im.imeventbus;

import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;

public class IMAtSomeOneEvent {
    private IMGroupMemberBean data;

    public IMAtSomeOneEvent(IMGroupMemberBean data) {
        this.data = data;
    }

    public IMGroupMemberBean getData() {
        return data;
    }

    public void setData(IMGroupMemberBean data) {
        this.data = data;
    }
}
