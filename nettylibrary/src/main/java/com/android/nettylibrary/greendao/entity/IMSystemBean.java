package com.android.nettylibrary.greendao.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 */
@Entity
public class IMSystemBean implements Serializable {
    private static final long serialVersionUID = -625351860612480701L;
    @Id(autoincrement = true)
    private Long _id;   //id,表的主键并自增长

    private String fingerprint;

    @Generated(hash = 945486634)
    public IMSystemBean(Long _id, String fingerprint) {
        this._id = _id;
        this.fingerprint = fingerprint;
    }

    @Generated(hash = 745312200)
    public IMSystemBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

}
