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

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.ResetPassWord.ResetPassWordBean;
import cn.chono.yopper.Service.Http.ResetPassWord.ResetPassWordRespBean;
import cn.chono.yopper.Service.Http.ResetPassWord.ResetPassWordService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SHA;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 设置密码
 *
 * @author sam.sun
 */
public class SettingPasswordActivity extends MainFrameActivity {


    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private EditText setting_password_et;
    private ImageView setting_password_delete_iv;

    private String password;

    private Dialog backDialog;

    //从验证码页面过来是0；从设置页面直接过来是1
    private int frompage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        frompage=this.getIntent().getExtras().getInt(YpSettings.FROM_PAGE);

        initComponent();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置密码"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("设置密码"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("设置密码");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.VISIBLE);
        this.getBtnOption().setVisibility(View.GONE);
        this.gettvOption().setText("完成");
        this.gettvOption().setTextColor(getResources().getColor(R.color.color_ffdbd7));

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                if(frompage==0){
                    backDialog = DialogUtil.createHintOperateDialog(SettingPasswordActivity.this, "", "设置登录密码后，可以使用手机号+密码登录俩俩，确认返回?", "继续", "确认", quitbackCallListener);
                    if (!isFinishing()) {
                        backDialog.show();
                    }

                }else{
                    finish();
                }

            }
        });


        this.getOptionLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



                ViewsUtils.preventViewMultipleClick(v, false);
                hideSoftInputView();
                password = setting_password_et.getText().toString().trim();

                if (password.length() < 6) {

                    DialogUtil.showDisCoverNetToast(SettingPasswordActivity.this, "密码不能少于6位");

                    ViewsUtils.preventViewMultipleClick(v, true);
                }  else {

                    password = SHA.encodeSHA1(password);

                    resetPassWordHttp(password, password);

                }

            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contextView = mInflater.inflate(R.layout.setting_password_activity, null);

        setting_password_et = (EditText) contextView.findViewById(R.id.setting_password_et);

        setting_password_delete_iv= (ImageView) contextView.findViewById(R.id.setting_password_delete_iv);

        setting_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                password = setting_password_et.getText().toString().trim();


                if (!TextUtils.isEmpty(password)) {

                    setting_password_delete_iv.setVisibility(View.VISIBLE);
                    SettingPasswordActivity.this.gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));
                } else {

                    setting_password_delete_iv.setVisibility(View.GONE);
                    SettingPasswordActivity.this.gettvOption().setTextColor(getResources().getColor(R.color.color_ffdbd7));
                }

            }
        });


        setting_password_delete_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_password_delete_iv.setVisibility(View.GONE);
                setting_password_et.setText("");

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
            ActivityUtil.jump(SettingPasswordActivity.this, SettingActivity.class, null, 2, 0);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                backDialog.dismiss();
            }

        }
    };

    private Dialog loadingDiaog;

    private void resetPassWordHttp(String hashedNewPassword,String confirmHashedPassword) {

        loadingDiaog = DialogUtil.LoadingDialog(SettingPasswordActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        ResetPassWordBean resetPassWordBean = new ResetPassWordBean();
        resetPassWordBean.setConfirmHashedPassword(confirmHashedPassword);
        resetPassWordBean.setHashedNewPassword(hashedNewPassword);

        ResetPassWordService resetPassWordService = new ResetPassWordService(this);
        resetPassWordService.parameter(resetPassWordBean);

        resetPassWordService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();
                ResetPassWordRespBean resetPassWordRespBean = (ResetPassWordRespBean) respBean;

                boolean us = resetPassWordRespBean.isResp();
                if (us) {

                    DialogUtil.showDisCoverNetToast(SettingPasswordActivity.this, "设置成功");
                    ActivityUtil.jump(SettingPasswordActivity.this, SettingActivity.class, null, 1, 0);

                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(SettingPasswordActivity.this);
                } else {
                    DialogUtil.showDisCoverNetToast(SettingPasswordActivity.this, msg);
                }


            }
        });

        resetPassWordService.enqueue();
    }

}
