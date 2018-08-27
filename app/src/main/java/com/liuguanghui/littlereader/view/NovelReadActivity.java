package com.liuguanghui.littlereader.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuguanghui.littlereader.R;
import com.liuguanghui.littlereader.adapter.ReadCategoryAdapter;
import com.liuguanghui.littlereader.db.entity.ChapterBean;
import com.liuguanghui.littlereader.db.entity.NovelBean;
import com.liuguanghui.littlereader.db.helper.ChapterHelper;
import com.liuguanghui.littlereader.db.helper.NovelHelper;
import com.liuguanghui.littlereader.inter.IBookChapters;
import com.liuguanghui.littlereader.util.BrightnessUtils;
import com.liuguanghui.littlereader.util.ReadSettingManager;
import com.liuguanghui.littlereader.util.ScreenUtils;
import com.liuguanghui.littlereader.util.StatusBarUtils;
import com.liuguanghui.littlereader.util.StringUtils;
import com.liuguanghui.littlereader.viewmodel.BaseViewModel;
import com.liuguanghui.littlereader.viewmodel.VMBookContentInfo;
import com.liuguanghui.littlereader.widget.page.NetPageLoader;
import com.liuguanghui.littlereader.widget.page.PageLoader;
import com.liuguanghui.littlereader.widget.page.PageView;
import com.liuguanghui.littlereader.widget.page.TxtChapter;
import com.liuguanghui.littlereader.widget.theme.ColorView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NovelReadActivity extends AppCompatActivity implements IBookChapters {

    protected Context mContext;
    private Toolbar mToolbar;
    protected BaseViewModel mModel;
    //private Unbinder mUnbinder;
    private boolean isSlideBack = true;//是否设置滑动返回
    private ColorView mStatusBar;

    TextView mTvToolbarTitle;
    TextView mReadTvPreChapter;
    SeekBar mReadSbChapterProgress;
    TextView mReadTvNextChapter;
    TextView mReadTvCategory;
    TextView mReadTvNightMode;
    TextView mReadTvSetting;
    RecyclerView mRvReadCategory;
    DrawerLayout mReadDlSlide;
    private PageView mPvReadPage;
    AppBarLayout mReadAblTopMenu;
    LinearLayout mReadLlBottomMenu;
    TextView mReadTvPageTip;


    private static final String TAG = "ReadActivity";
    public static final int REQUEST_MORE_SETTING = 1;
    //注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");

    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";

    private boolean isRegistered = false;

    /*****************view******************/
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private NovelBean mCollBook;
    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;

    /***************params*****************/
    private boolean isCollected = false; //isFromSDCard
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    private Long mBookId;
    private Long mNetId;
    ReadCategoryAdapter mReadCategoryAdapter;
    List<TxtChapter> mTxtChapters = new ArrayList<>();
    private VMBookContentInfo mVmContentInfo;
    List<ChapterBean> bookChapterList = new ArrayList<>();

    private NovelHelper novelHelper;

    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            }
            //监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };

    //亮度调节监听
    //由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            //判断当前是否跟随屏幕亮度，如果不是则返回
          //  if (selfChange || !mSettingDialog.isBrightFollowSystem()) return;

            //如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(NovelReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(NovelReadActivity.this, BrightnessUtils.getScreenBrightness(NovelReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(NovelReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setBrightness(NovelReadActivity.this, BrightnessUtils.getScreenBrightness(NovelReadActivity.this));
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setBinddingView(R.layout.activity_read, NO_BINDDING, mVmContentInfo);

        setContentView(R.layout.activity_novel_read);
        mPvReadPage = findViewById(R.id.pv_read_page);
        mReadAblTopMenu = findViewById(R.id.read_abl_top_menu);
        mReadLlBottomMenu = findViewById(R.id.read_ll_bottom_menu);
        mReadTvPageTip = findViewById(R.id.read_tv_page_tip);
        mTvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        mReadTvPreChapter  = findViewById(R.id.read_tv_pre_chapter);
        mReadSbChapterProgress  = findViewById(R.id.read_sb_chapter_progress);
        mReadTvNextChapter  = findViewById(R.id.read_tv_next_chapter);
        mReadTvCategory  = findViewById(R.id.read_tv_category);
        mReadTvNightMode  = findViewById(R.id.read_tv_night_mode);
        mReadTvSetting  = findViewById(R.id.read_tv_setting);
        mRvReadCategory  = findViewById(R.id.rv_read_category);
        mReadDlSlide  = findViewById(R.id.read_dl_slide);


        mContext = this;
        mVmContentInfo = new VMBookContentInfo(mContext, this);
        initView();
    }

    private void initView() {
        // 得到intent 对象
        Intent intent = getIntent();
        String netId = intent.getStringExtra("netId");
        String novelId = intent.getStringExtra("novelId");
        //书籍有本地和
        if(novelHelper == null){
            novelHelper =NovelHelper.getsInstance();
        }
          mCollBook =  novelHelper.findBookById(Long.parseLong(netId),Long.parseLong(novelId));
        if(mCollBook!=null){
            // 本地存在， 书籍章节列表也存在
            mBookId = mCollBook.getId();
            mNetId = mCollBook.getNetid();
            isCollected = true;

        }


        isNightMode = ReadSettingManager.getInstance().isNightMode();
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();
        mTvToolbarTitle.setText(mCollBook.getTitle());
        StatusBarUtils.transparencyBar(this);
        mPageLoader = mPvReadPage.getPageLoader(false);
        mReadDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        initData();

        //更多设置dialog
       // mSettingDialog = new ReadSettingDialog(this, mPageLoader);
        setCategory();


        toggleNightMode();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setBrightness(this, BrightnessUtils.getScreenBrightness(this));
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");
        //隐藏StatusBar
        mPvReadPage.post(
                () -> hideSystemBar()
        );
        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();
        /*Thread t = new ReadingThread();
        t.start();*/

        mPageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {
            @Override
            public void onChapterChange(int pos) {
                setCategorySelect(pos);
            }

            @Override
            public void onLoadChapter(List<TxtChapter> chapters, int pos) {
                mVmContentInfo.loadContent(mNetId,mBookId, chapters);
                setCategorySelect(mPageLoader.getChapterPos());
                if (mPageLoader.getPageStatus() == NetPageLoader.STATUS_LOADING
                        || mPageLoader.getPageStatus() == NetPageLoader.STATUS_ERROR) {
                    //冻结使用
                    mReadSbChapterProgress.setEnabled(false);
                }
                //隐藏提示
                mReadTvPageTip.setVisibility(GONE);
                mReadSbChapterProgress.setProgress(0);
            }

            @Override
            public void onCategoryFinish(List<TxtChapter> chapters) {
                mTxtChapters.clear();
                mTxtChapters.addAll(chapters);
                mReadCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageCountChange(int count) {
                mReadSbChapterProgress.setEnabled(true);
                mReadSbChapterProgress.setMax(count - 1);
                mReadSbChapterProgress.setProgress(0);
            }

            @Override
            public void onPageChange(int pos) {
                mReadSbChapterProgress.post(() -> {
                    mReadSbChapterProgress.setProgress(pos);
                });
            }
        });
        mReadSbChapterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mReadLlBottomMenu.getVisibility() == VISIBLE) {
                    //显示标题
                    mReadTvPageTip.setText((progress + 1) + "/" + (mReadSbChapterProgress.getMax() + 1));
                    mReadTvPageTip.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //进行切换
                int pagePos = mReadSbChapterProgress.getProgress();
                if (pagePos != mPageLoader.getPagePos()) {
                    mPageLoader.skipToPage(pagePos);
                }
                //隐藏提示
                mReadTvPageTip.setVisibility(GONE);
            }
        });

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

    }

    private void initData() {
          {
            //如果是网络文件
            //如果是已经收藏的，那么就从数据库中获取目录
              isCollected = false;
            if (isCollected) {
                Disposable disposable = ChapterHelper.getsInstance().findNovelChaptersRx(mNetId,mBookId)
                         // .compose(RxUtils::toSimpleSingle)
                        .subscribe(beans -> {
                            mCollBook.setChapterBeans(beans);
                            mPageLoader.openBook(mCollBook);
                            //如果是被标记更新的,重新从网络中获取目录
                            if (mCollBook.getIsNoReadUpdate()) {
                                mVmContentInfo.loadChapters(mNetId,mBookId);
                            }
                        });
                mVmContentInfo.addDisposadle(disposable);
            } else {
                //加载书籍目录
                mVmContentInfo.loadChapters(mNetId,mBookId);
            }
        }
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

    @Override
    public void bookChapters(List<ChapterBean> chapterBeans) {
        bookChapterList.clear();
        for (ChapterBean   bean : chapterBeans) {
            bookChapterList.add(bean);
        }
        mCollBook.setChapterBeans(bookChapterList);

        //如果是更新加载，那么重置PageLoader的Chapter
        if (mCollBook.getIsNoReadUpdate() && isCollected) {
            mPageLoader.setChapterList(bookChapterList);
            //异步下载更新的内容存到数据库
            //TODO
            ChapterHelper.getsInstance().saveBookChaptersWithAsync(bookChapterList);

        } else {
            mPageLoader.openBook(mCollBook);
         }

    }

    @Override
    public void finishChapters() {

    }

    @Override
    public void errorChapters() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

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


    /**
     * 设置选中目录
     *
     * @param selectPos
     */
    private void setCategorySelect(int selectPos) {
        for (int i = 0; i < mTxtChapters.size(); i++) {
            TxtChapter chapter = mTxtChapters.get(i);
            if (i == selectPos) {
                chapter.setSelect(true);
            } else {
                chapter.setSelect(false);
            }
        }

        mReadCategoryAdapter.notifyDataSetChanged();
    }

    private void setCategory() {
        mRvReadCategory.setLayoutManager(new LinearLayoutManager(mContext));
        mReadCategoryAdapter = new ReadCategoryAdapter(mTxtChapters);
        mRvReadCategory.setAdapter(mReadCategoryAdapter);

        if (mTxtChapters.size() > 0) {
            setCategorySelect(0);
        }

        mReadCategoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            setCategorySelect(position);
            mReadDlSlide.closeDrawer(Gravity.START);
            mPageLoader.skipToChapter(position);
        });

    }

    private void toggleNightMode() {
        if (isNightMode) {
            mReadTvNightMode.setText(StringUtils.getString(R.string.modeMorning));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_morning);
            mReadTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            mReadTvNightMode.setText(StringUtils.getString(R.string.modeNight));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_night);
            mReadTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }
}
