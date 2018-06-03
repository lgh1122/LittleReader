package com.liuguanghui.littlereader.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liuguanghui on 2018/6/3.
 */

@Entity
public class NovelSearchHistory {
    /**
     * 搜索时间
     */
    private Long id;
    /**
     * 搜索关键字 唯一主键
     */
    @Id
    private String keyword;
    @Generated(hash = 2091852051)
    public NovelSearchHistory(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }
    @Generated(hash = 133824910)
    public NovelSearchHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKeyword() {
        return this.keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
