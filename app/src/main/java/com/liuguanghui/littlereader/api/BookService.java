package com.liuguanghui.littlereader.api;

import com.allen.library.bean.BaseData;
import com.liuguanghui.littlereader.viewmodel.BookChaptersBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Liang_Lu on 2017/12/3.
 * 书籍模块接口
 */

public interface BookService {

    /**
     * 获取所有分类
     *
     * @return
     */
   /* @GET(ModelPath.API + "/classify")
    Observable<BaseData<String>> bookClassify();
*/
    /**
     * 获取分类下的书籍
     *
     * @param type
     * @param major
     * @param page
     * @return
     */
    @GET(ModelPath.BOOK)
    Observable<BaseData<String>> books(@Query("type") String type,
                                               @Query("major") String major, @Query("page") int page);

    /**
     * 获取书籍信息
     *
     * @param bookId
     * @return
     */
    @GET(ModelPath.BOOK + "/{bookId}")
    Observable<BaseData<String>> bookInfo(@Path("bookId") String bookId);
    /**
     * 获取书籍目录
     *
     * @return
     */
    @GET(ModelPath.CHAPTERLIST + "/{netId}/{novelId}")
    Observable<BookChaptersBean> bookChapters(@Path("netId") Long netId, @Path("novelId") Long novelId);

    @GET(ModelPath.CHAPTERLIST + "/{netId}/{novelId}")
    Observable<String> bookChapterStrs(@Path("netId") Long netId, @Path("novelId") Long novelId);

    /**
     * 根据link获取正文

     * @return
     */
    @GET(ModelPath.CHAPTERCONTENT + "/{netId}/{novelId}/{chapterId}")
    Observable<String> bookContent(@Path("netId") Long netId,@Path("novelId") Long novelId,@Path("chapterId") Long chapterId);

    /**
     * 根据tag获取书籍
     *
     * @param bookTag
     * @param page
     * @return
     */
   /* @GET(ModelPath.BOOKS + "/tag")
    Observable<BaseData<List<BookBean>>> booksByTag(@Query("bookTag") String bookTag, @Query("page") int page);
*/
   /* *//**
     * 搜索书籍
     *
     * @param keyword
     * @return
     *//*
    @GET(ModelPath.API + "/search")
    Observable<BaseData<List<BookBean>>> booksSearch(@Query("keyword") String keyword);*/

}
