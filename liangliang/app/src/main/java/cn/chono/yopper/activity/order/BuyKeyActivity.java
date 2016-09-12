package cn.chono.yopper.activity.order;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.GainPFruit.AvailableEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitBean;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitRespEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserKey.UserKeyBean;
import cn.chono.yopper.Service.Http.UserKey.UserKeyEntity;
import cn.chono.yopper.Service.Http.UserKey.UserKeyRespEntity;
import cn.chono.yopper.Service.Http.UserKey.UserKeyService;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 购买钥匙界面
 *
 * @author sam.sun
 */
public class BuyKeyActivity extends MainFrameActivity implements OnClickListener {

    private TextView buy_key_left_apple_num_tv;

    private TextView buy_key_left_key_num_tv;
    private TextView buy_key_price_tv;

    private EditText buy_key_key_num_et;

    private TextView buy_key_need_apple_num_tv;

    private TextView buy_key_get_apple_tv;
    private TextView buy_key_sure_tv;

    private int left_apple_count = 0;

    private int left_key_count = 0;

    private int buy_key_num = 5;

    int keyPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        setContentLayout(R.layout.act_order_buy_key);
        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("apple_count"))
            left_apple_count = bundle.getInt("apple_count");
        if (bundle.containsKey("key_count"))
            left_key_count = bundle.getInt("key_count");
        if (bundle.containsKey("keyPrice"))
            keyPrice = bundle.getInt("keyPrice");

        initComponent();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("购买钥匙"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        getAccountInfo();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("购买钥匙"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("购买钥匙");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });


        buy_key_left_apple_num_tv = (TextView) findViewById(R.id.buy_key_left_apple_num_tv);

        buy_key_left_key_num_tv = (TextView) findViewById(R.id.buy_key_left_key_num_tv);

        buy_key_price_tv = (TextView) findViewById(R.id.buy_key_price_tv);


        buy_key_key_num_et = (EditText) findViewById(R.id.buy_key_key_num_et);

        buy_key_sure_tv = (TextView) findViewById(R.id.buy_key_sure_tv);
        buy_key_sure_tv.setOnClickListener(this);

        buy_key_need_apple_num_tv = (TextView) findViewById(R.id.buy_key_need_apple_num_tv);

        buy_key_get_apple_tv = (TextView) findViewById(R.id.buy_key_get_apple_tv);
        buy_key_get_apple_tv.setOnClickListener(this);

        buy_key_left_apple_num_tv.setText(left_apple_count + "");
        buy_key_left_key_num_tv.setText(left_key_count + "");

        buy_key_need_apple_num_tv.setText("所需苹果：" + buy_key_num * keyPrice);

        buy_key_price_tv.setText(keyPrice + "");

        buy_key_key_num_et.setSelection(buy_key_key_num_et.getText().toString().trim().length());
        buy_key_key_num_et.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s)) {

                    buy_key_num = Integer.valueOf(s.toString().trim());

                    // 先判断输入的第一位不能是小数点
                    if (before == 0 && s.charAt(start) == '0') {
                        buy_key_key_num_et.setText(buy_key_num + "");
                    } else {

                        if (buy_key_num < 1) {
                            DialogUtil.showDisCoverNetToast(BuyKeyActivity.this, "请输入1-99之间的数值");
                            buy_key_key_num_et.setText(1 + "");
                        }

                        if (buy_key_num > 99) {
                            DialogUtil.showDisCoverNetToast(BuyKeyActivity.this, "请输入1-99之间的数值");
                            buy_key_key_num_et.setText(99 + "");
                        }
                    }
                } else {
                    buy_key_num = 0;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                buy_key_need_apple_num_tv.setText("所需苹果：" + buy_key_num * keyPrice);

            }
        });
    }

    //获取苹果与keys数量
    private void getAccountInfo() {

        GainPFruitBean fruitBean = new GainPFruitBean();
        fruitBean.setUserId(LoginUser.getInstance().getUserId());


        GainPFruitService pFruitService = new GainPFruitService(this);

        pFruitService.parameter(fruitBean);
        pFruitService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                GainPFruitRespEntity fruitRespBean = (GainPFruitRespEntity) respBean;
                AvailableEntity available = fruitRespBean.resp;
                int availableBalance = available.availableBalance;

                left_apple_count = availableBalance;
                left_key_count = available.keyCount;
                keyPrice = available.keyPrice;
                buy_key_left_apple_num_tv.setText(left_apple_count + "");
                buy_key_left_key_num_tv.setText(left_key_count + "");

            }
        }, new OnCallBackFailListener());

        pFruitService.enqueue();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.buy_key_get_apple_tv://获取苹果

                ViewsUtils.preventViewMultipleClick(v, 1000);

                Bundle appleBundle = new Bundle();

                appleBundle.putInt(YpSettings.ORDER_TYPE, Constant.ProductType_Apple);

                ActivityUtil.jump(BuyKeyActivity.this, AppleListActivity.class, appleBundle, 0, 100);

                break;

            case R.id.buy_key_sure_tv://确认购买钥匙

                ViewsUtils.preventViewMultipleClick(v, 1000);

                if (0 == buy_key_num) {

                    DialogUtil.showDisCoverNetToast(BuyKeyActivity.this, "数量不能为空");
                    return;

                }

                if (buy_key_num * keyPrice > left_apple_count) {

                    DialogUtil.showDisCoverNetToast(BuyKeyActivity.this, "苹果数量不足，请先获取苹果");
                    return;
                }


                buyUserKey(buy_key_num);
                break;
        }
    }

    public void buyUserKey(int keyCount) {


        UserKeyBean userKeyBean = new UserKeyBean();

        userKeyBean.keyCount = keyCount;

        UserKeyService userKeyService = new UserKeyService(this);

        userKeyService.parameter(userKeyBean);

        userKeyService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UserKeyRespEntity userKeyRespEntity = (UserKeyRespEntity) respBean;

                UserKeyEntity userKeyEntity = userKeyRespEntity.resp;

                if (userKeyEntity == null) {
                    return;
                }

                if (userKeyEntity.isSuccess) {

                    DialogUtil.showDisCoverNetToast(BuyKeyActivity.this, "钥匙购买成功");

                    finish();

                    return;
                }

                if (TextUtils.isEmpty(userKeyEntity.errorMsg)) {
                    DialogUtil.showDisCoverNetToast(BuyKeyActivity.this);
                    return;
                }

                DialogUtil.showDisCoverNetToast(BuyKeyActivity.this, userKeyEntity.errorMsg);

            }
        });
        userKeyService.enqueue();
    }
}
