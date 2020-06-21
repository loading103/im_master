package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMGroupBean;

import java.util.List;

public class IMRegetGroupKickOutEvent {
    private IMGroupBean groups;

    public IMGroupBean getGroups() {
        return groups;
    }

    public void setGroups(IMGroupBean groups) {
        this.groups = groups;
    }

    public IMRegetGroupKickOutEvent(IMGroupBean groups) {
        this.groups = groups;
    }
}
