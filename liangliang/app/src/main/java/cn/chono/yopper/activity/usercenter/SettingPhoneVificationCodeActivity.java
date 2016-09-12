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
import android.widget.Button;
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
import cn.chono.yopper.Service.Http.UserMobileVerifyCode.UserMobileVerifyCodeBean;
import cn.chono.yopper.Service.Http.UserMobileVerifyCode.UserMobileVerifyCodeService;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CountDownHelper;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 认证手机 更换手机 设置密码的验证码页面
 *
 * @author sam.sun
 */
public class SettingPhoneVificationCodeActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private int frompage;

    private TextView setting_verification_phone_tv;

    private EditText setting_verification_code_et;

    private ImageView setting_verification_code_empty_iv;


    private Button setting_verification_code_to_obtain_but;

    private String mobile,verifycode;

    private CountDownHelper countDownHelper;//倒计时

    private Dialog backDialog;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        Bundle bundle=this.getIntent().getExtras();
        frompage= bundle.getInt(YpSettings.FROM_PAGE);
        mobile=bundle.getString("mobile");
        if (bundle.containsKey(YpSettings.USERID))
            userID=bundle.getInt(YpSettings.USERID);

        LogUtils.e("手机号码＝"+mobile);
        initComponent();
        countDownHelper();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("验证码已发送"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("验证码已发送"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("验证码已发送");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.VISIBLE);
        this.getBtnOption().setVisibility(View.GONE);
        if(frompage!=3){
            this.gettvOption().setText("完成");
        }else{
            this.gettvOption().setText("下一步");
        }
        this.gettvOption().setTextColor(getResources().getColor(R.color.color_ffdbd7));

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);

                backDialog = DialogUtil.createHintOperateDialog(SettingPhoneVificationCodeActivity.this, "", "验证码短信可能略有延迟，确定返回并重新开始?", "等待", "返回", quitbackCallListener);
                if (!isFinishing()) {
                    backDialog.show();
                }

            }
        });

        this.getOptionLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                verifycode=setting_verification_code_et.getText().toString().trim();

                if (!TextUtils.isEmpty(verifycode) && verifycode.length()>=6) {
                    enqueueVerifiCationService(mobile, verifycode);
                }
            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contextView = mInflater.inflate(R.layout.setting_verification_code_activity, null);

        setting_verification_phone_tv = (TextView) contextView.findViewById(R.id.setting_verification_phone_tv);

        setting_verification_code_et = (EditText) contextView.findViewById(R.id.setting_verification_code_et);

        setting_verification_code_empty_iv= (ImageView) contextView.findViewById(R.id.setting_verification_code_empty_iv);

        setting_verification_code_to_obtain_but= (Button) contextView.findViewById(R.id.setting_verification_code_to_obtain_but);


        setting_verification_phone_tv.setText(mobile);

        setting_verification_code_to_obtain_but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownHelper();
                hideSoftInputView();
                enqueueGainVerifinCodeService();
            }
        });

        setting_verification_code_empty_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_verification_code_et.setText("");
                setting_verification_code_empty_iv.setVisibility(View.GONE);
            }
        });

        setting_verification_code_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                verifycode = setting_verification_code_et.getText().toString().trim();

                if (!TextUtils.isEmpty(verifycode) && verifycode.length()>=6) {

                    setting_verification_code_empty_iv.setVisibility(View.VISIBLE);
                    SettingPhoneVificationCodeActivity.this.gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));
                } else {

                    setting_verification_code_empty_iv.setVisibility(View.GONE);
                    SettingPhoneVificationCodeActivity.this.gettvOption().setTextColor(getResources().getColor(R.color.color_ffdbd7));
                }

            }
        });


        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }




    private BackCallListener quitbackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {


            if (!isFinishing()) {
                backDialog.dismiss();
            }
            finish();
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                backDialog.dismiss();
            }

        }
    };


    /**
     * 倒计时
     */
    public void countDownHelper() {
        if (null == countDownHelper) {
            countDownHelper = new CountDownHelper(SettingPhoneVificationCodeActivity.this, setting_verification_code_to_obtain_but, getString(R.string.resend_again_verification_code), 60, 1);
        }
        countDownHelper.start();
        // 设置倒计时监听
        countDownHelper.setOnFinishListener(new CountDownHelper.OnFinishListener() {

            @Override
            public void finish() {


            }
        });
    }


    /**
     * 获取验证码
     */
    public void enqueueGainVerifinCodeService() {
        GainVerifiCodeBean gainVerifiCodeBean = new GainVerifiCodeBean();
        gainVerifiCodeBean.setMobile(mobile);
        GainVerifiCodeService gainVerifiCodeService = new GainVerifiCodeService(SettingPhoneVificationCodeActivity.this);
        gainVerifiCodeService.parameter(gainVerifiCodeBean);


        gainVerifiCodeService.callBack(new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(SettingPhoneVificationCodeActivity.this);
                } else {
                    DialogUtil.showDisCoverNetToast(SettingPhoneVificationCodeActivity.this, msg);
                }
            }
        });
        gainVerifiCodeService.enqueue();

    }

    private Dialog loadingDiaog;
    /**
     * 验证 手机验证码
     */
    public void enqueueVerifiCationService(final String mobile, final String verifyCode) {



        loadingDiaog = DialogUtil.LoadingDialog(SettingPhoneVificationCodeActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }


        UserMobileVerifyCodeBean verifiCationBean = new UserMobileVerifyCodeBean();
        verifiCationBean.setMobile(mobile);
        verifiCationBean.setVerifyCode(verifyCode);

        UserMobileVerifyCodeService verifiCationService = new UserMobileVerifyCodeService(SettingPhoneVificationCodeActivity.this);
        verifiCationService.parameter(verifiCationBean);
        verifiCationService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                if(frompage!=3){

                    Bundle buns = new Bundle();

                    buns.putInt(YpSettings.USERID, userID);

                    ActivityUtil.jump(SettingPhoneVificationCodeActivity.this, UserInfoActivity.class, buns, 1, 0);
                }else{

                    Bundle buns = new Bundle();
                    buns.putInt(YpSettings.FROM_PAGE, 0);
                    ActivityUtil.jump(SettingPhoneVificationCodeActivity.this, SettingPasswordActivity.class, buns, 0, 100);
                }

            }
        });

        verifiCationService.callBack(new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(SettingPhoneVificationCodeActivity.this);
                } else {
                    DialogUtil.showDisCoverNetToast(SettingPhoneVificationCodeActivity.this, msg);
                }

            }
        });
        verifiCationService.enqueue();
    }



}
