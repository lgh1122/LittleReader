package com.liuguanghui.littlereader;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.liuguanghui.littlereader.api.BookService;
import com.liuguanghui.littlereader.db.entity.ChapterBean;
import com.liuguanghui.littlereader.util.JsonResult;
import com.liuguanghui.littlereader.util.rxhelper.RxObserver;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    /*@Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        System.out.println("包名" +appContext.getPackageName());
        Log.i("TESTLITT","包名" +appContext.getPackageName());
        assertEquals("com.liuguanghui.littlereader", appContext.getPackageName());
    }*/
    @Test
    public void rxHttpTest(){
        RxHttpUtils.getSInstance().createSApi(BookService.class)
                .bookChapters(3l,761l)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        Log.e("Error",errorMsg);
                        int i = 0;
                        i++;
                    }

                    @Override
                    protected void onSuccess(String data) {
                        if(data != null){

                        }
                        JsonResult jsonResult = JsonResult.formatToList(data,ChapterBean.class);
                        if (jsonResult.getStatus() == 200) {
                            List<ChapterBean> chapterBeans = (List<ChapterBean>) jsonResult.getData();
if(chapterBeans !=null && chapterBeans.size()>0){
    ChapterBean chapterBean = chapterBeans.get(0);
}

                        }
                    }
                });
    }




}
