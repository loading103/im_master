package com.android.nettylibrary.greendao.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 群列表人员信息
 */
@Entity
public class IMGroupBean implements Serializable {
    private static final long serialVersionUID = -8257666883268198804L;
    @Id(autoincrement = true)
    private Long _id;   //id,表的主键并自增长

    private String endTime;

    private long gmtCreate;

    private long gmtUpdate;

    private String groupAvatar;

    private String groupCharacteristic;

    private String groupId;

    private int groupLevel;

    private String groupName;

    private String groupOwner;

    private String inGroup;  //群主id

    private String isUse;  	//是否启用群

    private String logicalDeletion;

    private int memberCount;

    private String remark;

    private String showCharacteristic;

    private String showFriendGroup;

    private String startTime;

    private String usePageTemplate;

    /**
     * 下面几个是自己需求加的
     */
    private  int  lastMsgSendType;

    private  int  lastMsgType;

    private  long  lastMessageTime;   //最后消息时间

    private   int  lastSendstate;   //最后消息发送状态0成功  1发送中 2 失败

    private   String  lastFingerId;   //最后消息发送状态

    private   String  lastSendId;   //最后消息发送状态

    private String lastMessage;

    //消息是不是置顶
    private boolean isSetTop;  //是不是置顶

    private long setTopTime;     //置顶的时间（置顶的部分由置顶的时间戳来排序）

    private boolean ischoosed;     //分享时候是不是选中

    private String letter;

    @Generated(hash = 1393896322)
    public IMGroupBean(Long _id, String endTime, long gmtCreate, long gmtUpdate,
            String groupAvatar, String groupCharacteristic, String groupId,
            int groupLevel, String groupName, String groupOwner, String inGroup,
            String isUse, String logicalDeletion, int memberCount, String remark,
            String showCharacteristic, String showFriendGroup, String startTime,
            String usePageTemplate, int lastMsgSendType, int lastMsgType,
            long lastMessageTime, int lastSendstate, String lastFingerId,
            String lastSendId, String lastMessage, boolean isSetTop,
            long setTopTime, boolean ischoosed, String letter) {
        this._id = _id;
        this.endTime = endTime;
        this.gmtCreate = gmtCreate;
        this.gmtUpdate = gmtUpdate;
        this.groupAvatar = groupAvatar;
        this.groupCharacteristic = groupCharacteristic;
        this.groupId = groupId;
        this.groupLevel = groupLevel;
        this.groupName = groupName;
        this.groupOwner = groupOwner;
        this.inGroup = inGroup;
        this.isUse = isUse;
        this.logicalDeletion = logicalDeletion;
        this.memberCount = memberCount;
        this.remark = remark;
        this.showCharacteristic = showCharacteristic;
        this.showFriendGroup = showFriendGroup;
        this.startTime = startTime;
        this.usePageTemplate = usePageTemplate;
        this.lastMsgSendType = lastMsgSendType;
        this.lastMsgType = lastMsgType;
        this.lastMessageTime = lastMessageTime;
        this.lastSendstate = lastSendstate;
        this.lastFingerId = lastFingerId;
        this.lastSendId = lastSendId;
        this.lastMessage = lastMessage;
        this.isSetTop = isSetTop;
        this.setTopTime = setTopTime;
        this.ischoosed = ischoosed;
        this.letter = letter;
    }

    @Generated(hash = 431904894)
    public IMGroupBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtUpdate() {
        return this.gmtUpdate;
    }

    public void setGmtUpdate(long gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    public String getGroupAvatar() {
        return this.groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public String getGroupCharacteristic() {
        return this.groupCharacteristic;
    }

    public void setGroupCharacteristic(String groupCharacteristic) {
        this.groupCharacteristic = groupCharacteristic;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getGroupLevel() {
        return this.groupLevel;
    }

    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupOwner() {
        return this.groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getInGroup() {
        return this.inGroup;
    }

    public void setInGroup(String inGroup) {
        this.inGroup = inGroup;
    }

    public String getIsUse() {
        return this.isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getLogicalDeletion() {
        return this.logicalDeletion;
    }

    public void setLogicalDeletion(String logicalDeletion) {
        this.logicalDeletion = logicalDeletion;
    }

    public int getMemberCount() {
        return this.memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShowCharacteristic() {
        return this.showCharacteristic;
    }

    public void setShowCharacteristic(String showCharacteristic) {
        this.showCharacteristic = showCharacteristic;
    }

    public String getShowFriendGroup() {
        return this.showFriendGroup;
    }

    public void setShowFriendGroup(String showFriendGroup) {
        this.showFriendGroup = showFriendGroup;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUsePageTemplate() {
        return this.usePageTemplate;
    }

    public void setUsePageTemplate(String usePageTemplate) {
        this.usePageTemplate = usePageTemplate;
    }

    public int getLastMsgSendType() {
        return this.lastMsgSendType;
    }

    public void setLastMsgSendType(int lastMsgSendType) {
        this.lastMsgSendType = lastMsgSendType;
    }

    public int getLastMsgType() {
        return this.lastMsgType;
    }

    public void setLastMsgType(int lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public long getLastMessageTime() {
        return this.lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getLastSendstate() {
        return this.lastSendstate;
    }

    public void setLastSendstate(int lastSendstate) {
        this.lastSendstate = lastSendstate;
    }

    public String getLastFingerId() {
        return this.lastFingerId;
    }

    public void setLastFingerId(String lastFingerId) {
        this.lastFingerId = lastFingerId;
    }

    public String getLastSendId() {
        return this.lastSendId;
    }

    public void setLastSendId(String lastSendId) {
        this.lastSendId = lastSendId;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean getIsSetTop() {
        return this.isSetTop;
    }

    public void setIsSetTop(boolean isSetTop) {
        this.isSetTop = isSetTop;
    }

    public long getSetTopTime() {
        return this.setTopTime;
    }

    public void setSetTopTime(long setTopTime) {
        this.setTopTime = setTopTime;
    }

    public boolean getIschoosed() {
        return this.ischoosed;
    }

    public void setIschoosed(boolean ischoosed) {
        this.ischoosed = ischoosed;
    }

    public String getLetter() {
        return this.letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

}
