package com.liuguanghui.littlereader.pojo;

import java.util.Date;

public class NovelVO {
    private Long id;  // 书籍编号

    private String title;  // 书籍名称

    private String author;  // 作者

    private String tname;  //类型

    private Long tid; //类型ID

    private String netUrl;  // 源地址

    private Long latestchapterid;  // 最新章节地址
    private String latestchaptername;  // 最新章节名称

    private Byte status;   // 连载 or 完本
    private String introduction;  // 简介
    private String imgpath; // 图片
    private Date addtime; //添加时间
    private Date updatetime; //更新时间

	private Long netid;
    @Deprecated
    private Date lastUpdateTime;  // 最新更新时间 根据网站章节更新获取
    private char firstLetter;
    //阅读时间
    private long readDate;
	//    是否置顶
	private int isTop ;

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public long getReadDate() {
		return readDate;
	}

	public void setReadDate(long readDate) {
		this.readDate = readDate;
	}

	public Long getNetid() {
		return netid;
	}

	public void setNetid(Long netid) {
		this.netid = netid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public String getNetUrl() {
		return netUrl;
	}

	public void setNetUrl(String netUrl) {
		this.netUrl = netUrl;
	}

	public Long getLatestchapterid() {
		return latestchapterid;
	}

	public void setLatestchapterid(Long latestchapterid) {
		this.latestchapterid = latestchapterid;
	}

	public String getLatestchaptername() {
		return latestchaptername;
	}

	public void setLatestchaptername(String latestchaptername) {
		this.latestchaptername = latestchaptername;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	 

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

 

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public char getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(char firstLetter) {
		this.firstLetter = firstLetter;
	}

	@Override
	public String toString() {
		return "NovelVO [id=" + id + ", title=" + title + ", author=" + author + ", tname=" + tname + ", tid="
				+ tid + ", netUrl=" + netUrl + ", latestchapterid=" + latestchapterid + ", latestchaptername="
				+ latestchaptername + ", status=" + status +   ", introduction=" + introduction
				+ ", imgpath=" + imgpath + ", addtime=" + addtime + ", updatetime=" + updatetime  
				+ ", lastUpdateTime=" + lastUpdateTime + ", firstLetter="
				+ firstLetter + "]";
	}  

    
    
   
    
    
}