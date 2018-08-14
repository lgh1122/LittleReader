
package com.liuguanghui.littlereader.util.rxhelper;




import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Liang_Lu on 2017/12/4.
 */


public class RxTransformer {


/**
     * 无参数
     *
     * @param <T> 泛型
     * @return 返回Observable
     */

    public static <T> ObservableTransformer<T, T> switchSchedulers(boolean isLoading) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (isLoading) {
                        //LoadingHelper.getInstance().showLoading(MyApplication.getAppContext());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


}

