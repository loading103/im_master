package com.android.im.imbean;


import java.io.Serializable;
import java.util.List;

public class IMFindTotleData implements Serializable {
    private String  title;
    private List<String>datas;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public IMFindTotleData(String title, List<String> datas) {
        this.title = title;
        this.datas = datas;
    }
}
