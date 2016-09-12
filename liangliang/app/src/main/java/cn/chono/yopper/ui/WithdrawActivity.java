package cn.chono.yopper.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.chono.yopper.R;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.presenter.WithDrawContract;
import cn.chono.yopper.presenter.WithDrawPresenter;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;

/**
 * Created by sunquan on 16/8/4.
 */
public class WithdrawActivity extends BaseActivity<WithDrawPresenter> implements WithDrawContract.View {

    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;

    @BindView(R.id.base_option_iv)
    ImageView baseOptionIv;

    @BindView(R.id.base_option_tv)
    TextView baseOptionTv;

    @BindView(R.id.withdraw_atrrack_num_tv)
    TextView withdrawAtrrackNumTv;

    @BindView(R.id.withdraw_can_change_account_tv)
    TextView withdrawCanChangeAccountTv;

    @BindView(R.id.withdraw_input_change_account_tv)
    EditText withdrawInputChangeAccountTv;

    @BindView(R.id.withdraw_input_useraccount)
    EditText withdrawInputUseraccount;

    @BindView(R.id.withdraw_input_username)
    EditText withdrawInputUsername;

    @BindView(R.id.base_option_layout)
    LinearLayout baseOptionLayout;

    Dialog dialog;

    private int account;

    private int charmNum;

    private int userId;

    private static final int DECIMAL_DIGITS = 1;

    @Override
    protected int getLayout() {
        return R.layout.act_withdraw;
    }

    @Override
    protected WithDrawPresenter getPresenter() {
        return new WithDrawPresenter(mContext, this);
    }

    @Override
    protected void initVariables() {

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {

            account = bundle.getInt(YpSettings.WithDraw_Account);

            charmNum = bundle.getInt(YpSettings.Charm_Num);

            userId = bundle.getInt(YpSettings.USERID);
        }
    }

    @Override
    protected void initView() {

        baseTitleTv.setText("魅力值提现");

        baseOptionLayout.setVisibility(View.VISIBLE);

        baseOptionIv.setVisibility(View.GONE);

        baseOptionTv.setVisibility(View.VISIBLE);

        baseOptionTv.setText("记录");

        baseOptionTv.setTextColor(getResources().getColor(R.color.color_ff7462));

        updateCharmValue(charmNum);

        updateWithDrawAccount(account);

        InputFilter[] filters = {new EditInputFilter()};

        withdrawInputChangeAccountTv.setFilters(filters);

        withdrawInputUseraccount.setOnFocusChangeListener((v1, hasFocus1) -> {
            if (hasFocus1) {
                chekInput();
            }
        });

        withdrawInputUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (TextUtils.isEmpty(withdrawInputChangeAccountTv.getText().toString())) {

                    isCanInput(0);

                } else {

                    Logger.e("Edittext = " + withdrawInputChangeAccountTv.getText().toString());
                    double db = Double.parseDouble(withdrawInputChangeAccountTv.getText().toString().replace("元", ""));


                    Logger.e("Edittext value= " + db);
                    int number = (int) db;

                    isCanInput(number);
                }
            }
        });

    }

    private void chekInput() {
        if (TextUtils.isEmpty(withdrawInputChangeAccountTv.getText().toString())) {

            isCanInput(0);

        } else {

            double db = Double.parseDouble(withdrawInputChangeAccountTv.getText().toString().replace("元", ""));


            int number = (int) db;

            isCanInput(number);
        }
    }

    @Override
    protected void initDataAndLoadData() {

    }


    /**
     * 设置小数位数控制
     */
    public class EditInputFilter implements InputFilter {

        Pattern p;

        public EditInputFilter() {
            p = Pattern.compile("[0-9]*");   //除数字外的其他的
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();

            //验证非数字或者小数点的情况
            Matcher m = p.matcher(source);
            if (dValue.contains(".")) {
                //已经存在小数点的情况下，只能输入数字
                if (!m.matches()) {
                    return null;
                }
            } else {
                //未输入小数点的情况下，可以输入小数点和数字
                if (!m.matches() && !source.equals(".")) {
                    return null;
                }
            }

            if (dValue.equals("0") || dValue.equals(".")) {

                showToastHint("金额少于10元无法提现");
                return null;
            }


            if (source.toString().equals("0") && dValue.equals("")) {

                showToastHint("金额少于10元无法提现");
                return dest.subSequence(dstart, dend);
            }


            if (source.toString().equals(".")) {

                if (!TextUtils.isEmpty(dValue)) {
                    double i = Double.parseDouble(dValue);
                    if (i < 10) {
                        showToastHint("金额少于10元无法提现");
                        return dest.subSequence(dstart, dend);
                    }
                } else {
                    return dest.subSequence(dstart, dend);
                }

            }

            //验证输入金额的大小
            if (!source.toString().equals("")) {
                double dold = Double.parseDouble(dValue + source.toString());
                if (dold > 2000) {
                    withDrawtMorethanHintDialog("已超出今日提现金额上限2000元，请减少金额");
                    return dest.subSequence(dstart, dend);
                }
            }

            //超过提现金额
            if (!source.toString().equals("")) {
                double dold = Double.parseDouble(dValue + source.toString());

                double money = (account / 100.00);

                if (dold > money) {

                    source = Double.toString(money);

                    withdrawInputChangeAccountTv.setText(source);
                    return "";
                }
            }

            //验证小数位精度是否正确
            if (dValue.contains(".")) {
                int index = dValue.indexOf(".");
                int len = dend - index;
                //小数位只能2位
                if (len > DECIMAL_DIGITS) {
                    CharSequence newText = dest.subSequence(dstart, dend);
                    return newText;
                }
            }

            withdrawInputUseraccount.setEnabled(true);

            withdrawInputUsername.setEnabled(true);

            return dest.subSequence(dstart, dend) + source.toString();
        }
    }


    @OnClick({R.id.base_back_layout, R.id.base_option_layout, R.id.withdraw_sure_btn, R.id.withdraw_input_change_account_tv, R.id.withdraw_input_useraccount, R.id.withdraw_input_username})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_back_layout:
                finish();
                break;
            case R.id.base_option_layout:
                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.USERID, userId);
                ActivityUtil.jump(WithdrawActivity.this, WithDrawRecordsActivity.class, bundle, 0, 100);
                break;
            case R.id.withdraw_sure_btn:

                withDrawLimit();

                break;

//            case R.id.withdraw_input_change_account_tv:
//
//                chekInput();
//
//                break;
//
//            case R.id.withdraw_input_useraccount:
//                chekInput();
//                break;
//
//            case R.id.withdraw_input_username:
//                chekInput();
//                break;

        }
    }

    @Override
    public void updateCharmValue(int charmValue) {

        withdrawAtrrackNumTv.setText(charmValue + "");
    }

    @Override
    public void updateWithDrawAccount(int accunt) {

        double f = accunt / 100.00;


        DecimalFormat df = new DecimalFormat("#.#");

        String st = df.format(f);


        withdrawCanChangeAccountTv.setText(st + "元");
    }

    @Override
    public void showToastHint(String msg) {

        DialogUtil.showDisCoverNetToast(this, msg);
    }

    @Override
    public void commitDialog(int cash, String aliPayAccount, String aliPayAccountName) {

        dialog = DialogUtil.createHintOperateDialog(this, "",
                "请确定您填写的信息是否正确，否则无法提现，信息正确，系统会在7个工作日内，将您的兑换金额提现到您的支付宝，请注意查收", "取消", "确定", new BackCallListener() {
                    @Override
                    public void onCancel(View view, Object... obj) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onEnsure(View view, Object... obj) {

                        dialog.dismiss();

                        mPresenter.commitWithDraw(userId, cash, aliPayAccount, aliPayAccountName);
                    }
                });
        dialog.show();

    }

    @Override
    public void withDrawLimit() {

        String coinStr = withdrawInputChangeAccountTv.getText().toString();

        String aliPayAccount = withdrawInputUseraccount.getText().toString();

        String aliPayAccountName = withdrawInputUsername.getText().toString();

        double coin;

        if (TextUtils.isEmpty(coinStr)) {

            showToastHint("请填写兑换金额");
            return;

        } else {
            coin = Double.parseDouble(coinStr);
            if (coin < 10) {
                showToastHint("金额少于10元无法提现");
                return;
            }

            if (coin > account) {
                showToastHint("提取金额大于兑换金额");
                return;
            }

            if (coin > 2000) {
                withDrawtMorethanHintDialog("已超出今日提现金额上限2000元，请减少金额");
                return;
            }

        }

        if (TextUtils.isEmpty(aliPayAccount)) {

            showToastHint("请填写支付宝账号");
            return;
        }

        if (TextUtils.isEmpty(aliPayAccountName)) {

            showToastHint("请填写姓名");
            return;
        }

        commitDialog((int) coin * 100, aliPayAccount, aliPayAccountName);
    }

    @Override
    public void withDrawtMorethanHintDialog(String msg) {

        dialog = DialogUtil.createHintOperateDialog(this, "",
                msg, "", "确定", new BackCallListener() {
                    @Override
                    public void onCancel(View view, Object... obj) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onEnsure(View view, Object... obj) {

                        dialog.dismiss();

                    }
                });
        dialog.show();

    }

    @Override
    public void withDrawSuccess() {

        showToastHint("提交成功");

        finish();
    }

    @Override
    public void isCanInput(int account) {

        Logger.e("输入的金额为："+account);
        if (account < 10) {

            showToastHint("金额少于10元无法提现");

            withdrawInputChangeAccountTv.setEnabled(true);

            withdrawInputUseraccount.setEnabled(false);

            withdrawInputUsername.setEnabled(false);

        } else {

            withdrawInputChangeAccountTv.setEnabled(true);

            withdrawInputUseraccount.setEnabled(true);

            withdrawInputUsername.setEnabled(true);

        }
    }

}
