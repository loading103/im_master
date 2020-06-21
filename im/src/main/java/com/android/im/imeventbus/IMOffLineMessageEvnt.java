package com.android.im.imeventbus;

import com.android.im.imbean.IMFriendsBean;
import com.android.im.imbean.IMMessageDataBean;

import java.util.List;

public class IMOffLineMessageEvnt {
    private String datas;

    public IMOffLineMessageEvnt(String datas) {
        this.datas = datas;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}
