package cn.chono.yopper.activity.video;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanqu.qupai.utils.AppGlobalSetting;
import com.google.common.io.Files;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.video.VideoContant;
import cn.chono.yopper.utils.video.VideoFileUtils;
import cn.chono.yopper.utils.video.VideoRecordResult;
import cn.chono.yopper.utils.video.VideoRequestCode;

/**
 * 视频认证提交成功页面
 *
 * @author sam.sun
 */
public class VideoWelcomeActivity extends MainFrameActivity {

    private LayoutInflater mInflater;
    private LinearLayout video_welcome_back_layout;
    private Button video_welcome_go_ver_tv;

    private TextView video_welcome_verification_tv;

    private int sex;
    private int userid;
    private List<View> list;
    private VideoWelcomeAdapter adapter;
    private ViewPager video_welcome_viewpager;
    private LinearLayout video_welcome_indicator;
    private ImageView[] indicators = null;

    private LinearLayout.LayoutParams params;

    private ImageView indicator_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();


        setContentView(R.layout.video_welcome_activity);
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userid = LoginUser.getInstance().getUserId();

        sex = DbHelperUtils.getDbUserSex(userid);
        initComponent();

        initLayout();
        initViewPager();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("视频认证引导页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("视频认证引导页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {
        video_welcome_back_layout = (LinearLayout) this.findViewById(R.id.video_welcome_back_layout);
        video_welcome_verification_tv = (TextView) this.findViewById(R.id.video_welcome_verification_tv);
        video_welcome_verification_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        video_welcome_verification_tv.getPaint().setAntiAlias(true);//抗锯齿

        video_welcome_go_ver_tv = (Button) this.findViewById(R.id.video_welcome_go_ver_tv);

        video_welcome_viewpager = (ViewPager) this.findViewById(R.id.video_welcome_viewpager);
        video_welcome_indicator = (LinearLayout) this.findViewById(R.id.video_welcome_indicator);
        video_welcome_back_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //视频认证规范
        video_welcome_verification_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundles = new Bundle();
                bundles.putString(YpSettings.BUNDLE_KEY_WEB_URL, "video");
                bundles.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "视频认证规范");
                bundles.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);
                ActivityUtil.jump(VideoWelcomeActivity.this, SimpleWebViewActivity.class, bundles, 0, 100);
            }
        });
        //去认证
        video_welcome_go_ver_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (null != App.getInstance().qupaiService) {

                    //引导，只显示一次，这里用SharedPreferences记录
                    final AppGlobalSetting sp = new AppGlobalSetting(getApplicationContext());

                    Boolean isGuideShow = sp.getBooleanGlobalItem(YpSettings.PREF_VIDEO_EXIST_USER, true);


                    /**
                     * 建议上面的initRecord只在application里面调用一次。这里为了能够开发者直观看到改变所以可以调用多次
                     */
                    App.getInstance().qupaiService.showRecordPage(VideoWelcomeActivity.this, YpSettings.RECORDE_SHOW, isGuideShow);

                    sp.saveGlobalConfigItem(YpSettings.PREF_VIDEO_EXIST_USER, false);

                    Bundle bundles = new Bundle();

                    ActivityUtil.jump(VideoWelcomeActivity.this, VideoWindowPurposeActivity.class, bundles, 0, 100);
                }

            }
        });
    }

    private void initLayout() {
        list = new ArrayList<View>();
        indicators = new ImageView[3]; // 定义指示器数组大小
        for (int i = 0; i < 3; i++) {
            View view = mInflater.inflate(R.layout.video_welcome_viewpage_item, null);
            list.add(view);

            params = new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);

            // 循环加入指示器
            indicator_view = new ImageView(this);
            indicator_view.setBackgroundResource(R.drawable.video_welcome_page_no_select);

            indicators[i] = indicator_view;
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.video_welcome_page_select);
            }
            video_welcome_indicator.addView(indicators[i], params);
        }

    }

    /**
     * @throws
     * @Title: initViewPager
     * @Description: ViewPager 事件绑定和属性设置(这里用一句话描述这个方法的作用)
     * @param:
     * @return: void
     */
    private void initViewPager() {

        // 设置viewpager缓存长度个数
        video_welcome_viewpager.setOffscreenPageLimit(list.size());
        adapter = new VideoWelcomeAdapter();
        video_welcome_viewpager.setAdapter(adapter);
        video_welcome_viewpager.setOnPageChangeListener(new WelcomePageChangeListener());

    }

    /**
     * @ClassName: FindAppointmentAdapter
     */
    class VideoWelcomeAdapter extends PagerAdapter {

        private View itemView;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            itemView = list.get(position);
            ((ViewPager) container).addView(itemView, position);
            ImageView video_welcome_page_item_iv = (ImageView) itemView.findViewById(R.id.video_welcome_page_item_iv);

            TextView video_welcome_page_item_one_tv = (TextView) itemView.findViewById(R.id.video_welcome_page_item_one_tv);

            TextView video_welcome_page_item_two_tv = (TextView) itemView.findViewById(R.id.video_welcome_page_item_two_tv);

            if (position == 0) {
                if (sex == 1) {
                    video_welcome_page_item_iv.setBackgroundResource(R.drawable.video_welcome_man_one);
                } else {
                    video_welcome_page_item_iv.setBackgroundResource(R.drawable.video_welcome_woman_one);
                }
                video_welcome_page_item_one_tv.setText("100%真实认证");
                video_welcome_page_item_two_tv.setText("网络不再是虚拟世界");
            } else if (position == 1) {
                if (sex == 1) {
                    video_welcome_page_item_iv.setBackgroundResource(R.drawable.video_welcome_man_two);
                } else {
                    video_welcome_page_item_iv.setBackgroundResource(R.drawable.video_welcome_woman_two);
                }
                video_welcome_page_item_one_tv.setText("可查看对方认证视频");
                video_welcome_page_item_two_tv.setText("彼此交换最真实的一面");

            } else if (position == 2) {
                if (sex == 1) {
                    video_welcome_page_item_iv.setBackgroundResource(R.drawable.video_welcome_man_three);
                    video_welcome_page_item_one_tv.setText("获得女生的青睐");
                    video_welcome_page_item_two_tv.setText("她只喜欢真实自信的你");
                } else {
                    video_welcome_page_item_iv.setBackgroundResource(R.drawable.video_welcome_woman_three);
                    video_welcome_page_item_one_tv.setText("拒绝非认证用户消息");
                    video_welcome_page_item_two_tv.setText("不敢真面目示人的别来烦我");
                }
            }
            return itemView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            itemView = (View) object;
            super.setPrimaryItem(container, position, object);
        }

        public View getPrimaryItem() {
            return itemView;
        }

    }

    /**
     * @ClassName: WelcomePageChangeListener
     */
    class WelcomePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            // 更改指示器图片
            for (int i = 0; i < 3; i++) {
                indicators[arg0].setBackgroundResource(R.drawable.video_welcome_page_select);
                if (arg0 != i) {
                    indicators[i].setBackgroundResource(R.drawable.video_welcome_page_no_select);
                }
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case VideoRequestCode.RECORDE_SHOW:
                if (resultCode == Activity.RESULT_OK) {

                    VideoRecordResult result = new VideoRecordResult(data);
                    String videoPath = result.getPath();
                    String[] image = result.getThumbnail();
                    result.getDuration();
                    try {
                        //拷贝move操作
                        Files.move(new File(videoPath), new File(VideoContant.VIDEOPATH));
                        String[] images = new String[10];
                        for (int i = 0; i < image.length; i++) {
                            String imageUrl = VideoContant.THUMBPATH + System.currentTimeMillis() + ".png";
                            Files.move(new File(image[i]), new File(imageUrl));
                            images[i] = imageUrl;
                        }

                        //清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作
                        if (null != App.getInstance().qupaiService) {
                            App.getInstance().qupaiService.deleteDraft(VideoWelcomeActivity.this, data);

                        }


                        //跳转到编辑界面
                        Bundle be = new Bundle();
                        be.putString(YpSettings.VIDEO_PATH_NAME, VideoFileUtils.newOutgoingFilePath());
                        be.putStringArray(YpSettings.VIDEO_PATH_NAME_IMG, images);
                        ActivityUtil.jump(VideoWelcomeActivity.this, VideoCoverActivity.class, be, 0, 100);


                    } catch (IOException e) {
                        e.printStackTrace();


                    }
                }
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);

    }


}
