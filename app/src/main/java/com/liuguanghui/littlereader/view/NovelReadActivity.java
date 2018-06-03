package com.liuguanghui.littlereader.view;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguanghui.littlereader.R;
import com.liuguanghui.littlereader.page.PageView;
import com.liuguanghui.littlereader.util.ReadSettingManager;
import com.liuguanghui.littlereader.util.ScreenUtils;
import com.liuguanghui.littlereader.util.StatusBarUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NovelReadActivity extends AppCompatActivity {


    private  PageView mPvReadPage;
    AppBarLayout mReadAblTopMenu;
    LinearLayout mReadLlBottomMenu;
    TextView mReadTvPageTip;
    private boolean isFullScreen = false;

    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_read);
        mPvReadPage = findViewById(R.id.pv_read_page);
        mReadAblTopMenu = findViewById(R.id.read_abl_top_menu);
        mReadLlBottomMenu = findViewById(R.id.read_ll_bottom_menu);
        mReadTvPageTip = findViewById(R.id.read_tv_page_tip);


        initView();

    }

    private void initView() {

        mPvReadPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public boolean prePage() {
                return true;
            }

            @Override
            public boolean nextPage() {
                return true;
            }

            @Override
            public void cancel() {
            }
        });

        mPvReadPage.post(
                () -> hideSystemBar()
        );
        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();
        Thread t = new ReadingThread();
        t.start();

    }



    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            mReadAblTopMenu.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        }
    }

    private void initBottomMenu() {
        //判断是否全屏
        if (ReadSettingManager.getInstance().isFullScreen()) {
            //还需要设置mBottomMenu的底部高度
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mReadLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            mReadLlBottomMenu.setLayoutParams(params);
        } else {
            //设置mBottomMenu的底部距离
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mReadLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            mReadLlBottomMenu.setLayoutParams(params);
        }
    }
    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {
        hideSystemBar();
        if (mReadAblTopMenu.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        }
        return false;
    }

    private void hideSystemBar() {
        //隐藏
        StatusBarUtils.hideStableStatusBar(this);
        if (true) {
            StatusBarUtils.hideStableNavBar(this);
        }
    }

    private void showSystemBar() {
        //显示
        StatusBarUtils.showUnStableStatusBar(this);
        if (isFullScreen) {
            StatusBarUtils.showUnStableNavBar(this);
        }
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (mReadAblTopMenu.getVisibility() == View.VISIBLE) {
            //关闭
            mReadAblTopMenu.startAnimation(mTopOutAnim);
            mReadLlBottomMenu.startAnimation(mBottomOutAnim);
            mReadAblTopMenu.setVisibility(GONE);
            mReadLlBottomMenu.setVisibility(GONE);
            mReadTvPageTip.setVisibility(GONE);


            if (hideStatusBar) {
                hideSystemBar();
            }
        } else {
            showSystemBar();
            mReadAblTopMenu.setVisibility(View.VISIBLE);
            mReadLlBottomMenu.setVisibility(View.VISIBLE);
            mReadAblTopMenu.startAnimation(mTopInAnim);
            mReadLlBottomMenu.startAnimation(mBottomInAnim);


        }
    }

    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }

    private String text = "";
    private int textLenght = 0;

    private static final int COUNT = 400;

    private int currentTopEndIndex = 0;

    private int currentShowEndIndex = 0;

    private int currentBottomEndIndex = 0;

    private class ReadingThread extends Thread {
        public void run() {
            AssetManager am = getAssets();
            InputStream response;
            try {
                response = am.open("text.txt");
                if (response != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = -1;
                    while ((i = response.read()) != -1) {
                        baos.write(i);
                    }
                    text = new String(baos.toByteArray(), "UTF-8");
                    baos.close();
                    response.close();
                    handler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {


            textLenght = text.length();

            System.out.println("----textLenght----->" + textLenght);


            if (textLenght > COUNT) {
               /* textView.setText(text.subSequence(0, COUNT));
                textView = (TextView) view2.findViewById(R.id.textview);*/
                if (textLenght > (COUNT << 1)) {
                   // mPvReadPage.setText(text.subSequence(COUNT, COUNT * 2));
                    currentShowEndIndex = COUNT;
                    currentBottomEndIndex = COUNT << 1;
                } else {
                   // mPvReadPage.setText(text.subSequence(COUNT, textLenght));
                    currentShowEndIndex = textLenght;
                    currentBottomEndIndex = textLenght;
                }
            } else {
              //  mPvReadPage.setText(text.subSequence(0, textLenght));
                currentShowEndIndex = textLenght;
                currentBottomEndIndex = textLenght;
            }
        };
    };
}
