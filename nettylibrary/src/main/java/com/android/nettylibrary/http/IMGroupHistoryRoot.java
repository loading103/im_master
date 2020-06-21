package com.android.nettylibrary.http;

import java.util.List;

public class IMGroupHistoryRoot {
    private String code;
    private GroupHistoryBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GroupHistoryBean getData() {
        return data;
    }

    public void setData(GroupHistoryBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class GroupHistoryBean {
        private int current;
        private int pages;
        private List<GroupHistoryDetaiData> records;
        private int size;
        private int total;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<GroupHistoryDetaiData> getRecords() {
            return records;
        }

        public void setRecords(List<GroupHistoryDetaiData> records) {
            this.records = records;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
    public class GroupHistoryDetaiData {
        private String data;
        private String fingerprint;
        private String groupId;
        private String receiverAvatar;
        private String receiverId;
        private String receiverName;
        private long sendTime;
        private String senderAvatar;
        private String senderId;
        private String senderName;
        private int type;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getReceiverAvatar() {
            return receiverAvatar;
        }

        public void setReceiverAvatar(String receiverAvatar) {
            this.receiverAvatar = receiverAvatar;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public long getSendTime() {
            return sendTime;
        }

        public void setSendTime(long sendTime) {
            this.sendTime = sendTime;
        }

        public String getSenderAvatar() {
            return senderAvatar;
        }

        public void setSenderAvatar(String senderAvatar) {
            this.senderAvatar = senderAvatar;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}