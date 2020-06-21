package com.android.im.imeventbus;


public class IMAppUpdataEvent {
    private String dataJson;

    public String getDataJson() {
        return dataJson;
    }

    public IMAppUpdataEvent(String dataJson) {
        this.dataJson = dataJson;
    }
}
