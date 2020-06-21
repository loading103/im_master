package com.android.nettylibrary.http;

import java.io.Serializable;
import java.util.List;

public class IMMessageDataJson<T> implements Serializable{
    private String   nickName;
    private String   avaterUrl;
    //文本消息,表情消息1000
    private List<String>notices;             //群@id
    private String   text;                   //文字
    private String   type;                   //类型
    //图片2000
    private  List<String>   images;           //图片
    private  List<String>   thumbImages;      //省略图
    //视频 3000
    private String    videoUrl;             //视频源文件地址
    private String    firstFrame;             //视频文件的第一帧截图
    private String fileType;

    //音频 3500
    private String    audioUrl;             //文件地址
    private String    duration;             //时间

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    //红包消息4000
    private String    title;             //标题
    private String    redPacketId;             //红包id
    private String    desc;                  //红包描述信息    //推送消息简介
    private String  style   ;               //使用的风格，暂时默认为STYLE_DEFAULT

    //推送消息5000
    private String    image;             //封面图
    private String  link   ;               //连接地址
    private String  ownerurl   ;               //保存自己的资源url地址
    private String  ownerfirstFrame   ;               //保存自己的视频url地址

    //活动消息6000
    private String    state;             //1=未开始，2=正在进行中，3=已结束
    //跟投消息7000
    private String    icon;             //游戏ICON
    private String  gameName   ;               //跟投游戏名称
    private String    gameId;             //游戏id
    private String  betOrderId   ;               //投注订单ID
    private String    amount;             //投注金额
    private String  content   ;               //内容
    private String  num   ;               //期号
    private String  playMethod   ;               //玩法
    private String  userId   ;               //用户ID
    private String  sealingTime   ;               //用户ID
    private String  bettingMultiples   ;
    private String  bettingTotalAmount   ;
    //红单消息8000
    private String    betAmount;             //投注金额
    private String  winAmount   ;               //中奖金额
    private String    gainAmount;             //盈利金额
    //计划消息9000
    private String    planName;             //计划名称
    private String  planId   ;               //计划ID
    private String    planText;             //计划内容，数组
    private String    width;             //宽
    private String    height;             //高
    //系统消息
    private     String    sysCode;             //高
    private     T meta;
    //分享小程序
    private     String    twoImage;             //高
    private     String    shareDescribe;
    private     String    shareTitle;
    private     String    appId;
    private     String    shareImage;
    private     String    threeImage;
    private     String    programId;
    private     String  programName;
    private     String  programUrl;

    public String getOwnerfirstFrame() {
        return ownerfirstFrame;
    }

    public void setOwnerfirstFrame(String ownerfirstFrame) {
        this.ownerfirstFrame = ownerfirstFrame;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvaterUrl() {
        return avaterUrl;
    }

    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramUrl() {
        return programUrl;
    }

    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }

    public String getTwoImage() {
        return twoImage;
    }

    public void setTwoImage(String twoImage) {
        this.twoImage = twoImage;
    }

    public String getShareDescribe() {
        return shareDescribe;
    }

    public void setShareDescribe(String shareDescribe) {
        this.shareDescribe = shareDescribe;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getThreeImage() {
        return threeImage;
    }

    public void setThreeImage(String threeImage) {
        this.threeImage = threeImage;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getOwnerurl() {
        return ownerurl;
    }

    public void setOwnerurl(String ownerurl) {
        this.ownerurl = ownerurl;
    }

    public T getMeta() {
        return meta;
    }

    public void setMeta(T meta) {
        this.meta = meta;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }


    public String getSealingTime() {
        return sealingTime;
    }

    public void setSealingTime(String sealingTime) {
        this.sealingTime = sealingTime;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHight() {
        return height;
    }

    public void setHight(String hight) {
        this.height = hight;
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getThumbImages() {
        return thumbImages;
    }

    public void setThumbImages(List<String> thumbImages) {
        this.thumbImages = thumbImages;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFirstFrame() {
        return firstFrame;
    }

    public void setFirstFrame(String firstFrame) {
        this.firstFrame = firstFrame;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBetOrderId() {
        return betOrderId;
    }

    public void setBetOrderId(String betOrderId) {
        this.betOrderId = betOrderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPlayMethod() {
        return playMethod;
    }

    public void setPlayMethod(String playMethod) {
        this.playMethod = playMethod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(String betAmount) {
        this.betAmount = betAmount;
    }

    public String getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(String winAmount) {
        this.winAmount = winAmount;
    }

    public String getGainAmount() {
        return gainAmount;
    }

    public void setGainAmount(String gainAmount) {
        this.gainAmount = gainAmount;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanText() {
        return planText;
    }

    public void setPlanText(String planText) {
        this.planText = planText;
    }

    public IMMessageDataJson() {
    }

    public String getBettingMultiples() {
        return bettingMultiples;
    }

    public void setBettingMultiples(String bettingMultiples) {
        this.bettingMultiples = bettingMultiples;
    }

    public String getBettingTotalAmount() {
        return bettingTotalAmount;
    }

    public void setBettingTotalAmount(String bettingTotalAmount) {
        this.bettingTotalAmount = bettingTotalAmount;
    }
}
