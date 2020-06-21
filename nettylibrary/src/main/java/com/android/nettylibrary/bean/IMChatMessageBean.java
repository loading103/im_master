package com.android.nettylibrary.bean;


public class IMChatMessageBean {
    private String UserId;
    private String UserName;
    private String UserHeadIcon;
    private String UserContent;
    private String time;
    private int type;
    private int messagetype;
    private int sendState;
    private String imageUrl;
    private String imageIconUrl;
    private String imageLocal;

    public IMChatMessageBean(String UserId, String UserName,
                             String UserHeadIcon, String UserContent, String time, int type,
                             int messagetype, int sendState, String imageUrl, String imageIconUrl, String imageLocal) {
        this.UserId = UserId;
        this.UserName = UserName;
        this.UserHeadIcon = UserHeadIcon;
        this.UserContent = UserContent;
        this.time = time;
        this.type = type;
        this.messagetype = messagetype;
        this.sendState = sendState;
        this.imageUrl = imageUrl;
        this.imageIconUrl = imageIconUrl;
        this.imageLocal = imageLocal;
    }



    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserHeadIcon() {
        return this.UserHeadIcon;
    }

    public void setUserHeadIcon(String UserHeadIcon) {
        this.UserHeadIcon = UserHeadIcon;
    }

    public String getUserContent() {
        return this.UserContent;
    }

    public void setUserContent(String UserContent) {
        this.UserContent = UserContent;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMessagetype() {
        return this.messagetype;
    }

    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }

    public int getSendState() {
        return this.sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageIconUrl() {
        return this.imageIconUrl;
    }

    public void setImageIconUrl(String imageIconUrl) {
        this.imageIconUrl = imageIconUrl;
    }

    public String getImageLocal() {
        return this.imageLocal;
    }

    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
    }
}
