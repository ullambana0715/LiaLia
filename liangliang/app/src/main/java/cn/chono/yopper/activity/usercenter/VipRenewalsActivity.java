package cn.chono.yopper.activity.usercenter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.GainPFruit.AvailableEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitBean;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitRespEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitService;
import cn.chono.yopper.Service.Http.GainPFruit.VipPostionEntity;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.VipConfigs.VipConfigsEntity;
import cn.chono.yopper.Service.Http.VipConfigs.VipConfigsListEntity;
import cn.chono.yopper.Service.Http.VipConfigs.VipConfigsRespEntity;
import cn.chono.yopper.Service.Http.VipConfigs.VipConfigsService;
import cn.chono.yopper.Service.Http.VipPay.VipPayBean;
import cn.chono.yopper.Service.Http.VipPay.VipPayEntity;
import cn.chono.yopper.Service.Http.VipPay.VipPayRespEntity;
import cn.chono.yopper.Service.Http.VipPay.VipPayService;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.adapter.VipRenewalsAdapter;
import cn.chono.yopper.data.ApplesEntity;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.VipRenewalsPrivilegeEntity;
import cn.chono.yopper.event.NoSatisfyApple;
import cn.chono.yopper.event.VideoLookLimitChangeEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DialogUtil;

/**
 * Created by cc on 16/6/14.
 */
public class VipRenewalsActivity extends MainFrameActivity {

    RecyclerView vipRenewals_rv;

    VipRenewalsAdapter mVipRenewalsAdapter;


    Dialog payApple_dlg, noSatisfyApple_dlg;

    Integer apple_count;

    int userPosition;

    int userId;

    VipConfigsListEntity apple = new VipConfigsListEntity();

    List<VipConfigsListEntity> list = new ArrayList<>();

    private String frompage;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccountInfo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_vip_renewals);

        RxBus.get().register(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("apple_count"))
                apple_count = bundle.getInt("apple_count");

            if (bundle.containsKey("userPosition"))
                userPosition = bundle.getInt("userPosition");

            if (bundle.containsKey(YpSettings.FROM_PAGE))
                frompage = bundle.getString(YpSettings.FROM_PAGE);

        }

        userId = LoginUser.getInstance().getUserId();


        initView();


    }

    private void initView() {

        getBtnGoBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        getTvTitle().setText(getResources().getString(R.string.vip_renewals));

        vipRenewals_rv = (RecyclerView) findViewById(R.id.vipRenewals_rv);

        vipRenewals_rv.setLayoutManager(new LinearLayoutManager(this));

        mVipRenewalsAdapter = new VipRenewalsAdapter(this);

        vipRenewals_rv.setAdapter(mVipRenewalsAdapter);


    }


    private void getListData() {

        List<VipConfigsListEntity> entities = new ArrayList<>();

        entities.add(apple);


        entities.addAll(list);


        VipConfigsListEntity title = new VipConfigsListEntity();
        title.mVIPTITLE = this.getResources().getString(R.string.vip_renewals_become_effective);
        entities.add(title);


        VipConfigsListEntity heat_restrictions = new VipConfigsListEntity();

        heat_restrictions.mVipRenewalsPrivilegeEntity = new VipRenewalsPrivilegeEntity();
        heat_restrictions.mVipRenewalsPrivilegeEntity.title = "热度限制";
        heat_restrictions.mVipRenewalsPrivilegeEntity.imageId = R.drawable.vip_heat_restrictions;
        entities.add(heat_restrictions);


        VipConfigsListEntity login_keychain = new VipConfigsListEntity();
        login_keychain.mVipRenewalsPrivilegeEntity = new VipRenewalsPrivilegeEntity();
        login_keychain.mVipRenewalsPrivilegeEntity.title = "登录送钥匙";
        login_keychain.mVipRenewalsPrivilegeEntity.imageId = R.drawable.vip_login_keychain;
        entities.add(login_keychain);

        VipConfigsListEntity view_video = new VipConfigsListEntity();
        view_video.mVipRenewalsPrivilegeEntity = new VipRenewalsPrivilegeEntity();
        view_video.mVipRenewalsPrivilegeEntity.title = "查看视频";
        view_video.mVipRenewalsPrivilegeEntity.imageId = R.drawable.vip_view_video;
        entities.add(view_video);


        VipConfigsListEntity dating_events = new VipConfigsListEntity();
        dating_events.mVipRenewalsPrivilegeEntity = new VipRenewalsPrivilegeEntity();
        dating_events.mVipRenewalsPrivilegeEntity.title = "交友活动";
        dating_events.mVipRenewalsPrivilegeEntity.imageId = R.drawable.vip_dating_events;
        entities.add(dating_events);

        VipConfigsListEntity divination_deals = new VipConfigsListEntity();
        divination_deals.mVipRenewalsPrivilegeEntity = new VipRenewalsPrivilegeEntity();
        divination_deals.mVipRenewalsPrivilegeEntity.title = "占卜优惠";
        divination_deals.mVipRenewalsPrivilegeEntity.imageId = R.drawable.vip_divination_deals;
        entities.add(divination_deals);

        mVipRenewalsAdapter.setData(entities);


    }


    private void getVipConfigs() {


        VipConfigsService vipConfigsService = new VipConfigsService(this);

        vipConfigsService.callBack(new OnCallBackSuccessListener() {

            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);


                VipConfigsRespEntity vipConfigsRespEntity = (VipConfigsRespEntity) respBean;

                if (vipConfigsRespEntity == null) {

                    getListData();

                    return;
                }

                VipConfigsEntity vipConfigsEntity = vipConfigsRespEntity.resp;

                if (vipConfigsEntity == null) {

                    getListData();

                    return;
                }


                if (vipConfigsEntity.list == null || vipConfigsEntity.list.size() == 0) {

                    getListData();

                    return;
                }

                list = vipConfigsEntity.list;


                getListData();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                getListData();


            }


        });
        vipConfigsService.enqueue();

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


                //vip

                VipPostionEntity vipPostionEntity = available.vipPostion;


                userPosition = vipPostionEntity.lastUserVipPosition;


                apple.mApplesEntity = new ApplesEntity();

                apple.mApplesEntity.apple_count = apple_count;

                apple.mApplesEntity.userPosition = userPosition;

                getVipConfigs();


            }
        }, new OnCallBackFailListener() {

            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                apple.mApplesEntity = new ApplesEntity();
                apple.mApplesEntity.apple_count = 0;

                apple.mApplesEntity.userPosition = 0;


                getVipConfigs();

            }


        });

        pFruitService.enqueue();
    }


    private void payVip(int userId, int userPosition) {


        VipPayBean vipPayBean = new VipPayBean();

        vipPayBean.userId = userId;

        vipPayBean.userPosition = userPosition;

        VipPayService vipPayService = new VipPayService(this);

        vipPayService.parameter(vipPayBean);

        vipPayService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);


                VipPayRespEntity vipPayRespEntity = (VipPayRespEntity) respBean;

                VipPayEntity vipPayEntity = vipPayRespEntity.resp;


                if (0 == vipPayEntity.buyVip) {

                    DialogUtil.showDisCoverNetToast(VipRenewalsActivity.this, "购买成功");

                    if (!CheckUtil.isEmpty(frompage) && frompage.equals("OthersVideoDetailActivity")) {

                        RxBus.get().post("VideoLookLimitChangeEvent", new VideoLookLimitChangeEvent());
                    }

                    finish();

                    return;
                }


                RxBus.get().post("renewals_no_satisfy_apple", new NoSatisfyApple(vipPayEntity.message));


            }
        });

        vipPayService.enqueue();


    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("renewals_more_apple")


            }

    )
    public void moreApple(Object o) {

        Bundle appleBundle = new Bundle();

        appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);


        ActivityUtil.jump(VipRenewalsActivity.this, AppleListActivity.class, appleBundle, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("renewals_pay_apple")

            }

    )
    public void payApple(final VipConfigsListEntity vipConfigsListEntity) {


        StringBuffer sb = new StringBuffer();

        sb.append("消耗");

        sb.append(vipConfigsListEntity.appleCount);

        sb.append("苹果购买");

        String vip = "";

        if (0 == vipConfigsListEntity.userPosition)
            vip = getResources().getString(R.string.no_vip_user);
        if (1 == vipConfigsListEntity.userPosition)
            vip = getResources().getString(R.string.vip_silver);
        if (2 == vipConfigsListEntity.userPosition)
            vip = getResources().getString(R.string.vip_gole);
        if (3 == vipConfigsListEntity.userPosition)
            vip = getResources().getString(R.string.vip_platinum);
        if (4 == vipConfigsListEntity.userPosition)
            vip = getResources().getString(R.string.vip_diamond);


        sb.append(vip);


        payApple_dlg = DialogUtil.createHintOperateDialog(VipRenewalsActivity.this, "", sb.toString(), "取消", "确认购买", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                payApple_dlg.dismiss();


            }

            @Override
            public void onEnsure(View view, Object... obj) {

                payApple_dlg.dismiss();

                if (apple_count != null && apple_count >= vipConfigsListEntity.appleCount) {


                    payVip(userId, vipConfigsListEntity.userPosition);
                    return;
                }

                RxBus.get().post("renewals_no_satisfy_apple", new NoSatisfyApple());


            }
        });

        payApple_dlg.show();


    }


    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("renewals_no_satisfy_apple")


            }

    )

    public void noSatisfyApple(NoSatisfyApple satisfyApple) {

        String massge = "苹果数量不足，请先购买苹果";


        if (!TextUtils.isEmpty(satisfyApple.getMessage())) {
            massge = satisfyApple.getMessage();
        }


        noSatisfyApple_dlg = DialogUtil.createHintOperateDialog(VipRenewalsActivity.this, "", massge, "取消", "购买苹果", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                noSatisfyApple_dlg.dismiss();

            }

            @Override
            public void onEnsure(View view, Object... obj) {

                noSatisfyApple_dlg.dismiss();

                Bundle appleBundle = new Bundle();

                appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);


                ActivityUtil.jump(VipRenewalsActivity.this, AppleListActivity.class, appleBundle, 0, 100);


            }
        });

        noSatisfyApple_dlg.show();

    }

}
