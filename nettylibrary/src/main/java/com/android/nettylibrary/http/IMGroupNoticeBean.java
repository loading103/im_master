package com.android.nettylibrary.http;

import java.io.Serializable;
import java.util.List;

public class IMGroupNoticeBean implements Serializable {
    private  String code;
    private  String message;
    private  IMGroupNoticeDetail data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IMGroupNoticeDetail getData() {
        return data;
    }

    public void setData(IMGroupNoticeDetail data) {
        this.data = data;
    }


    public class IMGroupNoticeMetaBean {
        private  IMGroupNoticeDetail meta;

        public IMGroupNoticeDetail getMeta() {
            return meta;
        }

        public void setMeta(IMGroupNoticeDetail meta) {
            this.meta = meta;
        }
    }
    public class IMGroupNoticeDetail implements Serializable{
        private GroupNotice groupNotice;
        private String groupOwner;
        private String groupId;
        private String groupAvatar;
        private int memberCount;
        private String blacklist;
        private String groupCharacteristic;
        private String noticeReminder;
        private GroupFunction groupFunction;
        private String groupName;
        private String usePageTemplate;
        private String startTime;
        private String endTime;
        private String showCharacteristic;
        private String messageFree;
        private Config config;
        private String forbiddenWords;

        public GroupNotice getGroupNotice() {
            return groupNotice;
        }

        public void setGroupNotice(GroupNotice groupNotice) {
            this.groupNotice = groupNotice;
        }

        public String getGroupOwner() {
            return groupOwner;
        }

        public void setGroupOwner(String groupOwner) {
            this.groupOwner = groupOwner;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupAvatar() {
            return groupAvatar;
        }

        public void setGroupAvatar(String groupAvatar) {
            this.groupAvatar = groupAvatar;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public String getBlacklist() {
            return blacklist;
        }

        public void setBlacklist(String blacklist) {
            this.blacklist = blacklist;
        }

        public String getGroupCharacteristic() {
            return groupCharacteristic;
        }

        public void setGroupCharacteristic(String groupCharacteristic) {
            this.groupCharacteristic = groupCharacteristic;
        }

        public String getNoticeReminder() {
            return noticeReminder;
        }

        public void setNoticeReminder(String noticeReminder) {
            this.noticeReminder = noticeReminder;
        }

        public GroupFunction getGroupFunction() {
            return groupFunction;
        }

        public void setGroupFunction(GroupFunction groupFunction) {
            this.groupFunction = groupFunction;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getUsePageTemplate() {
            return usePageTemplate;
        }

        public void setUsePageTemplate(String usePageTemplate) {
            this.usePageTemplate = usePageTemplate;
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

        public String getShowCharacteristic() {
            return showCharacteristic;
        }

        public void setShowCharacteristic(String showCharacteristic) {
            this.showCharacteristic = showCharacteristic;
        }

        public String getMessageFree() {
            return messageFree;
        }

        public void setMessageFree(String messageFree) {
            this.messageFree = messageFree;
        }

        public Config getConfig() {
            return config;
        }

        public void setConfig(Config config) {
            this.config = config;
        }

        public String getForbiddenWords() {
            return forbiddenWords;
        }

        public void setForbiddenWords(String forbiddenWords) {
            this.forbiddenWords = forbiddenWords;
        }
    }

    public class GroupNotice implements Serializable{
        private String tipBoxStatus;
        private String pictureContent;
        private String fixedNoticeStatus;
        private int tipBoxInterval;
        private String textStatus;
        private String textContent;
        private String pictureStatus;
        private String fixedNoticeContent;
        private List<String> scrollNoticeContent;
        private String scrollNoticeStatus;
        private int tipBoxRule;

        public String getTipBoxStatus() {
            return tipBoxStatus;
        }

        public void setTipBoxStatus(String tipBoxStatus) {
            this.tipBoxStatus = tipBoxStatus;
        }

        public String getPictureContent() {
            return pictureContent;
        }

        public void setPictureContent(String pictureContent) {
            this.pictureContent = pictureContent;
        }

        public String getFixedNoticeStatus() {
            return fixedNoticeStatus;
        }

        public void setFixedNoticeStatus(String fixedNoticeStatus) {
            this.fixedNoticeStatus = fixedNoticeStatus;
        }

        public int getTipBoxInterval() {
            return tipBoxInterval;
        }

        public void setTipBoxInterval(int tipBoxInterval) {
            this.tipBoxInterval = tipBoxInterval;
        }

        public String getTextStatus() {
            return textStatus;
        }

        public void setTextStatus(String textStatus) {
            this.textStatus = textStatus;
        }

        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public String getPictureStatus() {
            return pictureStatus;
        }

        public void setPictureStatus(String pictureStatus) {
            this.pictureStatus = pictureStatus;
        }

        public String getFixedNoticeContent() {
            return fixedNoticeContent;
        }

        public void setFixedNoticeContent(String fixedNoticeContent) {
            this.fixedNoticeContent = fixedNoticeContent;
        }

        public List<String> getScrollNoticeContent() {
            return scrollNoticeContent;
        }

        public void setScrollNoticeContent(List<String> scrollNoticeContent) {
            this.scrollNoticeContent = scrollNoticeContent;
        }

        public String getScrollNoticeStatus() {
            return scrollNoticeStatus;
        }

        public void setScrollNoticeStatus(String scrollNoticeStatus) {
            this.scrollNoticeStatus = scrollNoticeStatus;
        }

        public int getTipBoxRule() {
            return tipBoxRule;
        }

        public void setTipBoxRule(int tipBoxRule) {
            this.tipBoxRule = tipBoxRule;
        }
    }

    public class GroupFunction implements Serializable{
        private String betsSwitch;
        private String textSwitch;
        private String pictureSwitch;
        private String redEnvSwitch;
        private String redBillSwitch;

        public String getBetsSwitch() {
            return betsSwitch;
        }

        public void setBetsSwitch(String betsSwitch) {
            this.betsSwitch = betsSwitch;
        }

        public String getTextSwitch() {
            return textSwitch;
        }

        public void setTextSwitch(String textSwitch) {
            this.textSwitch = textSwitch;
        }

        public String getPictureSwitch() {
            return pictureSwitch;
        }

        public void setPictureSwitch(String pictureSwitch) {
            this.pictureSwitch = pictureSwitch;
        }

        public String getRedEnvSwitch() {
            return redEnvSwitch;
        }

        public void setRedEnvSwitch(String redEnvSwitch) {
            this.redEnvSwitch = redEnvSwitch;
        }

        public String getRedBillSwitch() {
            return redBillSwitch;
        }

        public void setRedBillSwitch(String redBillSwitch) {
            this.redBillSwitch = redBillSwitch;
        }
    }

    public class Config implements Serializable{
        private String betsStatus;
        private int minGain;
        private int minTitle;
        private String titleStatus;
        private int minBets;
        private String redEnvStatus;
        private String callStatus;
        private String gainStatus;
        private int minRedEnv;
        private int minHit;
        private String hitStatus;

        public String getBetsStatus() {
            return betsStatus;
        }

        public void setBetsStatus(String betsStatus) {
            this.betsStatus = betsStatus;
        }

        public int getMinGain() {
            return minGain;
        }

        public void setMinGain(int minGain) {
            this.minGain = minGain;
        }

        public int getMinTitle() {
            return minTitle;
        }

        public void setMinTitle(int minTitle) {
            this.minTitle = minTitle;
        }

        public String getTitleStatus() {
            return titleStatus;
        }

        public void setTitleStatus(String titleStatus) {
            this.titleStatus = titleStatus;
        }

        public int getMinBets() {
            return minBets;
        }

        public void setMinBets(int minBets) {
            this.minBets = minBets;
        }

        public String getRedEnvStatus() {
            return redEnvStatus;
        }

        public void setRedEnvStatus(String redEnvStatus) {
            this.redEnvStatus = redEnvStatus;
        }

        public String getCallStatus() {
            return callStatus;
        }

        public void setCallStatus(String callStatus) {
            this.callStatus = callStatus;
        }

        public String getGainStatus() {
            return gainStatus;
        }

        public void setGainStatus(String gainStatus) {
            this.gainStatus = gainStatus;
        }

        public int getMinRedEnv() {
            return minRedEnv;
        }

        public void setMinRedEnv(int minRedEnv) {
            this.minRedEnv = minRedEnv;
        }

        public int getMinHit() {
            return minHit;
        }

        public void setMinHit(int minHit) {
            this.minHit = minHit;
        }

        public String getHitStatus() {
            return hitStatus;
        }

        public void setHitStatus(String hitStatus) {
            this.hitStatus = hitStatus;
        }
    }
}
