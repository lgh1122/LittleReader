package com.liuguanghui.littlereader.db.helper;


import com.liuguanghui.littlereader.db.entity.NovelBean;
import com.liuguanghui.littlereader.db.gen.DaoSession;
import com.liuguanghui.littlereader.db.gen.NovelBeanDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Liang_Lu on 2017/12/1.
 * 书架数据库操作工具类
 */

public class NovelHelper {
    private static volatile NovelHelper sInstance;
    private static DaoSession daoSession;
    private static NovelBeanDao novelBeanDao;

    public static NovelHelper getsInstance() {
        if (sInstance == null) {
            synchronized (NovelHelper.class) {
                if (sInstance == null) {
                    sInstance = new NovelHelper();
                    daoSession = DaoDbHelper.getInstance().getSession();
                    novelBeanDao = daoSession.getNovelBeanDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存一本书籍 同步
     *
     * @param novelBean
     */
    public void saveBook(NovelBean novelBean) {
        novelBeanDao.insertOrReplace(novelBean);
    }

    /**
     * 保存多本书籍 同步
     *
     * @param novelBeans
     */
    public void saveBooks(List<NovelBean> novelBeans) {
        novelBeanDao.insertOrReplaceInTx(novelBeans);
    }


    /**
     * 保存一本书籍 异步
     *
     * @param collBookBean
     */
    public void saveBookWithAsync(NovelBean collBookBean) {
        daoSession.startAsyncSession().runInTx(() -> {
           /* if (collBookBean.getBookChapters() != null) {
                //存储BookChapterBean(需要找个免更新的方式)
                daoSession.getBookChapterBeanDao()
                        .insertOrReplaceInTx(collBookBean.getBookChapters());
            }*/
            //存储CollBook (确保先后顺序，否则出错)
            novelBeanDao.insertOrReplace(collBookBean);
        });
    }

    /**
     * 保存多本书籍 异步
     *
     * @param novelBeans
     */
    public void saveBooksWithAsync(List<NovelBean> novelBeans) {
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
                            novelBeanDao.insertOrReplaceInTx(novelBeans);
                        }
                );

    }

    /**
     * 删除书籍
     *
     * @param novelBean
     */
    public Observable<String> removeBookInRx(NovelBean novelBean) {
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
                novelBeanDao.delete(novelBean);
                e.onNext("删除成功");
            }
        });
    }

    /**
     * 删除所有书籍
     */
    public void removeAllBook() {
        for (NovelBean collBookBean : findAllBooks()) {
            removeBookInRx(collBookBean);
        }
    }

    /**
     * 查询一本书籍
     */
    public NovelBean findBookById(Long netId,Long id  ) {
        NovelBean bookBean = novelBeanDao.queryBuilder().where(NovelBeanDao.Properties.Id.eq(id),NovelBeanDao.Properties.Netid.eq(netId))
                .unique();
        return bookBean;
    }

    /**
     * 查询所有书籍
     */
    public List<NovelBean> findAllBooks() {
        return novelBeanDao
                .queryBuilder()
                .orderDesc(NovelBeanDao.Properties.IsTop,NovelBeanDao.Properties.ReadDate)
                .list();
    }


}
