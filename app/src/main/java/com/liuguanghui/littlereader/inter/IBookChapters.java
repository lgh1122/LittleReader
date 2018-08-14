package com.liuguanghui.littlereader.inter;

import com.liuguanghui.littlereader.db.entity.ChapterBean;

import java.util.List;


public interface IBookChapters {
    void bookChapters(List<ChapterBean> chapterBeans);

    void finishChapters();

    void errorChapters();
    void showLoading();
    void stopLoading();

}
