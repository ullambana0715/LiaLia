package cn.chono.yopper.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import cn.chono.yopper.AppInfo;
import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.adapter.BasePagerAdapter;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.utils.ViewsUtils;

public class WelcomeActivity extends MainFrameActivity implements OnClickListener {

    //滑动图片的数组
    private int[] images;

    //引导页使用的pageview适配器
    private BasePagerAdapter pagerAdapter;

    private ImageView imageView;

    private ImageView iview;

    //ViewPager控件，用于滑动引导图片
    private ViewPager viewPager;

    //引导指示器
    private LinearLayout indicator;

    private ArrayList<View> views;
    private ImageView[] indicators = null;

    private LinearLayout.LayoutParams params;

    //进入按钮
    private Button welcome_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.welcome_activity);

        // 创建桌面快捷方式
        CreateShortcuts(this);
        PushAgent.getInstance(this).onAppStart();

        getTitleLayout().setVisibility(View.GONE);// 隐藏标题
        isMzMoble();// 判断是否是魅族手机
        initView();// 此方法必须在isMzMoble()之后。 初始化视图与数据绑定
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageEnd("引导页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("引导页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 监听事件
     */
    @Override
    public void onClick(View v) {
        ViewsUtils.preventViewMultipleClick(v, false);

        SharedprefUtil.save(WelcomeActivity.this, YpSettings.VersionName, AppInfo.getInstance().getVersionName());

        if (TextUtils.isEmpty(LoginUser.getInstance().getAuthToken())) {

            ActivityUtil.jump(WelcomeActivity.this, SelectEntryActivity.class, null, 0, 0);
            finish();
        } else {
            // 已经登录
            ActivityUtil.jump(WelcomeActivity.this, IndexActivity.class, null, 0, 100);
            finish();
        }
    }

    /**
     * 判断是否是魅族手机
     */
    private void isMzMoble() {

        if (UnitUtil.checkDeviceHasNavigationBar(WelcomeActivity.this)) {
            images = new int[]{R.drawable.mx_one, R.drawable.mx_two, R.drawable.mx_three};
        } else {
            images = new int[]{R.drawable.guide_one, R.drawable.guide_two, R.drawable.guide_three};
        }
    }

    /**
     * 初始化视图
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        welcome_btn = (Button) findViewById(R.id.welcome_btn);
        welcome_btn.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpage);
        indicator = (LinearLayout) findViewById(R.id.welcome_indicator);
        views = new ArrayList<View>();
        indicators = new ImageView[images.length]; // 定义指示器数组大小
        float mScale = UnitUtil.getScreenDIP(this);// 手机密度
        int imagePadding = (int) (mScale * 5 + 0.5f);
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < images.length; i++) {
            // 循环加入图片
            imageView = new ImageView(this);
            params.setMargins(imagePadding, imagePadding, imagePadding, imagePadding);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(this).load(images[i]).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);

            views.add(imageView);
            // 循环加入指示器
            iview = new ImageView(this);
            iview.setBackgroundResource(R.drawable.guide_indicators_default);

            indicators[i] = iview;
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.guide_indicators_now);
            }
            indicator.addView(indicators[i], params);

        }
        pagerAdapter = new BasePagerAdapter(views);
        viewPager.setAdapter(pagerAdapter); // 设置适配器
        viewPager.addOnPageChangeListener(new PageChageListener());
    }

    private class PageChageListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (arg0 == images.length - 1) {
                indicator.setVisibility(View.GONE);
                welcome_btn.setVisibility(View.VISIBLE);

            } else {
                indicator.setVisibility(View.VISIBLE);
                welcome_btn.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageSelected(int arg0) {
            // 更改指示器图片
            for (int i = 0; i < indicators.length; i++) {
                indicators[arg0].setBackgroundResource(R.drawable.guide_indicators_now);
                if (arg0 != i) {
                    indicators[i].setBackgroundResource(R.drawable.guide_indicators_default);
                }
            }

        }

    }

    private void CreateShortcuts(Activity activity) {
        // intent进行隐式跳转,到桌面创建快捷方式
        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        addIntent.putExtra("duplicate", false);
        // 得到应用的名称
        String title = activity.getResources().getString(R.string.app_name);
        // 将应用的图标设置为Parceable类型
        Parcelable icon = Intent.ShortcutIconResource.fromContext(activity, R.drawable.application_icon);
        // 点击图标之后的意图操作
        Intent myIntent = new Intent(activity, StartActivity.class);
        // 设置快捷方式的名称
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 设置快捷方式的图标
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 设置快捷方式的意图
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
        // 发送广播
        activity.sendBroadcast(addIntent);
    }

}
