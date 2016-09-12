package cn.chono.yopper.activity.usercenter;


import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.FeedBack.FeedBackBean;
import cn.chono.yopper.Service.Http.FeedBack.FeedBackService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 意见反馈
 *
 * @author sam.sun
 */
public class FeedBackActivity extends MainFrameActivity {


    private EditText feed_back_et;

    private TextView feed_back_submit_tv;

    private LinearLayout feed_back_go_back_layout;


    private Dialog hintdialog;
    private  boolean ispost=false;

    private Dialog loadingDiaog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        PushAgent.getInstance(this).onAppStart();

        initComponent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("意见反馈"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("意见反馈"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {


        feed_back_et = (EditText) this.findViewById(R.id.feed_back_et);

        feed_back_submit_tv = (TextView) this.findViewById(R.id.feed_back_submit_tv);

        feed_back_go_back_layout = (LinearLayout) this.findViewById(R.id.feed_back_go_back_layout);


        feed_back_go_back_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 500);
                finish();
            }
        });

        feed_back_submit_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 500);
                if(!ispost){
                    ispost=true;
                    post_feed_back();
                }

            }
        });

    }


    //获取P果数量
    private void post_feed_back() {

        String str = feed_back_et.getText().toString().trim();

        if (CheckUtil.isEmpty(str)) {
            ispost=false;
            return;
        }

        loadingDiaog = DialogUtil.LoadingDialog(FeedBackActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }


        FeedBackBean feedBackBean = new FeedBackBean();
        feedBackBean.setText(str);


        FeedBackService feedBackService = new FeedBackService(this);
        feedBackService.parameter(feedBackBean);
        feedBackService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                ispost=false;
                loadingDiaog.dismiss();
                hintdialog = DialogUtil.createSuccessHintDialog(FeedBackActivity.this, "反馈成功!");
                if (!FeedBackActivity.this.isFinishing()) {
                    hintdialog.show();
                    successtimer = new SuccessTimer(2000, 1000);
                    successtimer.start();
                }

            }
        },new OnCallBackFailListener(){
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();
                ispost=false;
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(FeedBackActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(FeedBackActivity.this, msg);
            }
        });

        feedBackService.enqueue();

    }


    private SuccessTimer successtimer;

    private class SuccessTimer extends CountDownTimer {

        public SuccessTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            if (hintdialog != null) {
                hintdialog.dismiss();
            }

            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

}
