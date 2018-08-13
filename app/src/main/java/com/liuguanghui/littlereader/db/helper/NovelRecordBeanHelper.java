package com.liuguanghui.littlereader.db.helper;


import com.liuguanghui.littlereader.db.entity.NovelBean;
import com.liuguanghui.littlereader.db.entity.NovelRecordBean;
import com.liuguanghui.littlereader.db.entity.NovelRecordBean;
import com.liuguanghui.littlereader.db.gen.DaoSession;
import com.liuguanghui.littlereader.db.gen.NovelBeanDao;
import com.liuguanghui.littlereader.db.gen.NovelRecordBeanDao;
import com.liuguanghui.littlereader.db.gen.NovelRecordBeanDao;

import java.util.List;

/**
  * 书籍阅读记录数据库操作工具类
 */

public class NovelRecordBeanHelper {
    private static volatile NovelRecordBeanHelper sInstance;
    private static DaoSession daoSession;
    private static NovelRecordBeanDao novelRecordBeanDao;

    public static NovelRecordBeanHelper getsInstance() {
        if (sInstance == null) {
            synchronized (NovelRecordBeanHelper.class) {
                if (sInstance == null) {
                    sInstance = new NovelRecordBeanHelper();
                    daoSession = DaoDbHelper.getInstance().getSession();
                    novelRecordBeanDao = daoSession.getNovelRecordBeanDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存一本书籍阅读记录 同步
     *
     * @param novelRecordBean
     */
    public void saveNovelRecordBean(NovelRecordBean novelRecordBean) {
        novelRecordBeanDao.insertOrReplace(novelRecordBean);
    }

    /**
     * 保存多条阅读记录
     *
     * @param novelRecordBeans
     */
    public void saveNovelRecordBeans(List<NovelRecordBean> novelRecordBeans) {
        novelRecordBeanDao.insertOrReplaceInTx(novelRecordBeans);
    }





    /**
     * 删除书籍
     *
     * @param novelRecordBean
     */
    public void removeNovelRecordBeanRx(NovelRecordBean novelRecordBean) {
        novelRecordBeanDao.delete(novelRecordBean);
    }

    /**
     * 删除所有书籍
     */
    public void removeAllNovelRecordBeans() {
        for (NovelRecordBean collBookBean : findAllNovelRecordBeans()) {
            removeNovelRecordBeanRx(collBookBean);
        }
    }


    /**
     * 查询一本书籍的阅读记录
     */
    public NovelRecordBean findNovelRecordBeanById(Long netId, Long novelId  ) {
        NovelRecordBean novelRecordBean = novelRecordBeanDao.queryBuilder().where(NovelRecordBeanDao.Properties.NovelId.eq(novelId),NovelRecordBeanDao.Properties.NetId.eq(netId))
                .unique();
        return novelRecordBean;
    }
    /**
     * 查询所有阅读记录
     */
    public List<NovelRecordBean> findAllNovelRecordBeans() {
        return novelRecordBeanDao
                .queryBuilder()
                .list();
    }






}
