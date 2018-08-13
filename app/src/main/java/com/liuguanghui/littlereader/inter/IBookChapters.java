package com.liuguanghui.littlereader.inter;

import com.liuguanghui.littlereader.db.entity.ChapterBean;



public interface IBookChapters {
    void bookChapters(ChapterBean bookChaptersBean);

    void finishChapters();

    void errorChapters();
    void showLoading();
    void stopLoading();

}
