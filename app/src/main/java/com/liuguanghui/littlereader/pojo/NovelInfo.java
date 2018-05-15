package com.liuguanghui.littlereader.pojo;

/**
 * Created by liuguanghui on 2018/5/10.
 */

public class NovelInfo {

    private int id;
    //图标
    private int image;
    //书籍名称
    private String novelName;
    //章节
    private String chapter;
    //    是否置顶
    private int isTop ;

    //    阅读时间
    private long readDate;

    private int addBook;

    public NovelInfo() {
    }

    public NovelInfo(int image, String novelName, String chapter, int isTop) {
        this.image = image;
        this.novelName = novelName;
        this.chapter = chapter;
        this.isTop = isTop;
    }

    public int getAddBook() {

        return addBook;
    }

    public void setAddBook(int addBook) {
        this.addBook = addBook;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

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
}
