package cn.chono.yopper.activity.video;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.SubmitDatingPurpose.SubmitDatingPurposeBean;
import cn.chono.yopper.Service.Http.SubmitDatingPurpose.SubmitDatingPurposeRespBean;
import cn.chono.yopper.Service.Http.SubmitDatingPurpose.SubmitDatingPurposeService;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 修改交友目的
 *
 * @author sam.sun
 */
public class ChangeDatingPurposeActivity extends MainFrameActivity implements OnClickListener {

    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private LinearLayout change_dating_purpose_friend;

    private ImageView change_dating_purpose_friend_iv;

    private TextView change_dating_purpose_friend_tv;

    private LinearLayout change_dating_purpose_love;

    private ImageView change_dating_purpose_love_iv;

    private TextView change_dating_purpose_love_tv;

    private LinearLayout change_dating_purpose_married;

    private ImageView change_dating_purpose_married_iv;

    private TextView change_dating_purpose_married_tv;

    private int purpose, result_code;
    private Dialog loadingDiaog;
    private boolean isPost = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            purpose = bundle.getInt(YpSettings.VIDEO_PURPOSE, 1);
            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }

        initComponent();
        setSelectedPurpose(purpose);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("交友目的"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("交友目的"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    private void setSelectedPurpose(int id) {
        switch (id) {
            case 1:
                change_dating_purpose_friend.setBackgroundResource(R.color.color_ff7462);
                change_dating_purpose_love.setBackgroundResource(R.color.color_ffffff);
                change_dating_purpose_married.setBackgroundResource(R.color.color_ffffff);

                change_dating_purpose_friend_iv.setImageResource(R.drawable.change_dating_purpose_friend_icon_select);
                change_dating_purpose_friend_tv.setTextColor(this.getResources().getColor(R.color.color_ffffff));

                change_dating_purpose_love_iv.setImageResource(R.drawable.change_dating_purpose_love_icon);
                change_dating_purpose_love_tv.setTextColor(this.getResources().getColor(R.color.color_000000));

                change_dating_purpose_married_iv.setImageResource(R.drawable.change_dating_purpose_married_icon);
                change_dating_purpose_married_tv.setTextColor(this.getResources().getColor(R.color.color_000000));



                break;

            case 2:
                change_dating_purpose_friend.setBackgroundResource(R.color.color_ffffff);
                change_dating_purpose_love.setBackgroundResource(R.color.color_ff7462);
                change_dating_purpose_married.setBackgroundResource(R.color.color_ffffff);


                change_dating_purpose_friend_iv.setImageResource(R.drawable.change_dating_purpose_friend_icon);
                change_dating_purpose_friend_tv.setTextColor(this.getResources().getColor(R.color.color_000000));

                change_dating_purpose_love_iv.setImageResource(R.drawable.change_dating_purpose_love_icon_select);
                change_dating_purpose_love_tv.setTextColor(this.getResources().getColor(R.color.color_ffffff));

                change_dating_purpose_married_iv.setImageResource(R.drawable.change_dating_purpose_married_icon);
                change_dating_purpose_married_tv.setTextColor(this.getResources().getColor(R.color.color_000000));

                break;

            case 3:
                change_dating_purpose_friend.setBackgroundResource(R.color.color_ffffff);
                change_dating_purpose_love.setBackgroundResource(R.color.color_ffffff);
                change_dating_purpose_married.setBackgroundResource(R.color.color_ff7462);

                change_dating_purpose_friend_iv.setImageResource(R.drawable.change_dating_purpose_friend_icon);
                change_dating_purpose_friend_tv.setTextColor(this.getResources().getColor(R.color.color_000000));

                change_dating_purpose_love_iv.setImageResource(R.drawable.change_dating_purpose_love_icon);
                change_dating_purpose_love_tv.setTextColor(this.getResources().getColor(R.color.color_000000));

                change_dating_purpose_married_iv.setImageResource(R.drawable.change_dating_purpose_married_icon_select);
                change_dating_purpose_married_tv.setTextColor(this.getResources().getColor(R.color.color_ffffff));


                break;
        }
    }


    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("交友目的");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.VIDEO_PURPOSE, purpose);
                intent.putExtras(bundle);
                ChangeDatingPurposeActivity.this.setResult(result_code, intent);
                finish();
            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contextView = mInflater.inflate(R.layout.change_datingpurpose_activity, null);


        change_dating_purpose_friend = (LinearLayout) contextView.findViewById(R.id.change_dating_purpose_friend);
        change_dating_purpose_friend.setOnClickListener(this);

        change_dating_purpose_love = (LinearLayout) contextView.findViewById(R.id.change_dating_purpose_love);
        change_dating_purpose_love.setOnClickListener(this);

        change_dating_purpose_married = (LinearLayout) contextView.findViewById(R.id.change_dating_purpose_married);
        change_dating_purpose_married.setOnClickListener(this);
        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        change_dating_purpose_friend_iv= (ImageView) findViewById(R.id.change_dating_purpose_friend_iv);
        change_dating_purpose_friend_tv= (TextView) findViewById(R.id.change_dating_purpose_friend_tv);

        change_dating_purpose_love_iv= (ImageView) findViewById(R.id.change_dating_purpose_love_iv);
        change_dating_purpose_love_tv= (TextView) findViewById(R.id.change_dating_purpose_love_tv);

        change_dating_purpose_married_iv= (ImageView) findViewById(R.id.change_dating_purpose_married_iv);
        change_dating_purpose_married_tv= (TextView) findViewById(R.id.change_dating_purpose_married_tv);

    }


    //提交交友目的
    private void submitDatingPurpose(int type) {


        SubmitDatingPurposeBean purposeBean = new SubmitDatingPurposeBean();
        purposeBean.setPurpose(type);
        purposeBean.setUserId(LoginUser.getInstance().getUserId());


        SubmitDatingPurposeService purposeService = new SubmitDatingPurposeService(this);
        purposeService.parameter(purposeBean);
        purposeService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                SubmitDatingPurposeRespBean purposeRespBean = (SubmitDatingPurposeRespBean) respBean;

                SubmitDatingPurposeRespBean.Purpose respPurpose = purposeRespBean.getResp();

                loadingDiaog.dismiss();
                isPost = false;
                if (null != respPurpose) {
                    purpose = respPurpose.getPurpose();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt(YpSettings.VIDEO_PURPOSE, purpose);
                    intent.putExtras(bundle);
                    ChangeDatingPurposeActivity.this.setResult(result_code, intent);
                    ChangeDatingPurposeActivity.this.finish();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();
                isPost = false;
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(ChangeDatingPurposeActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ChangeDatingPurposeActivity.this, msg);
            }
        });
        purposeService.enqueue();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.change_dating_purpose_friend:
                if (!isPost) {
                    isPost = true;
                    loadingDiaog = DialogUtil.LoadingDialog(ChangeDatingPurposeActivity.this, null);
                    if (!isFinishing()) {
                        loadingDiaog.show();
                    }
                    submitDatingPurpose(1);
                }
                break;

            case R.id.change_dating_purpose_love:
                if (!isPost) {
                    isPost = true;
                    loadingDiaog = DialogUtil.LoadingDialog(ChangeDatingPurposeActivity.this, null);
                    if (!isFinishing()) {
                        loadingDiaog.show();
                    }
                    submitDatingPurpose(2);
                }
                break;

            case R.id.change_dating_purpose_married:
                if (!isPost) {
                    isPost = true;
                    loadingDiaog = DialogUtil.LoadingDialog(ChangeDatingPurposeActivity.this, null);
                    if (!isFinishing()) {
                        loadingDiaog.show();
                    }
                    submitDatingPurpose(3);
                }
                break;

        }
    }

}
