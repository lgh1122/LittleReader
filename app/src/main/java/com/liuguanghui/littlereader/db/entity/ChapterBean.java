package com.liuguanghui.littlereader.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;


@Entity(
        // Define indexes spanning multiple columns here.
        indexes = {
                @Index(value = "id DESC, novelId DESC ,netid DESC", unique = true)
        }
)
public class ChapterBean  implements Serializable {


    private static final long serialVersionUID = -1936946875330080150L;
    @Id(autoincrement = true)
    private Long autoId;

    private Long id;
    private Long novelId;

    private Long netid;
    private String title;

    private String chapterPath;

    private Long prevId;

    private Long nextId;

    @Generated(hash = 1169085799)
    public ChapterBean(Long autoId, Long id, Long novelId, Long netid, String title,
            String chapterPath, Long prevId, Long nextId) {
        this.autoId = autoId;
        this.id = id;
        this.novelId = novelId;
        this.netid = netid;
        this.title = title;
        this.chapterPath = chapterPath;
        this.prevId = prevId;
        this.nextId = nextId;
    }

    @Generated(hash = 1028095945)
    public ChapterBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getChapterPath() {
        return chapterPath;
    }

    public void setChapterPath(String chapterPath) {
        this.chapterPath = chapterPath == null ? null : chapterPath.trim();
    }

    public Long getPrevId() {
        return prevId;
    }

    public void setPrevId(Long prevId) {
        this.prevId = prevId;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    public Long getAutoId() {
        return this.autoId;
    }

    public void setAutoId(Long autoId) {
        this.autoId = autoId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNovelId() {
        return this.novelId;
    }

    public void setNovelId(Long novelId) {
        this.novelId = novelId;
    }

    public Long getNetid() {
        return this.netid;
    }

    public void setNetid(Long netid) {
        this.netid = netid;
    }
}