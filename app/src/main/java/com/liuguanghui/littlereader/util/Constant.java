package com.liuguanghui.littlereader.util;


import java.io.File;

/**
 * Created by Liang_Lu on 2017/11/22.
 */

public class Constant {
    public static String BASE_URL;

    static {
        BASE_URL = "http://139.199.36.170:9002";
    }

    public static final String ZHUISHU_IMAGE_URL = "http://statics.zhuishushenqi.com";
    //Book Date Convert Format
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";

    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)
    public static String BOOK_CACHE_PATH = FileUtils.getCachePath() + File.separator
            + "book_cache" + File.separator;

    /**
     * 保存本地token
     */
    public static String TOKEN = "token";
}
