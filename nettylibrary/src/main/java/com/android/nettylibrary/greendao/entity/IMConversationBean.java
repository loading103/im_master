package com.android.nettylibrary.greendao.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 群列表人员信息
 */
@Entity
public class IMConversationBean implements Serializable {
    private static final long serialVersionUID = -625351860612480701L;
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

    private String lotteryBalance;  //客服-可分配的彩金

    private String groupType;  //客服-分组

    private String isCharacteristic;   //客服-是否使用标识

    private String signature;   //	客服-个性签名

    private String sex;  //客服-性别：1=男，2=女，3=其他

    private String usePageTemplate;  //客服-使用模板

    private String nickname;  //客服-昵称

    private String name;   //客服-名字

    private String mobile;

    private String characteristic;  //客服-标识

    private String avatar;    ////客服-头像

    private String level;    ////会员等级

    private String points;    ////会员等级

    private String title;    ////会员-头衔

    private String lable;    ////	用户标签

    private String gmtOffline;   //用户离线消息

    private String memberId;

    private String  status;
    /**
     * 下面几个是自己需求加的
     */
    private  String  conversationId;     //会话Id

    private  String  conversationName;     //会话名称

    private  String  conversationavatar;     //会话头像



    private  int  lastMessageSendType;   //最后发送者消息的类型（是不是自己发送的）

    private  int  lastMessageType;  //最后发送消息的类型（音频图片）

    private  long  lastMessageTime;   //最后消息时间

    private   int  lastMessageSendstate;   //最后消息发送状态0成功  1发送中 2 失败

    private   String  lastMessageId;   //最后消息发送ID

    private   String  lastSendId;   //最后消息发送状态

    private String lastMessageContent; //最后消息发送内容
    //消息是不是置顶
    private boolean isSetTop;  //是不是置顶

    private long setTopTime;     //置顶的时间（置顶的部分由置顶的时间戳来排序）

    private boolean ischoosed;     //分享时候是不是选中

    private String currentUid;

    private String isOnline;   //用户离线
    private String isReaded;   //已读未读 0是自己没发过消息 1是发出对方未读  2是已读
    @Generated(hash = 2078446142)
    public IMConversationBean(Long _id, String endTime, long gmtCreate,
            long gmtUpdate, String groupAvatar, String groupCharacteristic,
            String groupId, int groupLevel, String groupName, String groupOwner,
            String inGroup, String isUse, String logicalDeletion, int memberCount,
            String remark, String showCharacteristic, String showFriendGroup,
            String startTime, String lotteryBalance, String groupType,
            String isCharacteristic, String signature, String sex,
            String usePageTemplate, String nickname, String name, String mobile,
            String characteristic, String avatar, String level, String points,
            String title, String lable, String gmtOffline, String memberId,
            String status, String conversationId, String conversationName,
            String conversationavatar, int lastMessageSendType, int lastMessageType,
            long lastMessageTime, int lastMessageSendstate, String lastMessageId,
            String lastSendId, String lastMessageContent, boolean isSetTop,
            long setTopTime, boolean ischoosed, String currentUid, String isOnline,
            String isReaded) {
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
        this.lotteryBalance = lotteryBalance;
        this.groupType = groupType;
        this.isCharacteristic = isCharacteristic;
        this.signature = signature;
        this.sex = sex;
        this.usePageTemplate = usePageTemplate;
        this.nickname = nickname;
        this.name = name;
        this.mobile = mobile;
        this.characteristic = characteristic;
        this.avatar = avatar;
        this.level = level;
        this.points = points;
        this.title = title;
        this.lable = lable;
        this.gmtOffline = gmtOffline;
        this.memberId = memberId;
        this.status = status;
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.conversationavatar = conversationavatar;
        this.lastMessageSendType = lastMessageSendType;
        this.lastMessageType = lastMessageType;
        this.lastMessageTime = lastMessageTime;
        this.lastMessageSendstate = lastMessageSendstate;
        this.lastMessageId = lastMessageId;
        this.lastSendId = lastSendId;
        this.lastMessageContent = lastMessageContent;
        this.isSetTop = isSetTop;
        this.setTopTime = setTopTime;
        this.ischoosed = ischoosed;
        this.currentUid = currentUid;
        this.isOnline = isOnline;
        this.isReaded = isReaded;
    }
    @Generated(hash = 1582591681)
    public IMConversationBean() {
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
    public String getLotteryBalance() {
        return this.lotteryBalance;
    }
    public void setLotteryBalance(String lotteryBalance) {
        this.lotteryBalance = lotteryBalance;
    }
    public String getGroupType() {
        return this.groupType;
    }
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
    public String getIsCharacteristic() {
        return this.isCharacteristic;
    }
    public void setIsCharacteristic(String isCharacteristic) {
        this.isCharacteristic = isCharacteristic;
    }
    public String getSignature() {
        return this.signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getUsePageTemplate() {
        return this.usePageTemplate;
    }
    public void setUsePageTemplate(String usePageTemplate) {
        this.usePageTemplate = usePageTemplate;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getCharacteristic() {
        return this.characteristic;
    }
    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getLevel() {
        return this.level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getPoints() {
        return this.points;
    }
    public void setPoints(String points) {
        this.points = points;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLable() {
        return this.lable;
    }
    public void setLable(String lable) {
        this.lable = lable;
    }
    public String getGmtOffline() {
        return this.gmtOffline;
    }
    public void setGmtOffline(String gmtOffline) {
        this.gmtOffline = gmtOffline;
    }
    public String getMemberId() {
        return this.memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getConversationId() {
        return this.conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    public String getConversationName() {
        return this.conversationName;
    }
    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }
    public String getConversationavatar() {
        return this.conversationavatar;
    }
    public void setConversationavatar(String conversationavatar) {
        this.conversationavatar = conversationavatar;
    }
    public int getLastMessageSendType() {
        return this.lastMessageSendType;
    }
    public void setLastMessageSendType(int lastMessageSendType) {
        this.lastMessageSendType = lastMessageSendType;
    }
    public int getLastMessageType() {
        return this.lastMessageType;
    }
    public void setLastMessageType(int lastMessageType) {
        this.lastMessageType = lastMessageType;
    }
    public long getLastMessageTime() {
        return this.lastMessageTime;
    }
    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
    public int getLastMessageSendstate() {
        return this.lastMessageSendstate;
    }
    public void setLastMessageSendstate(int lastMessageSendstate) {
        this.lastMessageSendstate = lastMessageSendstate;
    }
    public String getLastMessageId() {
        return this.lastMessageId;
    }
    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
    public String getLastSendId() {
        return this.lastSendId;
    }
    public void setLastSendId(String lastSendId) {
        this.lastSendId = lastSendId;
    }
    public String getLastMessageContent() {
        return this.lastMessageContent;
    }
    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
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
    public String getCurrentUid() {
        return this.currentUid;
    }
    public void setCurrentUid(String currentUid) {
        this.currentUid = currentUid;
    }
    public String getIsOnline() {
        return this.isOnline;
    }
    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
    public String getIsReaded() {
        return this.isReaded;
    }
    public void setIsReaded(String isReaded) {
        this.isReaded = isReaded;
    }

    
}
