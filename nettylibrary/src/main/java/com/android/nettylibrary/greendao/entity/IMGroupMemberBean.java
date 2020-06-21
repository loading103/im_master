package com.android.nettylibrary.greendao.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 群列表人员信息
 */
@Entity
public class IMGroupMemberBean implements Serializable {
    private static final long serialVersionUID = -1538226146123982895L;
    @Id(autoincrement = true)
    private Long _id;   //id,表的主键并自增长
    private String groupId;
    private String groupName;
    private String memberId;
    private String nickName;
    private String avatar;
    private String lable;
    private String level;
    private String points;    ////会员等级
    private String title;    ////会员-头衔
    private String gmtOffline;   //用户离线消息
    private String blacklist;   //是不是黑名单
    private String forbiddenWords; //是不是被禁言
    private String isViewHit;
    private String isViewTitle;
    private String letter;
    private boolean ischoosed;
    private String  gmId;
    @Generated(hash = 2136012651)
    public IMGroupMemberBean(Long _id, String groupId, String groupName,
            String memberId, String nickName, String avatar, String lable,
            String level, String points, String title, String gmtOffline,
            String blacklist, String forbiddenWords, String isViewHit,
            String isViewTitle, String letter, boolean ischoosed, String gmId) {
        this._id = _id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberId = memberId;
        this.nickName = nickName;
        this.avatar = avatar;
        this.lable = lable;
        this.level = level;
        this.points = points;
        this.title = title;
        this.gmtOffline = gmtOffline;
        this.blacklist = blacklist;
        this.forbiddenWords = forbiddenWords;
        this.isViewHit = isViewHit;
        this.isViewTitle = isViewTitle;
        this.letter = letter;
        this.ischoosed = ischoosed;
        this.gmId = gmId;
    }
    @Generated(hash = 258078178)
    public IMGroupMemberBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getMemberId() {
        return this.memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getLable() {
        return this.lable;
    }
    public void setLable(String lable) {
        this.lable = lable;
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
    public String getGmtOffline() {
        return this.gmtOffline;
    }
    public void setGmtOffline(String gmtOffline) {
        this.gmtOffline = gmtOffline;
    }
    public String getBlacklist() {
        return this.blacklist;
    }
    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }
    public String getForbiddenWords() {
        return this.forbiddenWords;
    }
    public void setForbiddenWords(String forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }
    public String getIsViewHit() {
        return this.isViewHit;
    }
    public void setIsViewHit(String isViewHit) {
        this.isViewHit = isViewHit;
    }
    public String getIsViewTitle() {
        return this.isViewTitle;
    }
    public void setIsViewTitle(String isViewTitle) {
        this.isViewTitle = isViewTitle;
    }
    public String getLetter() {
        return this.letter;
    }
    public void setLetter(String letter) {
        this.letter = letter;
    }
    public boolean getIschoosed() {
        return this.ischoosed;
    }
    public void setIschoosed(boolean ischoosed) {
        this.ischoosed = ischoosed;
    }
    public String getGmId() {
        return this.gmId;
    }
    public void setGmId(String gmId) {
        this.gmId = gmId;
    }




}
