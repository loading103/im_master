package com.android.im.imbean;

public class UpdataPickureBean {
   private  String url;
    private  long time;
    private  int durtime;
    private  String thumbImagePath;

    public int getDurtime() {
        return durtime;
    }

    public void setDurtime(int durtime) {
        this.durtime = durtime;
    }

    public String getThumbImagePath() {
        return thumbImagePath;
    }

    public void setThumbImagePath(String thumbImagePath) {
        this.thumbImagePath = thumbImagePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
