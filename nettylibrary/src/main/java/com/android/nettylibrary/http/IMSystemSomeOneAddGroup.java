package com.android.nettylibrary.http;

import java.io.Serializable;
import java.util.List;

public class IMSystemSomeOneAddGroup implements Serializable{
    private String avatar;
    private String blacklist;
    private String forbiddenWords;
    private String gmId;
    private String groupId;
    private String memberId;
    private String messageFree;
    private String nickName;
    private String noticeReminder;
    private String title;
    private String userRole;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public String getForbiddenWords() {
        return forbiddenWords;
    }

    public void setForbiddenWords(String forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }

    public String getGmId() {
        return gmId;
    }

    public void setGmId(String gmId) {
        this.gmId = gmId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMessageFree() {
        return messageFree;
    }

    public void setMessageFree(String messageFree) {
        this.messageFree = messageFree;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNoticeReminder() {
        return noticeReminder;
    }

    public void setNoticeReminder(String noticeReminder) {
        this.noticeReminder = noticeReminder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
