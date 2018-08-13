package com.liuguanghui.littlereader.db.helper;


import com.liuguanghui.littlereader.db.entity.ChapterBean;
import com.liuguanghui.littlereader.db.entity.NovelBean;
import com.liuguanghui.littlereader.db.gen.ChapterBeanDao;
import com.liuguanghui.littlereader.db.gen.DaoSession;
import com.liuguanghui.littlereader.db.gen.NovelBeanDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Liang_Lu on 2017/12/1.
 * 章节数据库操作工具类
 */

public class ChapterHelper {
    private static volatile ChapterHelper sInstance;
    private static DaoSession daoSession;
    private static ChapterBeanDao chapterBeanDao;

    public static ChapterHelper getsInstance() {
        if (sInstance == null) {
            synchronized (ChapterHelper.class) {
                if (sInstance == null) {
                    sInstance = new ChapterHelper();
                    daoSession = DaoDbHelper.getInstance().getSession();
                    chapterBeanDao = daoSession.getChapterBeanDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存一本书籍 同步
     *
     * @param chapterBean
     */
    public void saveBook(ChapterBean chapterBean) {
        chapterBeanDao.insertOrReplace(chapterBean);
    }

    /**
     * 保存多本书籍 同步
     *
     * @param chapterBeans
     */
    public void saveBooks(List<ChapterBean> chapterBeans) {
        chapterBeanDao.insertOrReplaceInTx(chapterBeans);
    }


    /**
     * 保存一本书籍 异步
     *
     * @param collBookBean
     */
    public void saveBookWithAsync(ChapterBean collBookBean) {
        daoSession.startAsyncSession().runInTx(() -> {
           /* if (collBookBean.getBookChapters() != null) {
                //存储BookChapterBean(需要找个免更新的方式)
                daoSession.getBookChapterBeanDao()
                        .insertOrReplaceInTx(collBookBean.getBookChapters());
            }*/
            //存储CollBook (确保先后顺序，否则出错)
            chapterBeanDao.insertOrReplace(collBookBean);
        });
    }

    /**
     * 保存多本书籍 异步
     *
     * @param chapterBeans
     */
    public void saveBooksWithAsync(List<ChapterBean> chapterBeans) {
        daoSession.startAsyncSession()
                .runInTx(
                        () -> {
                           /* for (NovelBean bean : novelBeans) {
                                if (bean.getBookChapters() != null) {
                                    //存储BookChapterBean(需要修改，如果存在id相同的则无视)
                                    daoSession.getBookChapterBeanDao()
                                            .insertOrReplaceInTx(bean.getBookChapters());
                                }
                            }*/
                            //存储CollBook (确保先后顺序，否则出错)
                            chapterBeanDao.insertOrReplaceInTx(chapterBeans);
                        }
                );

    }

    /**
     * 删除书籍
     *
     * @param chapterBean
     */
    public Observable<String> removeBookInRx(ChapterBean chapterBean) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                /*//查看文本中是否存在删除的数据
                FileUtils.deleteFile(Constant.BOOK_CACHE_PATH + collBookBean.get_id());
                //删除任务
                BookDownloadHelper.getsInstance().removeDownloadTask(collBookBean.get_id());
                //删除目录
                BookChapterHelper.getsInstance().removeBookChapters(collBookBean.get_id());*/
                //删除CollBook
                chapterBeanDao.delete(chapterBean);
                e.onNext("删除成功");
            }
        });
    }

    /**
     * 删除所有书籍
     */
    public void removeBookAllChapter(Long netId,Long novelId) {
        for (ChapterBean chapterBean : findNovelChapters(netId,novelId)) {
            removeBookInRx(chapterBean);
        }
    }

    /**
     * 查询书籍章节
     */
    public ChapterBean findBookById(Long netId,Long novelId , Long id  ) {
        ChapterBean chapterBean = chapterBeanDao.queryBuilder().where(ChapterBeanDao.Properties.NovelId.eq(novelId),ChapterBeanDao.Properties.Id.eq(id)
                ,ChapterBeanDao.Properties.Netid.eq(netId))
                .unique();
        return chapterBean;
    }

    /**
     * 查询书籍的所有章节
     */
    public List<ChapterBean> findNovelChapters(Long netId,Long novelId) {
        return chapterBeanDao
                .queryBuilder()
                .where(ChapterBeanDao.Properties.NovelId.eq(novelId),ChapterBeanDao.Properties.Netid.eq(netId))
                .orderAsc(ChapterBeanDao.Properties.Id)
                .list();
    }


}
