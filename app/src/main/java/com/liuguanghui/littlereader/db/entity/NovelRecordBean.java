package com.liuguanghui.littlereader.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liuguanghui on 2018/6/3.
 */
@Entity
public class NovelRecordBean implements Serializable {

    private static final long serialVersionUID = -1341681074047596476L;
    //所属的书的id
    @Id
    private long novelId;

    private Long netId;

    //阅读到了第几章
    private long chapterId;
    //当前的页码
    private int pagePos;
    @Generated(hash = 137695649)
    public NovelRecordBean(long novelId, Long netId, long chapterId, int pagePos) {
        this.novelId = novelId;
        this.netId = netId;
        this.chapterId = chapterId;
        this.pagePos = pagePos;
    }
    @Generated(hash = 1912913007)
    public NovelRecordBean() {
    }
    public long getNovelId() {
        return this.novelId;
    }
    public void setNovelId(long novelId) {
        this.novelId = novelId;
    }
    public Long getNetId() {
        return this.netId;
    }
    public void setNetId(Long netId) {
        this.netId = netId;
    }
    public long getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }
    public int getPagePos() {
        return this.pagePos;
    }
    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }
}
