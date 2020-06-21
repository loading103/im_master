package com.android.im.imbean;


import java.util.List;

public class MyBgBeanData {
    private List<String>chatBackgroupList;
    private List<String>selfBackgroupList;

    public List<String> getChatBackgroupList() {
        return chatBackgroupList;
    }

    public void setChatBackgroupList(List<String> chatBackgroupList) {
        this.chatBackgroupList = chatBackgroupList;
    }

    public List<String> getSelfBackgroupList() {
        return selfBackgroupList;
    }

    public void setSelfBackgroupList(List<String> selfBackgroupList) {
        this.selfBackgroupList = selfBackgroupList;
    }
}
