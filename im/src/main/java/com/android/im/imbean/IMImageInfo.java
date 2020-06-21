package com.android.im.imbean;

public class IMImageInfo {
    /**
     * 缩略图
     */
    private String thumbnailUrl;
    /**
     * 原图
     */
    private String originUrl;

    private boolean isMine;

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public IMImageInfo(String thumbnailUrl, String originUrl, boolean isMine) {
        this.thumbnailUrl = thumbnailUrl;
        this.originUrl = originUrl;
        this.isMine = isMine;
    }
}
