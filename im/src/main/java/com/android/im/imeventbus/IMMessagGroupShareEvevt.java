package com.android.im.imeventbus;

import com.android.im.imbean.IMBetDetailBean;

import java.util.List;

public class IMMessagGroupShareEvevt {
    private IMBetDetailBean bean;
    private List<String> groups;
    private String shareType;

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public IMBetDetailBean getBean() {
        return bean;
    }

    public void setBean(IMBetDetailBean bean) {
        this.bean = bean;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public IMMessagGroupShareEvevt(IMBetDetailBean bean, List<String> groups, String shareType) {
        this.bean = bean;
        this.groups = groups;
        this.shareType = shareType;
    }
}
