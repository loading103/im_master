package com.android.im.imbean;

import java.util.List;

/**
 * state	投注状态： UNSETTLED_ACCOUNTS:未结算 NOT_WINNING_THE_PRIZE:未中奖 HAS_WON_THE_PRIZE:已中奖	是	[string]
 * 2	type	投注类型，具体见下面	是	[string]
 * 3	time	时间筛选:1-本月,2-今日	是	[string]
 * 4	pageNo	分页-页码	是	[string]bettingOrderId
 * 5	pageSize	每页条数	是	[s
 */
public class IMBetGetBean {
    private String state;
    private String type;
    private String time;
    private String pageNo;
    private String pageSize;
    private String bettingOrderId;
    private String gameId;
    private String bettingOnlyAmount;
    private List<String> groupIds;
    private String startTime;
    private String endTime;
    private String packetId;
    private String userType;
    private String base64Data;
    private String avatar;
    private String groupId;
    private List<String> memberIdList;
    private String q;
    private String gmId;

    private String groupAvatar;
    private String groupName;
    private String fixedNoticeStatus;
    private String fixedNoticeContent;

    private String nickName;
    private String bgUrl;
    private String isFriendValid;
    private String signature;
    private String locationCode;
    private String programId;
    private String appId;
    private String collectionIds;
    private String searchKeyword;
    private String current;
    private String size;
    private String useIds;
    private String systemType;

    private String respondentsObjId;
    private String complaintType;
    private String complaintContent;
    private List<String> complaintImageList;

    public String getRespondentsObjId() {
        return respondentsObjId;
    }

    public void setRespondentsObjId(String respondentsObjId) {
        this.respondentsObjId = respondentsObjId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintContent() {
        return complaintContent;
    }

    public void setComplaintContent(String complaintContent) {
        this.complaintContent = complaintContent;
    }

    public List<String> getComplaintImageList() {
        return complaintImageList;
    }

    public void setComplaintImageList(List<String> complaintImageList) {
        this.complaintImageList = complaintImageList;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getUseIds() {
        return useIds;
    }

    public void setUseIds(String useIds) {
        this.useIds = useIds;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(String collectionIds) {
        this.collectionIds = collectionIds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getBettingOrderId() {
        return bettingOrderId;
    }

    public void setBettingOrderId(String bettingOrderId) {
        this.bettingOrderId = bettingOrderId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBettingOnlyAmount() {
        return bettingOnlyAmount;
    }

    public void setBettingOnlyAmount(String bettingOnlyAmount) {
        this.bettingOnlyAmount = bettingOnlyAmount;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberIdList() {
        return memberIdList;
    }

    public void setMemberIdList(List<String> memberIdList) {
        this.memberIdList = memberIdList;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getGmId() {
        return gmId;
    }

    public void setGmId(String gmId) {
        this.gmId = gmId;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFixedNoticeStatus() {
        return fixedNoticeStatus;
    }

    public void setFixedNoticeStatus(String fixedNoticeStatus) {
        this.fixedNoticeStatus = fixedNoticeStatus;
    }

    public String getFixedNoticeContent() {
        return fixedNoticeContent;
    }

    public void setFixedNoticeContent(String fixedNoticeContent) {
        this.fixedNoticeContent = fixedNoticeContent;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getIsFriendValid() {
        return isFriendValid;
    }

    public void setIsFriendValid(String isFriendValid) {
        this.isFriendValid = isFriendValid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
