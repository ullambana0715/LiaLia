package cn.chono.yopper.activity.usercenter;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.ParseException;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.GainPFruit.AvailableEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitBean;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitRespEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitService;
import cn.chono.yopper.Service.Http.GainPFruit.VipPostionEntity;
import cn.chono.yopper.Service.Http.GainPFruit.VipPostionListEntity;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.find.ExchangeBonusActivity;
import cn.chono.yopper.activity.find.FindWebActivity;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.activity.order.BuyKeyActivity;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.ui.AttractInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.TimeUtils;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 我的账户界面
 *
 * @author sam.sun
 */
public class UserAccountActivity extends MainFrameActivity implements OnClickListener {


    private TextView account_apple_num_tv;

    private TextView account_key_num_tv;

    private TextView atrrack_key_num_tv;

    private TextView account_get_apple_tv;
    private TextView account_get_key_tv;
    private TextView atrrack_get_key_tv;

    private int apple_count = 0;

    private int key_count = 0;

    private int attrack_count = 0;

    int keyPrice = 0;

    ViewStub account_member_vs;

    ViewStub account_no_member_vs;

    int userPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        setContentLayout(R.layout.act_user_account);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            apple_count = bundle.getInt("apple_count");

            key_count = bundle.getInt("key_count");

            attrack_count = bundle.getInt("attrack_count");
        }

        initComponent();


    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的账户"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        getAccountInfo();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的账户"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("我的账户");
        this.getBtnGoBack().setVisibility(View.VISIBLE);

        this.gettvOption().setText("兑换");
        this.getBtnOption().setVisibility(View.GONE);
        this.gettvOption().setVisibility(View.VISIBLE);
        this.gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));
        this.getOptionLayout().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.jumpForResult(UserAccountActivity.this, ExchangeBonusActivity.class, null, 6600, 0, 100);
            }
        });


        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });


        account_apple_num_tv = (TextView) findViewById(R.id.account_apple_num_tv);

        account_get_apple_tv = (TextView) findViewById(R.id.account_get_apple_tv);
        account_get_apple_tv.setOnClickListener(this);

        account_key_num_tv = (TextView) findViewById(R.id.account_key_num_tv);

        account_get_key_tv = (TextView) findViewById(R.id.account_get_key_tv);
        account_get_key_tv.setOnClickListener(this);

        account_apple_num_tv.setText(String.valueOf(apple_count));

        account_key_num_tv.setText(String.valueOf(key_count));

        account_member_vs = (ViewStub) findViewById(R.id.account_member_vs);
        account_no_member_vs = (ViewStub) findViewById(R.id.account_no_member_vs);

        atrrack_key_num_tv = (TextView) findViewById(R.id.atrrack_key_num_tv);

        atrrack_get_key_tv = (TextView) findViewById(R.id.atrrack_get_key_tv);
        atrrack_get_key_tv.setOnClickListener(this);
    }


    private void setMemberData(final VipPostionEntity vipPostionEntity) {


        account_member_vs.setVisibility(View.VISIBLE);


        TextView account_tv_member_status = (TextView) findViewById(R.id.account_member_tv_status);


        TextView account_member_tv_data_title = (TextView) findViewById(R.id.account_member_tv_data_title);

        TextView account_member_tv_data = (TextView) findViewById(R.id.account_member_tv_data);

        ImageView account_member_iv_icon = (ImageView) findViewById(R.id.account_member_iv_icon);

        ImageView account_member_iv_bg = (ImageView) findViewById(R.id.account_member_iv_bg);

        LinearLayout account_member_ll_competence = (LinearLayout) findViewById(R.id.account_member_ll_competence);

        TextView account_member_tv_but = (TextView) findViewById(R.id.account_member_tv_but);


        account_member_tv_but.setText(this.getResources().getString(R.string.renewals));

        account_member_tv_but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putInt("apple_count", apple_count);

                bundle.putInt("userPosition", vipPostionEntity.lastUserVipPosition);

                ActivityUtil.jump(UserAccountActivity.this, VipRenewalsActivity.class, bundle, 0, 100);
            }
        });


        account_tv_member_status.setText(vipPostionEntity.title == null ? "" : vipPostionEntity.title);


        account_member_tv_data_title.setText(this.getResources().getString(R.string.validity_period));


        long tiem = TimeUtils.getFormat(vipPostionEntity.validDate);


        String date = TimeUtils.longToString(tiem, "yyyy-MM-dd");

        account_member_tv_data.setText(date);


        if (1 == vipPostionEntity.lastUserVipPosition) {

            Glide.with(this).load(R.drawable.vip_silver_icon).diskCacheStrategy(DiskCacheStrategy.NONE).into(account_member_iv_icon);


        } else if (2 == vipPostionEntity.lastUserVipPosition) {

            Glide.with(this).load(R.drawable.vip_gole_icon).diskCacheStrategy(DiskCacheStrategy.NONE).into(account_member_iv_icon);


        } else if (3 == vipPostionEntity.lastUserVipPosition) {

            Glide.with(this).load(R.drawable.vip_platinum_icon).diskCacheStrategy(DiskCacheStrategy.NONE).into(account_member_iv_icon);


        } else if (4 == vipPostionEntity.lastUserVipPosition) {

            Glide.with(this).load(R.drawable.vip_diamond_icon).diskCacheStrategy(DiskCacheStrategy.NONE).into(account_member_iv_icon);


        }


        if (vipPostionEntity.vipRightList != null && vipPostionEntity.vipRightList.size() > 0) {

            account_member_ll_competence.removeAllViews();
            for (int i = 0; i < vipPostionEntity.vipRightList.size(); i++) {

                VipPostionListEntity vipPostionListEntity = vipPostionEntity.vipRightList.get(i);

                if (vipPostionEntity == null) {
                    return;
                }


                if (TextUtils.isEmpty(vipPostionListEntity.description)) {
                    return;
                }

                View view = LayoutInflater.from(this).inflate(R.layout.account_member_competence_content, null);

                TextView textView = (TextView) view.findViewById(R.id.account_member_competence_tv);

                TextView account_member_competence_but = (TextView) view.findViewById(R.id.account_member_competence_but);

                textView.setText(vipPostionListEntity.description);

                if (vipPostionListEntity.isNeedLookStore) {
                    account_member_competence_but.setVisibility(View.VISIBLE);
                } else {
                    account_member_competence_but.setVisibility(View.GONE);
                }


                account_member_competence_but.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();

                        bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_NEED_HOST, true);

                        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "about/TarotShop");

                        ActivityUtil.jump(UserAccountActivity.this, FindWebActivity.class, bundle, 0, 100);
                    }
                });

                account_member_ll_competence.addView(view);


            }


        }


    }

    private void setNoMemberData(final VipPostionEntity vipPostionEntity) {

        account_no_member_vs.setVisibility(View.VISIBLE);


        ImageView account_no_member_iv_icon = (ImageView) findViewById(R.id.account_no_member_iv_icon);

        ImageView account_no_member_iv_bg = (ImageView) findViewById(R.id.account_no_member_iv_bg);

        TextView account_no_member_tv_vip_title = (TextView) findViewById(R.id.account_no_member_tv_vip_title);

        TextView account_no_member_tv_vip = (TextView) findViewById(R.id.account_no_member_tv_vip);

        TextView account_no_member_tv_but = (TextView) findViewById(R.id.account_no_member_tv_but);


        account_no_member_tv_but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putInt("apple_count", apple_count);

                bundle.putInt("userPosition", vipPostionEntity.lastUserVipPosition);

                if (0 == vipPostionEntity.lastUserVipPosition) {
                    ActivityUtil.jump(UserAccountActivity.this, VipOpenedActivity.class, bundle, 0, 100);
                    return;
                }

                ActivityUtil.jump(UserAccountActivity.this, VipRenewalsActivity.class, bundle, 0, 100);

            }
        });


        Glide.with(this).load(R.drawable.member_ordinary_icon).diskCacheStrategy(DiskCacheStrategy.NONE).into(account_no_member_iv_icon);


        if (0 == vipPostionEntity.lastUserVipPosition) {


            account_no_member_tv_vip_title.setText(getResources().getString(R.string.no_vip));

            account_no_member_tv_vip.setText(getResources().getString(R.string.opened_vip_privilege));

            account_no_member_tv_but.setText(getResources().getString(R.string.view_privilege));

            return;

        }


        account_no_member_tv_vip_title.setText(vipPostionEntity.title == null ? "" : vipPostionEntity.title);

        long tiem = TimeUtils.getFormat(vipPostionEntity.validDate);


        String date = TimeUtils.longToString(tiem, "yyyy-MM-dd");


        account_no_member_tv_vip.setText(this.getResources().getString(R.string.validity_period) + " " + date);


        account_no_member_tv_but.setText(this.getResources().getString(R.string.renewals));


    }

    private String getUserPosition(int userPosition) {


        if (1 == userPosition) {

            return this.getResources().getString(R.string.silver_vip);

        } else if (2 == userPosition) {

            return this.getResources().getString(R.string.gole_vip);

        } else if (3 == userPosition) {

            return this.getResources().getString(R.string.platinum_vip);

        } else if (4 == userPosition) {

            return this.getResources().getString(R.string.diamond_vip);

        }


        return "";


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

                apple_count = availableBalance;
                key_count = available.keyCount;
                keyPrice = available.keyPrice;
                attrack_count = available.remainCharm;

                account_apple_num_tv.setText(availableBalance + "");
                account_key_num_tv.setText(available.keyCount + "");

                atrrack_key_num_tv.setText(available.remainCharm + "");
                //vip

                VipPostionEntity vipPostionEntity = available.vipPostion;

                if (vipPostionEntity == null) {


                    return;


                }

                userPosition = vipPostionEntity.lastUserVipPosition;

                // V2.5.4 vip身份 ，0 表示普通用户 1表示 白银VIP，2表示 黄金VIP，3表示 铂金VIP，4表示 钻石VIP

                if (0 == vipPostionEntity.lastUserVipPosition) {

                    setNoMemberData(vipPostionEntity);
                    return;

                }

                //Vip 过期

                if (3 == vipPostionEntity.validStatus) {

                    setNoMemberData(vipPostionEntity);

                    return;

                }

                //Vip 不过期

                setMemberData(vipPostionEntity);


            }
        }, new OnCallBackFailListener());

        pFruitService.enqueue();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.account_get_apple_tv:

                Bundle appleBundle = new Bundle();

                appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);


                ActivityUtil.jump(UserAccountActivity.this, AppleListActivity.class, appleBundle, 0, 100);

                break;

            case R.id.account_get_key_tv:
                Bundle bundle = new Bundle();
                bundle.putInt("apple_count", apple_count);
                bundle.putInt("key_count", key_count);
                bundle.putInt("keyPrice", keyPrice);
                ActivityUtil.jump(UserAccountActivity.this, BuyKeyActivity.class, bundle, 0, 100);
                break;
            case R.id.atrrack_get_key_tv:
                Bundle bd = new Bundle();
                bd.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
                ActivityUtil.jump(UserAccountActivity.this, AttractInfoActivity.class, bd, 0, 100);

                break;
        }
    }


}
