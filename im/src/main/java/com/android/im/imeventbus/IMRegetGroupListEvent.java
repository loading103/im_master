package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMGroupBean;

import java.util.List;

public class IMRegetGroupListEvent {
    private List<IMGroupBean> groups;

    public List<IMGroupBean> getGroups() {
        return groups;
    }

    public void setGroups(List<IMGroupBean> groups) {
        this.groups = groups;
    }

    public IMRegetGroupListEvent(List<IMGroupBean> groups) {
        this.groups = groups;
    }
}
