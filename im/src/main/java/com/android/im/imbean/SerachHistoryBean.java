package com.android.im.imbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2019/12/30.
 * Describe:搜索历史
 */
@Entity
public class SerachHistoryBean {
    @Id(autoincrement = true)//设置自增长
    private Long id;
    private String name;

    @Generated(hash = 407081998)
    public SerachHistoryBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1018408177)
    public SerachHistoryBean() {
    }

    public SerachHistoryBean(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
