package com.liuguanghui.littlereader.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

@Entity(
		// Define indexes spanning multiple columns here.
		indexes = {
				@Index(value = "id DESC, netid DESC", unique = true)
		}
)
public class NovelBean implements Serializable {


	private static final long serialVersionUID = -5901231322962950903L;
	@Id(autoincrement = true)
	private Long autoId;

	private Long id;  // 书籍编号
	private Long netid;

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
    private Long addtime; //添加时间
    private Long updatetime; //更新时间
    
	//    是否置顶
	private int isTop ;
	//阅读时间
	private long readDate;

	//是否更新后还未阅读
	private boolean isNoReadUpdate = true;

	//本地章节表中已缓存的最新章节，下次获取新章节从此起始获取
	private Long cacheChapter;

	// 不存在数据库中
	@Transient
	private Long lastUpdateTime;
	@Transient
	private List<ChapterBean> chapterBeans;
	// 不存在数据库中
	@Transient
	private String firstLetter;
	@Generated(hash = 431969143)
	public NovelBean(Long autoId, Long id, Long netid, String title, String author,
					String tname, Long tid, String netUrl, Long latestchapterid,
					String latestchaptername, Byte status, String introduction,
					String imgpath, Long addtime, Long updatetime, int isTop, long readDate,
					boolean isNoReadUpdate, Long cacheChapter) {
			this.autoId = autoId;
			this.id = id;
			this.netid = netid;
			this.title = title;
			this.author = author;
			this.tname = tname;
			this.tid = tid;
			this.netUrl = netUrl;
			this.latestchapterid = latestchapterid;
			this.latestchaptername = latestchaptername;
			this.status = status;
			this.introduction = introduction;
			this.imgpath = imgpath;
			this.addtime = addtime;
			this.updatetime = updatetime;
			this.isTop = isTop;
			this.readDate = readDate;
			this.isNoReadUpdate = isNoReadUpdate;
			this.cacheChapter = cacheChapter;
	}

	@Generated(hash = 1624046591)
	public NovelBean() {
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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

	public Long getNetid() {
			return this.netid;
	}

	public void setNetid(Long netid) {
			this.netid = netid;
	}

	public String getTitle() {
			return this.title;
	}

	public void setTitle(String title) {
			this.title = title;
	}

	public String getAuthor() {
			return this.author;
	}

	public void setAuthor(String author) {
			this.author = author;
	}

	public String getTname() {
			return this.tname;
	}

	public void setTname(String tname) {
			this.tname = tname;
	}

	public Long getTid() {
			return this.tid;
	}

	public void setTid(Long tid) {
			this.tid = tid;
	}

	public String getNetUrl() {
			return this.netUrl;
	}

	public void setNetUrl(String netUrl) {
			this.netUrl = netUrl;
	}

	public Long getLatestchapterid() {
			return this.latestchapterid;
	}

	public void setLatestchapterid(Long latestchapterid) {
			this.latestchapterid = latestchapterid;
	}

	public String getLatestchaptername() {
			return this.latestchaptername;
	}

	public void setLatestchaptername(String latestchaptername) {
			this.latestchaptername = latestchaptername;
	}

	public Byte getStatus() {
			return this.status;
	}

	public void setStatus(Byte status) {
			this.status = status;
	}

	public String getIntroduction() {
			return this.introduction;
	}

	public void setIntroduction(String introduction) {
			this.introduction = introduction;
	}

	public String getImgpath() {
			return this.imgpath;
	}

	public void setImgpath(String imgpath) {
			this.imgpath = imgpath;
	}

	public Long getAddtime() {
			return this.addtime;
	}

	public void setAddtime(Long addtime) {
			this.addtime = addtime;
	}

	public Long getUpdatetime() {
			return this.updatetime;
	}

	public void setUpdatetime(Long updatetime) {
			this.updatetime = updatetime;
	}

	public int getIsTop() {
			return this.isTop;
	}

	public void setIsTop(int isTop) {
			this.isTop = isTop;
	}

	public long getReadDate() {
			return this.readDate;
	}

	public void setReadDate(long readDate) {
			this.readDate = readDate;
	}

	public boolean getIsNoReadUpdate() {
			return this.isNoReadUpdate;
	}

	public void setIsNoReadUpdate(boolean isNoReadUpdate) {
			this.isNoReadUpdate = isNoReadUpdate;
	}

	public Long getCacheChapter() {
			return this.cacheChapter;
	}

	public void setCacheChapter(Long cacheChapter) {
			this.cacheChapter = cacheChapter;
	}


	public List<ChapterBean> getChapterBeans() {
		return chapterBeans;
	}

	public void setChapterBeans(List<ChapterBean> chapterBeans) {
		this.chapterBeans = chapterBeans;
	}
}