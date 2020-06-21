package com.android.nettylibrary.http;

import java.util.List;

public class IMGroupMemberBean {
    private String code;
    private List< GroupMemberDetaiData> data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<GroupMemberDetaiData> getData() {
        return data;
    }

    public void setData(List<GroupMemberDetaiData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public class GroupMemberDetaiData {
        private String blacklist;
        private String forbiddenWords;
        private String gmId;
        private long gmtCreate;
        private long gmtUpdate;
        private String groupId;
        private String groupName;
        private String logicalDeletion;
        private String memberId;
        private String messageFree;
        private String nickName;
        private String noticeReminder;
        private String remark;
        private String userRole;
        private String validTime;
        private String name;
        private String avatar;
        private String lable;
        private String level;
        private String points;    ////会员等级
        private String title;    ////会员-头衔
        private String gmtOffline;   //用户离线消息
        private String isViewHit;
        private String isViewTitle;

        public String getIsViewHit() {
            return isViewHit;
        }

        public void setIsViewHit(String isViewHit) {
            this.isViewHit = isViewHit;
        }

        public String getIsViewTitle() {
            return isViewTitle;
        }

        public void setIsViewTitle(String isViewTitle) {
            this.isViewTitle = isViewTitle;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGmtOffline() {
            return gmtOffline;
        }

        public void setGmtOffline(String gmtOffline) {
            this.gmtOffline = gmtOffline;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
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

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public long getGmtUpdate() {
            return gmtUpdate;
        }

        public void setGmtUpdate(long gmtUpdate) {
            this.gmtUpdate = gmtUpdate;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getLogicalDeletion() {
            return logicalDeletion;
        }

        public void setLogicalDeletion(String logicalDeletion) {
            this.logicalDeletion = logicalDeletion;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
        }

        public String getValidTime() {
            return validTime;
        }

        public void setValidTime(String validTime) {
            this.validTime = validTime;
        }
    }
}
