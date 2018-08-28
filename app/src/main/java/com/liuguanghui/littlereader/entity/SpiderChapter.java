package com.liuguanghui.littlereader.entity;

public class SpiderChapter   {
    private Long id;
    private Long novelId;
    private Long netid;

    private String title;

    private String chapterPath;

    private String cPath;

    private Long prevId;

    private Long nextId;


    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getNovelId() {
        return novelId;
    }

    public void setNovelId(Long novelId) {
        this.novelId = novelId;
    }

    public Long getNetid() {
        return netid;
    }

    public void setNetid(Long netid) {
        this.netid = netid;
    }

    public String getcPath() {
        return cPath;
    }

    public void setcPath(String cPath) {
        this.cPath = cPath;
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





	@Override
	public String toString() {
		return "SpiderChapter [title=" + title + ", chapterPath=" + chapterPath + ", cPath=" + cPath + ", prevId="
				+ prevId + ", nextId=" + nextId + "]";
	}

	 
    
    
}