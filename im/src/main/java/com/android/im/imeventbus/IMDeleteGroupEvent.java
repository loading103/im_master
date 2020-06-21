package com.android.im.imeventbus;



public class IMDeleteGroupEvent {
    private String groupId;

    public IMDeleteGroupEvent(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
