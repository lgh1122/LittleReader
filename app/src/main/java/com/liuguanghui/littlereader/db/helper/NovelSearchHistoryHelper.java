package com.liuguanghui.littlereader.db.helper;


import com.liuguanghui.littlereader.db.entity.NovelSearchHistory;
import com.liuguanghui.littlereader.db.gen.DaoSession;
import com.liuguanghui.littlereader.db.gen.NovelSearchHistoryDao;

import java.util.List;

/**
 * Created by Liang_Lu on 2017/12/1.
 * 书架数据库操作工具类
 */

public class NovelSearchHistoryHelper {
    private static volatile NovelSearchHistoryHelper sInstance;
    private static DaoSession daoSession;
    private static NovelSearchHistoryDao novelSearchHistoryDao;

    public static NovelSearchHistoryHelper getsInstance() {
        if (sInstance == null) {
            synchronized (NovelSearchHistoryHelper.class) {
                if (sInstance == null) {
                    sInstance = new NovelSearchHistoryHelper();
                    daoSession = DaoDbHelper.getInstance().getSession();
                    novelSearchHistoryDao = daoSession.getNovelSearchHistoryDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存一本书籍 同步
     *
     * @param novelSearchHistory
     */
    public void saveBook(NovelSearchHistory novelSearchHistory) {
        novelSearchHistoryDao.insertOrReplace(novelSearchHistory);
    }

    /**
     * 保存多本书籍 同步
     *
     * @param novelSearchHistories
     */
    public void saveBooks(List<NovelSearchHistory> novelSearchHistories) {
        novelSearchHistoryDao.insertOrReplaceInTx(novelSearchHistories);
    }





    /**
     * 删除书籍
     *
     * @param novelSearchHistory
     */
    public void removeBookHistoryRx(NovelSearchHistory novelSearchHistory) {
        novelSearchHistoryDao.delete(novelSearchHistory);
    }

    /**
     * 删除所有书籍
     */
    public void removeAllHistory() {
        for (NovelSearchHistory collBookBean : findAllBookHistorys()) {
            removeBookHistoryRx(collBookBean);
        }
    }

    /**
     * 查询一本书籍
     */
    public NovelSearchHistory findBookHistoryByKeyword(String keyword) {
        NovelSearchHistory novelSearchHistory = novelSearchHistoryDao.queryBuilder().where(NovelSearchHistoryDao.Properties.Keyword.eq(keyword))
                .unique();
        return novelSearchHistory;
    }

    /**
     * 查询所有搜索历史
     */
    public List<NovelSearchHistory> findAllBookHistorys() {
        return novelSearchHistoryDao
                .queryBuilder()
                .list();
    }

    /**
     * 查询前六搜索历史
     */
    public List<NovelSearchHistory> findLimitBookHistorys() {
        return novelSearchHistoryDao
                .queryBuilder()
                .limit(6)
                .orderDesc(NovelSearchHistoryDao.Properties.Id)
                .list();
    }




}
