package com.android.im.imeventbus;

import android.graphics.Bitmap;

public class VideoBeanEvent {
    private String url;
    private Bitmap bitmap;
    private boolean isVideo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public VideoBeanEvent(String url, Bitmap bitmap, boolean isVideo) {
        this.url = url;
        this.bitmap = bitmap;
        this.isVideo=isVideo;
    }

    public VideoBeanEvent() {
    }
}
