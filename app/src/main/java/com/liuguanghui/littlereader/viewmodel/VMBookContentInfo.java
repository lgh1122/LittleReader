package com.liuguanghui.littlereader.viewmodel;

import android.content.Context;
import android.util.Log;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.liuguanghui.littlereader.api.BookService;
import com.liuguanghui.littlereader.db.entity.ChapterBean;
import com.liuguanghui.littlereader.inter.IBookChapters;
import com.liuguanghui.littlereader.widget.page.TxtChapter;

import java.util.List;

import io.reactivex.disposables.Disposable;


/**
 * Created by Liang_Lu on 2017/12/11.
 */

public class VMBookContentInfo extends BaseViewModel {
    IBookChapters iBookChapters;

    Disposable mDisposable;
    String title;

    public VMBookContentInfo(Context mContext, IBookChapters iBookChapters) {
        super(mContext);
        this.iBookChapters = iBookChapters;
    }

    public void loadChapters(Long netId , Long novelId) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(BookService.class)
                .bookChapters(netId,novelId)
                .compose(Transformer.<BookChaptersBean>switchSchedulers())
                .subscribe(new CommonObserver<BookChaptersBean>() {
                    @Override
                    protected void onError(String s) {
                        Log.e("Error",s);
                    }
                    @Override
                    protected void onSuccess(BookChaptersBean bookChaptersBean) {
                        if(bookChaptersBean != null){
                        }
                        //JsonResult jsonResult = JsonResult.formatToList(data,ChapterBean.class);
                        if (bookChaptersBean.getStatus() == 200) {
                            List<ChapterBean> chapterBeans = bookChaptersBean.getData();//(List<ChapterBean>) jsonResult.getData();

                            if (iBookChapters != null) {
                                iBookChapters.bookChapters(chapterBeans);
                            }
                        }
                    }
                });

        /*RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(BookService.class)
                .bookChapters(netId,novelId)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookChaptersBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        Log.e("Error",errorMsg);
                    }
                    @Override
                    protected void onSuccess(BookChaptersBean data) {
                        if(data != null){
                        }
                        //JsonResult jsonResult = JsonResult.formatToList(data,ChapterBean.class);
                        if (data.getStatus() == 200) {
                            List<ChapterBean> chapterBeans = data.getData();//(List<ChapterBean>) jsonResult.getData();

                            if (iBookChapters != null) {
                                iBookChapters.bookChapters(chapterBeans);
                            }
                        }
                    }
                });*/

    }

    /**
     * 加载正文
     *
     * @param bookId
     * @param bookChapterList
     */
    public void loadContent( Long netId,Long bookId, List<TxtChapter> bookChapterList){
        int size = bookChapterList.size();
        //取消上次的任务，防止多次加载
        if (mDisposable != null) {
            mDisposable.dispose();
        }

       /* List<Observable<ChapterContentBean>> chapterContentBeans = new ArrayList<>(bookChapterList.size());
        ArrayDeque<String> titles = new ArrayDeque<>(bookChapterList.size());
        //首先判断是否Chapter已经存在
        for (int i = 0; i < size; i++) {
            TxtChapter bookChapter = bookChapterList.get(i);
            if (!(BookManager.isChapterCached(bookId, bookChapter.getTitle()))) {
                Observable<ChapterContentBean> contentBeanObservable = RxHttpUtils
                        .createApi(BookService.class).bookContent(bookChapter.getLink());
                chapterContentBeans.add(contentBeanObservable);
                titles.add(bookChapter.getTitle());
            }
            //如果已经存在，再判断是不是我们需要的下一个章节，如果是才返回加载成功
            else if (i == 0) {
                if (iBookChapters != null) {
                    iBookChapters.finishChapters();
                }
            }
        }
        title = titles.poll();
        Observable.concat(chapterContentBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ChapterContentBean>() {
                            @Override
                            public void accept(ChapterContentBean bean) throws Exception {
                                BookSaveUtils.getInstance().saveChapterInfo(bookId, title, bean.getChapter().getCpContent());
                                iBookChapters.finishChapters();
                                title = titles.poll();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (bookChapterList.get(0).getTitle().equals(title)) {
                                    iBookChapters.errorChapters();
                                }
                                LogUtils.e(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                mDisposable = disposable;
                            }
                        });*/

    }
}