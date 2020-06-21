package com.android.im.imbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/12/23.
 * Describe:小程序
 */
@Entity
public class SmallProgramBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * aPPkey :
     * appId : 5d9013d9baa48711ec03276f
     * collectionId : 7
     * customerId : 20485de8ba1d7a85dd48404805d6
     * isOnline : Y
     * programId : 2
     * programName : shuimi
     * programUrl : 跳转链接
     * searchKeyword : 水果，桃子
     * shareImage : http://65.52.160.124:9050/group1/M00/00/2B/CgABFV3U2QGAfpBRAAVkGzrTEmY42.jpeg
     * threeImage : http://65.52.160.124:9050/group1/M00/00/2B/CgABFV3U2QGAfpBRAAVkGzrTEmY42.jpeg
     * twoImage : http://65.52.160.124:9050/group1/M00/00/2B/CgABFV3U2QGAfpBRAAVkGzrTEmY42.jpeg
     */
    @Id(autoincrement = true)//设置自增长
    private Long id;
    @Index(unique = true)//设置唯一性
    private String programId;

    private String aPPkey;
    private int collectionId;
    private String customerId;
    private String isOnline;
    private String programName;
    private String programUrl;
    private String searchKeyword;
    private String shareImage;
    private String threeImage;
    private String twoImage;
    private String shareTitle;
    private String letter;
    private String isConllection;
    private String useId;
    private boolean flag=false;
    private boolean isDrag;//是否拖动

    @Generated(hash = 1000954824)
    public SmallProgramBean(Long id, String programId, String aPPkey, int collectionId,
            String customerId, String isOnline, String programName, String programUrl,
            String searchKeyword, String shareImage, String threeImage, String twoImage,
            String shareTitle, String letter, String isConllection, String useId, boolean flag,
            boolean isDrag) {
        this.id = id;
        this.programId = programId;
        this.aPPkey = aPPkey;
        this.collectionId = collectionId;
        this.customerId = customerId;
        this.isOnline = isOnline;
        this.programName = programName;
        this.programUrl = programUrl;
        this.searchKeyword = searchKeyword;
        this.shareImage = shareImage;
        this.threeImage = threeImage;
        this.twoImage = twoImage;
        this.shareTitle = shareTitle;
        this.letter = letter;
        this.isConllection = isConllection;
        this.useId = useId;
        this.flag = flag;
        this.isDrag = isDrag;
    }

    @Generated(hash = 1053796136)
    public SmallProgramBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getaPPkey() {
        return aPPkey;
    }

    public void setaPPkey(String aPPkey) {
        this.aPPkey = aPPkey;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
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

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
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

    public String getTwoImage() {
        return twoImage;
    }

    public void setTwoImage(String twoImage) {
        this.twoImage = twoImage;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getIsConllection() {
        return isConllection;
    }

    public void setIsConllection(String isConllection) {
        this.isConllection = isConllection;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isDrag() {
        return isDrag;
    }

    public void setDrag(boolean drag) {
        isDrag = drag;
    }

    public String getAPPkey() {
        return this.aPPkey;
    }

    public void setAPPkey(String aPPkey) {
        this.aPPkey = aPPkey;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public boolean getIsDrag() {
        return this.isDrag;
    }

    public void setIsDrag(boolean isDrag) {
        this.isDrag = isDrag;
    }
}
