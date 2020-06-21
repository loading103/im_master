package com.android.im.imbean;

import java.util.List;

/**
 * Created by Administrator on 2019/12/26.
 * Describe:
 */
public class RecentlyUseBean {
    private String current;
    private String pages;
    private List<SmallProgramBean> records;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public List<SmallProgramBean> getRecords() {
        return records;
    }

    public void setRecords(List<SmallProgramBean> records) {
        this.records = records;
    }
}
