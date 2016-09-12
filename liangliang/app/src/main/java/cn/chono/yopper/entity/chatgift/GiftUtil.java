package cn.chono.yopper.entity.chatgift;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserReceiveGiftTable;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.view.MyDialog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jianghua on 2016/8/1.
 */
public class GiftUtil {
//    private static GiftUtil instances;
//
//    public static GiftUtil getInstances(){
//        if(null == instances){
//            synchronized (GiftUtil.class){
//                if(null == instances){
//                    instances = new GiftUtil();
//                }
//            }
//        }
//        return instances;
//    }

    public CompositeSubscription mCompositeSubscription;

    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     * 保存礼物数据
     *
     * @param userId
     * @param data
     */
    public void saveGift(int userId, String data) {
        GiftBeanDto giftBeanDto;
        try {
            giftBeanDto = App.db.findFirst(Selector.from(GiftBeanDto.class).where("userId", " =", userId));
            if (null != giftBeanDto) {
                giftBeanDto.setUserId(LoginUser.getInstance().getUserId());
                giftBeanDto.setResp(data);
                App.db.update(giftBeanDto);
            } else {
                giftBeanDto = new GiftBeanDto();
                giftBeanDto.setUserId(LoginUser.getInstance().getUserId());
                giftBeanDto.setResp(data);
                App.db.save(giftBeanDto);
            }
        } catch (DbException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * 获取本地礼物数据
     *
     * @param userId
     * @return
     */
    public HashMap<String, Object> getGiftData(int userId) {
        GiftBeanDto giftBeanDto;
        AllGiftsEntity giftBeanList;
        List<GiftInfoEntity> giftHot;
        List<GiftInfoEntity> giftUnHot = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        int appcount = 0;
        try {
            giftBeanDto = App.db.findFirst(Selector.from(GiftBeanDto.class).where("userId", " =", userId));

            if (null != giftBeanDto && !TextUtils.isEmpty(giftBeanDto.getResp())) {

                giftBeanList = JsonUtils.fromJson(giftBeanDto.getResp(), AllGiftsEntity.class);

                giftUnHot = giftBeanList.getGiftsOfUnpassHot();

                giftHot = giftBeanList.getGiftsOfPassHot();

                appcount = giftBeanList.getRemainAppleCount();

                if (giftHot != null && giftHot.size() > 0) {

                    GiftInfoEntity passHot;

                    for (int i = 0; i < giftHot.size(); i++) {

                        passHot = new GiftInfoEntity();
                        passHot.setGiftId(giftHot.get(i).getGiftId());
                        passHot.setAppleCount(giftHot.get(i).getAppleCount());
                        passHot.setCharm(giftHot.get(i).getCharm());
                        passHot.setGiftName(giftHot.get(i).getGiftName());
                        passHot.setImageUrl(giftHot.get(i).getImageUrl());
                        giftUnHot.add(passHot);

                    }
                }
            }

            map.put("listdata", giftUnHot);

            map.put("appcount", appcount);
            return map;
        } catch (DbException e) {
            e.printStackTrace();

        }
        return map;
    }

    /**
     * 获取网络礼物数据
     */
    public void getNetGiftData(int userid) {
        Subscription subscription = HttpFactory.getHttpApi().getAllGifts()
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(giftBean -> {
                    Logger.e("获取得到了礼物数据：：：" + giftBean.toString());
                    if (null != giftBean) {
                        saveGift(userid, JsonUtils.toJson(giftBean));
                    }
                }, throwable -> {

                });

        if (subscription != null) {
            addSubscrebe(subscription);
        }
    }


    /**
     * 礼物的弹框
     *
     * @param context
     * @param url
     * @param contenttv
     * @param contenttv2
     * @param leftBtn
     * @param rightbtn
     * @param isgive
     * @return
     */
    public Dialog GiftClickDialog(Context context, String url, String contenttv, String contenttv2, String leftBtn, String rightbtn, boolean isgive) {
        Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.view_gift_dialog, contentView -> {

            LinearLayout layout = (LinearLayout) contentView.findViewById(R.id.gift_dialog_layout);
            RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.gift_dialog_bg);

            ViewGroup.LayoutParams lp = relativeLayout.getLayoutParams();
            lp.height = UnitUtil.dip2px(230, context);
            lp.width = UnitUtil.dip2px(280, context);
            relativeLayout.setLayoutParams(lp);


            ImageView dialogIv = (ImageView) contentView.findViewById(R.id.dialog_iv);

            TextView remindTv = (TextView) contentView.findViewById(R.id.remind_name_tv);

            TextView remindTv2 = (TextView) contentView.findViewById(R.id.remind_content_tv);

            Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);

            Button sureBtn = (Button) contentView.findViewById(R.id.sure_btn);

            if (!TextUtils.isEmpty(url)) {
                dialogIv.setVisibility(View.VISIBLE);
                String imageUrl = ImgUtils.DealImageUrl(url, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
                Glide.with(context).load(imageUrl).into(dialogIv);
            } else {
                dialogIv.setVisibility(View.GONE);
            }

            remindTv.setText(contenttv);
            remindTv2.setText(contenttv2);
            cancelBtn.setText(rightbtn);
            sureBtn.setText(leftBtn);


            cancelBtn.setOnClickListener(v -> {
                Logger.e("点击了取消送礼物按钮 ；GiftUtils；；；" + isgive);
                RxBus.get().post("cancelbtn", true);
            });

            sureBtn.setOnClickListener(v -> {
                Logger.e("点击了确定送礼物按钮 ；GiftUtils；；；" + isgive);
                if (isgive) {
                    RxBus.get().post("sureBtn", 1);//是赠送
                } else {
                    RxBus.get().post("sureBtn", 0);//苹果不足 去购买苹果
                }
            });
        });
        return dialog;
    }

    /**
     * 赠送礼物苹果不足快速下单
     *
     * @param rep
     */
    public void giveGiftOrderInfo(GiftOrdreReq rep) {
        Subscription subscription = HttpFactory.getHttpApi().giveOrder(rep)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(orderResp -> {
                    GiftOrderResp resp = orderResp;
                    Logger.e("快速下单成功" + resp.toString());

                    if(resp.getResult()==0){

                        RxBus.get().post("giftOrderInfo", resp);

                    }else if(resp.getResult()==1){
                        RxBus.get().post("giftOrderInfoWithNoApple", resp);
                    }


                }, throwable -> {
                    Logger.e("购买苹果出现异常");
                    RxBus.get().post("giftOrderInfoFiled", 1);
                });

        if (subscription != null) {
            addSubscrebe(subscription);
        }

    }

    /**
     * 赠送礼物
     *
     * @param giftid
     * @param datingId
     * @param toUserId
     * @param isFirstCall
     */
    public void giveGift(Activity context, String giftid, String datingId, int toUserId, boolean isFirstCall, int position) {
        GiveGiftBody body = new GiveGiftBody();
        body.setDatingId(datingId);
        body.setIsFirstCall(isFirstCall);
        body.setToUserId(toUserId);

        Logger.e("送礼物的body ==" + body.toString());
        Subscription subscription = HttpFactory.getHttpApi().giveGift(giftid, body)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(givegift -> {
                    Logger.e("赠送礼物的返回成功");
                    // 赠送结果（0：成功 1：对方Hot 2：苹果数量不足）\r\n            当对方Hot时，app端组织提示文案
                    if (givegift.getResult() == 0) {
                        RxBus.get().post("giveSuccess", position);
                    } else if (givegift.getResult() == 1) {
                        RxBus.get().post("giveHotFiled", position);
                    } else if (givegift.getResult() == 2) {
                        RxBus.get().post("giveFiled", new CommonEvent<>(givegift));
                    } else if (givegift.getResult() == 3) {
                        //存在拉黑关系
                        RxBus.get().post("giveFiledWithBlock", new CommonEvent<>(givegift));
                    } else if (givegift.getResult() == 4) {
                        //对方拒绝接受礼物
                        RxBus.get().post("giveFiledWithRefused", new CommonEvent<>(givegift));
                    }

                }, throwable -> {
                    RxBus.get().post("giveError", new CommonEvent<>());
                    DialogUtil.showDisCoverNetToast(context, "赠送失败");
                });

        if (subscription != null) {
            addSubscrebe(subscription);
        }

    }


    /**
     * 接收礼物
     *
     * @param datingId
     * @param fromUserId
     */
    public void receiveGift(String datingId, int fromUserId, int userid) {

        ReceiveGiftBody body = new ReceiveGiftBody();
        body.setDatingId(datingId);
        body.setFromUserId(fromUserId);

        Subscription subscription = HttpFactory.getHttpApi().receiveGift(body)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(receivegift -> {

                    Logger.e("接收礼物成功");
                    if (receivegift != null) {

                        if (receivegift.getResult() == 0) {

                            ChatUtils.changeGiftReceiveStatus(fromUserId + "", userid + "", datingId, UserReceiveGiftTable.Received);

                        } else if (receivegift.getResult() == 1) {
                            //存在拉黑关系
                            RxBus.get().post("gifteReceiveWithBlock", new CommonEvent<>(receivegift.getMsg()));

                        }


                    }


                }, throwable -> {

                    Logger.e("接收礼物成功失败");
                    ChatUtils.changeGiftReceiveStatus(fromUserId + "", userid + "", datingId, UserReceiveGiftTable.no_Receive);

                });

        if (subscription != null) {
            addSubscrebe(subscription);
        }

    }

}
