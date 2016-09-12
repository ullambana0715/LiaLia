package cn.chono.yopper.activity.video;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.base.IndexActivity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DbHelperUtils;

/**
 * 视频认证提交成功页面
 *
 * @author sam.sun
 */
public class VideoCommitFinishActivity extends MainFrameActivity {


    private TextView video_commit_success_finish_tv;

    private ImageView video_commit_success_sex_iv;

    private int sex;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.video_commit_success_activity);
        sex = DbHelperUtils.getDbUserSex(LoginUser.getInstance().getUserId());

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.VIDEO_PATH_TYPE))
            type=getIntent().getExtras().getString(YpSettings.VIDEO_PATH_TYPE);

        initComponent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("视频提交成功页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("视频提交成功页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        video_commit_success_sex_iv = (ImageView) this.findViewById(R.id.video_commit_success_sex_iv);

        video_commit_success_finish_tv = (TextView) this.findViewById(R.id.video_commit_success_finish_tv);

        if (sex == 1) {
            video_commit_success_sex_iv.setBackgroundResource(R.drawable.video_commit_success_man);
        } else {
            video_commit_success_sex_iv.setBackgroundResource(R.drawable.video_commit_success_women);
        }

        video_commit_success_finish_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.equals(YpSettings.VIDEO_PATH_TYPE_UserInfoPresenter, type)) {

                    CommonItemEvent commonItemEvent = new CommonItemEvent();

                    RxBus.get().post("UserInfoActivityGeneralVideosEvent", commonItemEvent);

                    finish();

                } else {

                    ActivityUtil.jump(VideoCommitFinishActivity.this, IndexActivity.class, null, 1, 200);

                }


            }
        });
    }


}
