package com.android.im.imeventbus;


import com.android.nettylibrary.greendao.entity.IMPersonBean;

import java.util.List;

public class IMRegetPersonListEvent {
    private List<IMPersonBean> persons;

    public List<IMPersonBean> getGroups() {
        return persons;
    }

    public void setGroups(List<IMPersonBean> groups) {
        this.persons = groups;
    }

    public IMRegetPersonListEvent(List<IMPersonBean> groups) {
        this.persons = groups;
    }
}
