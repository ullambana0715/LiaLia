package cn.chono.yopper.ui.gift;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.tencent.TIMConversationType;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.chono.yopper.R;
import cn.chono.yopper.activity.chat.ChatActivity;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.activity.order.UserAppleOrderPayActivity;
import cn.chono.yopper.activity.usercenter.VipOpenedActivity;
import cn.chono.yopper.activity.usercenter.VipRenewalsActivity;
import cn.chono.yopper.adapter.GiftPassHotAdapter;
import cn.chono.yopper.adapter.GiftUnPassAdapter;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AttributeDto;
import cn.chono.yopper.data.GiftMsg;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.entity.chatgift.GiftInfoEntity;
import cn.chono.yopper.entity.chatgift.GiftOrderResp;
import cn.chono.yopper.entity.chatgift.GiftOrdreReq;
import cn.chono.yopper.entity.chatgift.GiveGiftRpBean;
import cn.chono.yopper.entity.chatgift.PresentGiftInfoBean;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.GiftEvent;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.im.imObserver.ChatObserver;
import cn.chono.yopper.im.model.CustomMessage;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.TextMessage;
import cn.chono.yopper.presenter.GiftContract;
import cn.chono.yopper.presenter.GiftPresenter;
import cn.chono.yopper.recyclerview.Divider;
import cn.chono.yopper.recyclerview.DividerItemDecoration;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.view.LayoutManager.FullyGridLayoutManager;

/**
 * Created by sunquan on 16/8/4.
 */
public class GiftActivity extends BaseActivity<GiftPresenter> implements GiftContract.View {

    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;

    @BindView(R.id.gift_receive_headimg_iv)
    ImageView giftReceiveHeadimgIv;

    @BindView(R.id.gift_receive_name_tv)
    TextView giftReceiveNameTv;

    @BindView(R.id.gift_receive_sex_age_horsecope_tv)
    TextView giftReceiveSexAgeHorsecopeTv;

    @BindView(R.id.gift_user_apple_count_tv)
    TextView giftUserAppleCountTv;

    @BindView(R.id.gift_unpass_hot_rv)
    RecyclerView giftUnpassHotRv;

    @BindView(R.id.gift_pass_hot_rv)
    RecyclerView giftPassHotRv;

    @BindView(R.id.gift_pass_hot_hint_ly)
    LinearLayout giftPassHotHintLy;

    private int userId;

    private String targetHeadImg;

    private int targetId;

    private int targetAge;

    private String datingId;

    private String targetHorsecope;

    private int targetSex;

    private String targetName;

    private GiftPassHotAdapter mPassAdapter;

    private GiftUnPassAdapter mUnPassAdapter;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    Dialog tipsDialog, sendGiftDialog, noAppleDialog, helpdialog, loadDialog;

    ChatObserver mChatObserver;


    private String giftImg;

    private int charmValue;

    private String toSendWords = "";

    @Override
    protected int getLayout() {
        return R.layout.gift_activity;
    }

    @Override
    protected GiftPresenter getPresenter() {
        return new GiftPresenter(mContext, this);
    }

    @Override
    protected void initVariables() {

        userId = LoginUser.getInstance().getUserId();

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            targetHeadImg = bundle.getString(YpSettings.USER_ICON);

            targetName = bundle.getString(YpSettings.USER_NAME);

            targetId = bundle.getInt(YpSettings.USERID);

            targetAge = bundle.getInt(YpSettings.USER_AGE);

            targetHorsecope = bundle.getString(YpSettings.USER_HOROSCOPE);

            targetSex = bundle.getInt(YpSettings.USER_SEX);

            datingId = bundle.getString(YpSettings.DATINGS_ID);
        }

    }

    @Override
    protected void initView() {

        RxBus.get().register(this);

        mPool = Glide.get(this).getBitmapPool();

        transformation = new CropCircleTransformation(mPool);

        baseTitleTv.setText("赠送礼物");

        giftReceiveSexAgeHorsecopeTv.setCompoundDrawablesWithIntrinsicBounds(targetSex == 1 ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman, 0, 0, 0);

        Glide.with(this).load(targetHeadImg).bitmapTransform(transformation).into(giftReceiveHeadimgIv);

        giftReceiveNameTv.setText(targetName);


        if (targetSex == 1) {

            giftReceiveSexAgeHorsecopeTv.setTextColor(this.getResources().getColor(R.color.color_6cbcfc));

        } else {

            giftReceiveSexAgeHorsecopeTv.setTextColor(this.getResources().getColor(R.color.color_fa9cdb));

        }

        giftReceiveSexAgeHorsecopeTv.setText(targetAge + " " + targetHorsecope);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration();

        dividerItemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            public Divider getHorizontalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(getResources().getColor(R.color.color_ffffff)).size(5).build();
            }

            public Divider getVerticalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(Color.alpha(Color.alpha(getResources().getColor(R.color.color_ffffff)))).size(5).build();
            }
        });


        giftUnpassHotRv.addItemDecoration(dividerItemDecoration);
        FullyGridLayoutManager giftUnpassHotGridLayoutManager = new FullyGridLayoutManager(this, 3);
        giftUnpassHotGridLayoutManager.setSmoothScrollbarEnabled(true);
        giftUnpassHotRv.setLayoutManager(giftUnpassHotGridLayoutManager);
        giftUnpassHotRv.setNestedScrollingEnabled(true);
        mUnPassAdapter = new GiftUnPassAdapter(this);
        giftUnpassHotRv.setAdapter(mUnPassAdapter);

        giftPassHotRv.addItemDecoration(dividerItemDecoration);
        FullyGridLayoutManager giftPassHotGridLayoutManager = new FullyGridLayoutManager(this, 3);
        giftPassHotGridLayoutManager.setSmoothScrollbarEnabled(true);
        giftPassHotRv.setLayoutManager(giftPassHotGridLayoutManager);
        giftPassHotRv.setNestedScrollingEnabled(true);
        mPassAdapter = new GiftPassHotAdapter(this);
        giftPassHotRv.setAdapter(mPassAdapter);

        loadDialog = DialogUtil.LoadingDialog(this);
    }

    @Override
    protected void initDataAndLoadData() {

        mPresenter.getAllGifts();
    }

    @OnClick({R.id.base_back_layout, R.id.gift_why_send_no_response_tv, R.id.gift_why_girl_like_gift_tv, R.id.gift_buy_apple_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_back_layout:
                finish();
                break;
            case R.id.gift_why_send_no_response_tv:
                onClikGiftWhySendNoResponse();
                break;
            case R.id.gift_why_girl_like_gift_tv:
                onClikGiftWhyGirlLikeGift();
                break;
            case R.id.gift_buy_apple_tv:

                onClikGetApple();

                break;
        }
    }

    @Override
    public void updateAppleCountView(int count) {

        giftUserAppleCountTv.setText("余额：" + count + "个苹果");

    }

    @Override
    public void updateGiftNoPassList(List<GiftInfoEntity> list) {

        mUnPassAdapter.setData(list);

        mUnPassAdapter.notifyDataSetChanged();

    }

    @Override
    public void updateGiftPassHotList(List<GiftInfoEntity> list) {

        giftPassHotHintLy.setVisibility(View.VISIBLE);

        mPassAdapter.setData(list);

        mPassAdapter.notifyDataSetChanged();

    }

    @Override
    public void showNoGiftPassHotDataView() {

        giftPassHotHintLy.setVisibility(View.GONE);
    }

    @Override
    public void onClikGiftWhySendNoResponse() {

        tipsDialog = DialogUtil.createHintOperateDialog(this, "送礼物后女生没有回应怎么办", "若女生两周内都没有回复您，系统将自动全额退回您购买礼物的苹果\n" +
                "建议：不要向没有通过视频认证的女生送礼", "", "知道了", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                tipsDialog.dismiss();

            }

            @Override
            public void onEnsure(View view, Object... obj) {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.show();
    }

    @Override
    public void onClikGiftWhyGirlLikeGift() {

        tipsDialog = DialogUtil.createHintOperateDialog(this, "为什么女生喜欢礼物？", "女生喜欢大方的男人，送她礼物，会使她有一种被喜欢被认可的感觉，从而大大提高交友成功率\n" +
                "女生收到礼物后，会增加相应的魅力值，魅力值可以兑换成现金", "", "知道了", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

            }

            @Override
            public void onEnsure(View view, Object... obj) {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.show();

    }

    @Override
    public void onClikGetApple() {

        Bundle appleBundle = new Bundle();

        appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);

        ActivityUtil.jump(GiftActivity.this, AppleListActivity.class, appleBundle, 0, 100);

    }

    /**
     * 礼物点击
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {

                    @Tag("onClickNoPassGift")
            }

    )
    public void onClickNoPassGift(CommonEvent commonEvent) {


        if (commonEvent != null && commonEvent.getEvent() != null) {

            giftDto = (GiftInfoEntity) commonEvent.getEvent();

            giftImg = giftDto.getImageUrl();

            charmValue = giftDto.getCharm();

            sendGiftDialog = DialogUtil.createGiftPhotoDialog(this, giftDto.getImageUrl(), giftDto.getGiftName(), "需要" + giftDto.getAppleCount() + "个苹果", "取消", "赠送", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {

                    sendGiftDialog.dismiss();
                }

                @Override
                public void onEnsure(View view, Object... obj) {
                    sendGiftDialog.dismiss();
                    mPresenter.giveGift(giftDto.getGiftId(), datingId, targetId, true, giftDto.getImageUrl(), giftDto.getCharm());
                }
            });
            sendGiftDialog.show();
        }

    }


    private GiftInfoEntity giftDto;

    /**
     * 可以穿hot礼物点击
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("onClickPassHotGift")
            }

    )
    public void onClickPassHotGift(CommonEvent commonEvent) {

        if (commonEvent != null && commonEvent.getEvent() != null) {

            giftDto = (GiftInfoEntity) commonEvent.getEvent();

            sendGiftDialog = DialogUtil.createGiftPhotoDialog(this, giftDto.getImageUrl(), giftDto.getGiftName(), "需要" + giftDto.getAppleCount() + "个苹果", "取消", "赠送", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {

                    sendGiftDialog.dismiss();
                }

                @Override
                public void onEnsure(View view, Object... obj) {
                    sendGiftDialog.dismiss();
                    mPresenter.giveGift(giftDto.getGiftId(), datingId, targetId, true, giftDto.getImageUrl(), giftDto.getCharm());
                    if (!loadDialog.isShowing()) {
                        loadDialog.show();
                    }
                }
            });
            sendGiftDialog.show();

        }
    }


    /**
     * 苹果不足
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("appleInsufficient")
            }

    )
    public void appleInsufficient(CommonEvent commonEvent) {

        loadDialog.dismiss();

        if (commonEvent != null && commonEvent.getEvent() != null) {

            GiveGiftRpBean dto = (GiveGiftRpBean) commonEvent.getEvent();

            noAppleDialog = DialogUtil.createHintOperateDialog(this, "", dto.getMsg(), "取消", "获取苹果", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {
                    noAppleDialog.dismiss();
                }

                @Override
                public void onEnsure(View view, Object... obj) {

                    noAppleDialog.dismiss();

                    if (null != giftDto) {

                        PresentGiftInfoBean giftInfoBean = new PresentGiftInfoBean();

                        giftInfoBean.setToUserId(targetId);

                        giftInfoBean.setDatingId(datingId);

                        giftInfoBean.setFromUserId(LoginUser.getInstance().getUserId());

                        giftInfoBean.setGiftId(giftDto.getGiftId());

                        giftInfoBean.setFirstCall(true);

                        GiftOrdreReq req = new GiftOrdreReq();

                        req.setNeedAppleCount(giftDto.getAppleCount());

                        req.setPresentGiftInfo(giftInfoBean);

                        toSendWords = dto.getToSendWords();

                        mPresenter.appleQuickly(req);

                        if (!loadDialog.isShowing()) {
                            loadDialog.show();
                        }
                    }

                }
            });

            noAppleDialog.show();
        }
    }


    /**
     * 苹果不足
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("sendGiftWithBolck")
            }

    )
    public void sendGiftWithBolck(CommonEvent commonEvent) {

        loadDialog.dismiss();

        String hintmsg = (String) commonEvent.getEvent();

        if (!TextUtils.isEmpty(hintmsg)) {
            DialogUtil.showDisCoverNetToast(this, hintmsg);
        }

    }


    /**
     * 对方拒绝
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("sendGiftWithRefused")
            }

    )
    public void sendGiftWithRefused(CommonEvent commonEvent) {

        loadDialog.dismiss();

        String hintmsg = (String) commonEvent.getEvent();

        if (!TextUtils.isEmpty(hintmsg)) {
            DialogUtil.showDisCoverNetToast(this, hintmsg);
        }

    }

     /**
     * 快速下单成功
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("quicklySuccess")
            }

    )
    public void quicklySuccess(GiftOrderResp resp) {

        loadDialog.dismiss();

        Bundle bundle = new Bundle();


        bundle.putString(YpSettings.ORDER_ID, resp.getOrder().getOrderId());

        bundle.putString(YpSettings.ProductName, resp.getOrder().getProduct().getProductName());

        bundle.putLong(YpSettings.PAY_COST, (resp.getOrder().getTotalFee() / 100));

        bundle.putInt(YpSettings.PAY_TYPE, 3);

        bundle.putString(YpSettings.ToSendWords, toSendWords);


        ActivityUtil.jump(GiftActivity.this, UserAppleOrderPayActivity.class, bundle, 0, 100);

    }



    /**
     * 快速下单失败-没有找到匹配的苹果商品
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("quicklyFiledWithNoApple")
            }

    )
    public void quicklyFiledWithNoApple(GiftOrderResp resp) {

        loadDialog.dismiss();

        Bundle appleBundle = new Bundle();

        appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);

        ActivityUtil.jump(GiftActivity.this, AppleListActivity.class, appleBundle, 0, 100);

    }


    /**
     * 快速下单失败
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("quicklyFiled")
            }

    )
    public void quicklyFiled(Integer status) {

        loadDialog.dismiss();

//        DialogUtil.showDisCoverNetToast(GiftActivity.this, "购买苹果出现异常");

    }


    /**
     * 礼物赠送失败 hot
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("giveGiftFiledHot")
            }

    )
    public void giveGiftFiledHot(CommonEvent commonEvent) {

        loadDialog.dismiss();

        helpdialog = DialogUtil.createHotHintDialog(GiftActivity.this, hotbackCallListener);
        if (!isFinishing()) {
            helpdialog.show();
        }

    }


    //hot提示框
    private BackCallListener hotbackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {

            if (!isFinishing()) {
                helpdialog.dismiss();
            }

            //根据登陆用户的VIP状态跳转页面
            //若没有开通过VIP，点击进入VIP介绍页
            //若VIP已过期，点击进入续费页面

            Bundle bundle = new Bundle();

            int userPosition = DbHelperUtils.getOldVipPosition(userId);

            bundle.putInt("userPosition", userPosition);


            if (0 == userPosition) {
                ActivityUtil.jump(GiftActivity.this, VipOpenedActivity.class, bundle, 0, 100);
            } else {
                ActivityUtil.jump(GiftActivity.this, VipRenewalsActivity.class, bundle, 0, 100);
            }


        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }

        }
    };

    /**
     * 礼物赠送成功
     */

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {


                    @Tag("giveGiftSuccess")
            }

    )
    public void giveGiftSuccess(GiftEvent giftEvent) {

        if (loadDialog.isShowing()) {
            loadDialog.dismiss();
        }

//        if (giftEvent != null) {
//            sendGiftMsg(giftEvent.getGiftImg(), giftEvent.getCharmValue());
//        }

        if (giftEvent != null && !TextUtils.isEmpty(giftEvent.getToSendWords())) {

            sendTextMsg(giftEvent.getToSendWords());
        }


        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, targetId);

        bundle.putString(YpSettings.DATINGS_ID, datingId);

        ActivityUtil.jump(GiftActivity.this, ChatActivity.class, bundle, 0, 100);
        finish();
    }


    //快速购买苹果支付成功
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("quicklyPaySuccessForGift")

            }
    )
    public void quicklyPaySuccessForGift(CommonEvent event) {

//        sendGiftMsg(giftImg, charmValue);

        if (!TextUtils.isEmpty(toSendWords)) {
            sendTextMsg(toSendWords);
        }


        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, targetId);

        bundle.putString(YpSettings.DATINGS_ID, datingId);

        ActivityUtil.jump(GiftActivity.this, ChatActivity.class, bundle, 0, 100);

        finish();

    }


    private void sendTextMsg(String textStr) {

        mChatObserver = new ChatObserver(targetId + "", TIMConversationType.C2C);

        long date = System.currentTimeMillis();
        TextMsg textmsg = new TextMsg(MessageType.Text, textStr, 0, datingId, 0);

        String msg_str = JsonUtils.toJson(textmsg);

        AttributeDto attributeDto = new AttributeDto();
        attributeDto.setMask(0);
        attributeDto.setDateid(datingId);
        attributeDto.setCounsel(0);
        attributeDto.setType(MessageType.Text);

        String attributeDto_str = JsonUtils.toJson(attributeDto);

        ImMessage message = new TextMessage(textStr, attributeDto_str);

        String TIMMessageStr = JsonUtils.toJson(message.getMessage());

        String msgid = message.getMessage().getMsgId();

        ChatUtils.SaveOrUpdateChatMsgToDB(targetId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.succeed, datingId, 0, TIMMessageStr);
        ChatUtils.saveMessageRecord(msg_str, targetId + "", ChatDto.succeed, ChatDto.s_type, date, 0, datingId, TIMMessageStr);

        mChatObserver.sendMessage(message.getMessage(), null);

    }


//    private void sendGiftMsg(String giftImg, int charmValue) {
//
//        if (TextUtils.isEmpty(giftImg)) {
//
//            return;
//        }
//
//        mChatObserver = new ChatObserver(targetId + "", TIMConversationType.C2C);
//
//        GiftMsg msg = new GiftMsg();
//
//        msg.setDateid(datingId);
//        msg.setCounsel(0);
//        msg.setMask(0);
//        msg.setType(MessageType.Gift);
//
//        msg.setGiftImg(giftImg);
//        msg.setCharmValue(charmValue);
//
//        long date = System.currentTimeMillis();
//        String msg_str = JsonUtils.toJson(msg);
//
//
//        ImMessage message = new CustomMessage(msg_str, "[礼物]");
//
//        String TIMMessageStr = JsonUtils.toJson(message.getMessage());
//
//        String msgid = message.getMessage().getMsgId();
//
//        ChatUtils.SaveOrUpdateChatMsgToDB(targetId + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.sending, datingId, 0, TIMMessageStr);
//
//        ChatUtils.saveMessageRecord(msg_str, targetId + "", ChatDto.sending, ChatDto.s_type, date, 0, datingId, TIMMessageStr);
//
//        mChatObserver.sendMessage(message.getMessage(), null);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        if (mChatObserver != null) {
            mChatObserver.stop();
        }
    }


    //快速购买苹果支付成功
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("AppleBuySuccess")

            }
    )
    public void AppleBuySuccess(CommonEvent event) {


        mPresenter.getAllGifts();

    }


}
