package com.android.im.imeventbus;

import com.android.im.imbean.IMFriendsBean;

import java.util.List;

public class IMNewFriendUnReadNumber {
    private List<IMFriendsBean> datas;

    public IMNewFriendUnReadNumber(List<IMFriendsBean> datas) {
        this.datas = datas;
    }

    public List<IMFriendsBean> getDatas() {
        return datas;
    }

    public void setDatas(List<IMFriendsBean> datas) {
        this.datas = datas;
    }
}
