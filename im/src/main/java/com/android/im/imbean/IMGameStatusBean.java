package com.android.im.imbean;

import java.util.ArrayList;
import java.util.List;

public class IMGameStatusBean {
    private String statusId;
    private String  statusName;
    private boolean isChooseed;

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public boolean isChooseed() {
        return isChooseed;
    }

    public void setChooseed(boolean chooseed) {
        isChooseed = chooseed;
    }

    public IMGameStatusBean(String statusId, String statusName, boolean isChooseed) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.isChooseed = isChooseed;
    }

    /**
     *  * state	投注状态： UNSETTLED_ACCOUNTS:未结算 NOT_WINNING_THE_PRIZE:未中奖 HAS_WON_THE_PRIZE:已中奖	是	[string]
     */
    public static List<IMGameStatusBean> getdata() {
        List<IMGameStatusBean>datas=new ArrayList<>();
        datas.add(new IMGameStatusBean("","全部",true));
        datas.add(new IMGameStatusBean("UNSETTLED_ACCOUNTS","未结算",false));
        datas.add(new IMGameStatusBean("HAS_WON_THE_PRIZE","已中奖",false));
        datas.add(new IMGameStatusBean("NOT_WINNING_THE_PRIZE","未中奖",false));
        return datas;
    }
}
