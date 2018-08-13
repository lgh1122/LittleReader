package com.liuguanghui.littlereader.turn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.liuguanghui.littlereader.R;

import java.io.File;
import java.io.IOException;

public class TurnActivity extends Activity {
    /** Called when the activity is first created. */
    private PageWidget mPageWidget;
    Bitmap mCurPageBitmap, mNextPageBitmap;
    Canvas mCurPageCanvas, mNextPageCanvas;
    BookPageFactory pagefactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        mPageWidget = new PageWidget(this,screenWidth,screenHeight);
        setContentView(mPageWidget);

        mCurPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap
                .createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        pagefactory = new BookPageFactory(screenWidth, screenHeight);//设置分辨率为480*800

        pagefactory.setBgBitmap(BitmapFactory.decodeResource(
                this.getResources(), R.drawable.bg));//设置背景图片
        try {
            //SYS_TEMMD
            String filesPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            // 文件路径
            filesPath = Environment.getExternalStorageDirectory().toString();
            String filePath = filesPath +File.separator+"DBcamrea"+File.separator+"26316108.txt";

            pagefactory.openbook(filePath);//打开文件
            pagefactory.onDraw(mCurPageCanvas);//将文字绘于手机屏幕
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Toast.makeText(this, "电子书不存在,请将《/1/26316108.txt》放在SD卡根目录下",
                    Toast.LENGTH_SHORT).show();
        }

        mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);

        mPageWidget.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                // TODO Auto-generated method stub

                boolean ret=false;
                if (v == mPageWidget) {
                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        //停止动画。与forceFinished(boolean)相反，Scroller滚动到最终x与y位置时中止动画。
                        mPageWidget.abortAnimation();
                        //计算拖拽点对应的拖拽角
                        mPageWidget.calcCornerXY(e.getX(), e.getY());
                        //将文字绘于当前页
                        pagefactory.onDraw(mCurPageCanvas);
                        if (mPageWidget.DragToRight()) {
                            //是否从左边翻向右边
                            try {
                                //true，显示上一页
                                pagefactory.prePage();
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            if(pagefactory.isfirstPage())return false;
                            pagefactory.onDraw(mNextPageCanvas);
                        } else {
                            try {
                                //false，显示下一页
                                pagefactory.nextPage();
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            if(pagefactory.islastPage())return false;
                            pagefactory.onDraw(mNextPageCanvas);
                        }
                        mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
                    }

                    ret = mPageWidget.doTouchEvent(e);
                    return ret;
                }
                return false;
            }

        });
    }
}
