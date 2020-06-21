package com.android.nettylibrary.http;

import java.io.Serializable;
import java.util.List;

public class IMMessageKictOutJson implements Serializable{
    private String    groupName;             //视频源文件地址
    private String    groupId;             //视频文件的第一帧截图
    private String groupOwner;
    private String    memberCount;             //文件地址

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }
}
