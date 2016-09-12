package cn.chono.yopper.activity.usercenter;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.GainVerifiCode.GainVerifiCodeBean;
import cn.chono.yopper.Service.Http.GainVerifiCode.GainVerifiCodeService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 设置手机号
 *
 * @author sam.sun
 */
public class SettingPhoneActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private TextView setting_phone_one_hint_tv;

    private TextView setting_phone_two_hint_tv;

    private EditText setting_phone_et;

    private ImageView setting_phone_delete_iv;

    private int userID;


    //1:个人资料认证手机号码；2:个人资料更换手机号码；3:设置页面输入手机号码
    private int frompage;

    private String oldmobile;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        Bundle bundle=this.getIntent().getExtras();
        frompage=bundle.getInt(YpSettings.FROM_PAGE);
        if(frompage==2){
            oldmobile =bundle.getString("mobile");
        }

        if (bundle.containsKey(YpSettings.USERID))
            userID=bundle.getInt(YpSettings.USERID);

        initComponent();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("手机号认证"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("手机号认证"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    /**
     * 初始化
     */
    private void initComponent() {


        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.VISIBLE);
        this.getBtnOption().setVisibility(View.GONE);
        this.gettvOption().setText("下一步");
        this.gettvOption().setTextColor(getResources().getColor(R.color.color_ffdbd7));

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();

            }
        });

        this.getOptionLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mobile = setting_phone_et.getText().toString().trim();

                if (CheckUtil.isEmpty(mobile)) {
                    return;
                }

                if(!CheckUtil.isCellPhone(mobile)){
                    DialogUtil.showDisCoverNetToast(SettingPhoneActivity.this,"请输入正确手机号码");
                    return;
                }

                enqueueGainVerifinCodeService(mobile);

            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contextView = mInflater.inflate(R.layout.setting_phone_activity, null);


        setting_phone_one_hint_tv = (TextView) contextView.findViewById(R.id.setting_phone_one_hint_tv);
        setting_phone_two_hint_tv = (TextView) contextView.findViewById(R.id.setting_phone_two_hint_tv);
        setting_phone_et = (EditText) contextView.findViewById(R.id.setting_phone_et);
        setting_phone_delete_iv = (ImageView) contextView.findViewById(R.id.setting_phone_delete_iv);


        // 设置标题栏
        if (frompage == 1) {
            this.getTvTitle().setText("手机号认证");
            setting_phone_one_hint_tv.setText("认证成功后，可以使用手机号登录");
            setting_phone_two_hint_tv.setVisibility(View.GONE);
        } else if (frompage == 2) {
            this.getTvTitle().setText("绑定手机号");
            setting_phone_one_hint_tv.setText("更换手机号后，可以使用新手机号登录，");
            setting_phone_two_hint_tv.setText("当前手机号码" + oldmobile);

        } else if (frompage == 3) {
            this.getTvTitle().setText("认证手机号");
            setting_phone_one_hint_tv.setText("绑定手机号并设置密码后，");
            setting_phone_two_hint_tv.setText("可以使用手机号+密码登录俩俩");
        }


        setting_phone_delete_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_phone_et.setText("");
                setting_phone_delete_iv.setVisibility(View.GONE);
            }
        });

        setting_phone_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mobile = setting_phone_et.getText().toString().trim();

                LogUtils.e("输入的手机号码＝" + mobile);

                if (!TextUtils.isEmpty(mobile)) {

                    setting_phone_delete_iv.setVisibility(View.VISIBLE);
                    SettingPhoneActivity.this.gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));
                } else {

                    setting_phone_delete_iv.setVisibility(View.GONE);
                    SettingPhoneActivity.this.gettvOption().setTextColor(getResources().getColor(R.color.color_ffdbd7));
                }

            }
        });


        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

private Dialog loadingDiaog;

    public void enqueueGainVerifinCodeService(final String mobile) {

        loadingDiaog = DialogUtil.LoadingDialog(SettingPhoneActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        GainVerifiCodeBean gainVerifiCodeBean = new GainVerifiCodeBean();
        gainVerifiCodeBean.setMobile(mobile);
        GainVerifiCodeService gainVerifiCodeService = new GainVerifiCodeService(this);
        gainVerifiCodeService.parameter(gainVerifiCodeBean);


        gainVerifiCodeService.callBack(new OnCallBackSuccessListener(){
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                //跳转到下个页面
                Bundle buns = new Bundle();
                buns.putInt(YpSettings.FROM_PAGE, frompage);
                buns.putString("mobile", mobile);
                buns.putInt(YpSettings.USERID, userID);
                ActivityUtil.jump(SettingPhoneActivity.this, SettingPhoneVificationCodeActivity.class, buns, 0, 100);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(SettingPhoneActivity.this);
                    return;
                }else{
                    DialogUtil.showDisCoverNetToast(SettingPhoneActivity.this,msg);
                }
            }
        });
        gainVerifiCodeService.enqueue();

    }


}
