package com.liuguanghui.littlereader.widget.page;

/**
 * Created by newbiechen on 17-7-1.
 */

public class TxtChapter {

    //章节所属的小说(网络)
    Long novelId;
    // 章节所属源网站id
    Long netId;
    //章节对应id
    Long Id;
    //章节的链接(网络)
    String link;

    //章节名(共用)
    String title;

    //章节内容在文章中的起始位置(本地)
    Long start;
    //章节内容在文章中的终止位置(本地)
    long end;

    //选中目录
    boolean isSelect;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Long getNovelId() {
        return novelId;
    }

    public void setNovelId(Long novelId) {
        this.novelId = novelId;
    }

    public Long getNetId() {
        return netId;
    }

    public void setNetId(Long netId) {
        this.netId = netId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "TxtChapter{" +
                "title='" + title + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
