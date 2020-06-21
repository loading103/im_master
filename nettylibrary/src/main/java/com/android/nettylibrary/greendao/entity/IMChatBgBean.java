package com.android.nettylibrary.greendao.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 */
@Entity
public class IMChatBgBean implements Serializable {
    private static final long serialVersionUID = 2030555082670563109L;
    @Id(autoincrement = true)
    private Long _id;   //id,表的主键并自增长

    private String url;

    private boolean ischoose;   //是不是选中

    private String ismine;

    @Generated(hash = 526445708)
    public IMChatBgBean(Long _id, String url, boolean ischoose, String ismine) {
        this._id = _id;
        this.url = url;
        this.ischoose = ischoose;
        this.ismine = ismine;
    }

    @Generated(hash = 1059383787)
    public IMChatBgBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getIschoose() {
        return this.ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    public String getIsmine() {
        return this.ismine;
    }

    public void setIsmine(String ismine) {
        this.ismine = ismine;
    }


}
