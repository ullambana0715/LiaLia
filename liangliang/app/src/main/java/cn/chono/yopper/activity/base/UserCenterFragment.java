package cn.chono.yopper.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserCenterInfo.UserCenterInfoRespBean;
import cn.chono.yopper.Service.Http.UserCenterInfo.UserCenterInfoService;
import cn.chono.yopper.activity.chat.MessageListActivity;
import cn.chono.yopper.activity.find.MyBonusActivity;
import cn.chono.yopper.activity.find.MyEnergyActivity;
import cn.chono.yopper.activity.order.UserOrderListActivity;
import cn.chono.yopper.activity.usercenter.MyActivitiesActivity;
import cn.chono.yopper.activity.usercenter.SettingActivity;
import cn.chono.yopper.activity.usercenter.UserAccountActivity;
import cn.chono.yopper.activity.usercenter.VisitorsActivity;
import cn.chono.yopper.activity.video.VideoDetailGetActivity;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.LoginVideoStatusDto;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.Visits;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.SyncVideoStateEvent;
import cn.chono.yopper.glide.BlurTransformation;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.ui.UserInfoEditActivity;
import cn.chono.yopper.ui.like.LikeBaseActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.ViewsUtils;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 个人中心
 *
 * @author SQ
 */
public class UserCenterFragment extends Fragment implements OnClickListener {


    private Activity mActivity;
    private TextView user_name_tv;// 昵称

    private RelativeLayout user_center_head_img_layout;
    private ImageView user_head_img_iv;// 头像

    private ImageView user_center_vip_iv;

    private RelativeLayout video_layout;//认证
    private TextView user_center_video_state_tv;//认证状态

    private ImageView user_center_video_hint_iv;

    private RelativeLayout look_layout;// 看过我

    private TextView look_num_tv;// 看过我的总人数

    private TextView add_look_num_tv;// 新增查看人数

    private TextView usercenter_apple_num_tv;//p果的数量

    private TextView usercenter_key_num_tv;//钥匙的数量

    private ImageView usercenter_acount_attract_iv;//魅力值图标


    private TextView usercenter_attract_num_tv;//魅力值的数量

    private TextView power_value_tv;//能量值

    private RelativeLayout user_center_hint_layout;//个人资料完善度

    private TextView user_center_done_hint_tv;//资料完善度


    private RelativeLayout user_center_message;//我的消息
    private TextView user_center_message_num_tv;

    private LinearLayout user_center_like;//喜欢的人

    private LinearLayout user_center_event;//我的活动

    private LinearLayout user_center_prize;//我的奖品


    private ImageView user_center_like_update_iv;

    private ImageView user_center_prize_iv;

    private LinearLayout p_guo_iv;//我的P果

    private LinearLayout power_iv;//我的能量

    private RelativeLayout setting_iv;//设置

    private RelativeLayout user_center_order_rl;

    private BlurTransformation blurtransformation;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;

    private int pguo_count = 0;

    private int mKeyCount = 0;

    private int remainCharm;//魅力值

    private int userid;

    protected CompositeSubscription mCompositeSubscription;


    private boolean isgetUserinfo = false;

    public UserCenterFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.usercenter_layout, container, false);

        PushAgent.getInstance(mActivity).onAppStart();

        RxBus.get().register(this);

        initView(convertView);

        userid = LoginUser.getInstance().getUserId();

        setNoReadMessageNum(userid + "");

        return convertView;

    }

    private void initView(View view) {


        setting_iv = (RelativeLayout) view.findViewById(R.id.user_center_setting_iv);
        setting_iv.setOnClickListener(this);

        user_name_tv = (TextView) view.findViewById(R.id.user_center_name_tv);

        user_center_done_hint_tv = (TextView) view.findViewById(R.id.user_center_done_hint_tv);

        user_center_head_img_layout = (RelativeLayout) view.findViewById(R.id.user_center_head_img_layout);
        user_head_img_iv = (ImageView) view.findViewById(R.id.user_center_head_img_iv);

        user_center_vip_iv = (ImageView) view.findViewById(R.id.user_center_vip_iv);

        user_head_img_iv.setOnClickListener(this);


        video_layout = (RelativeLayout) view.findViewById(R.id.user_center_video_layout);
        video_layout.setOnClickListener(this);

        user_center_video_hint_iv = (ImageView) view.findViewById(R.id.user_center_video_hint_iv);

        look_layout = (RelativeLayout) view.findViewById(R.id.user_center_look_num_layout);
        look_layout.setOnClickListener(this);

        look_num_tv = (TextView) view.findViewById(R.id.user_center_look_num_tv);
        add_look_num_tv = (TextView) view.findViewById(R.id.user_center_add_look_num_tv);

        usercenter_apple_num_tv = (TextView) view.findViewById(R.id.usercenter_apple_num_tv);

        usercenter_key_num_tv = (TextView) view.findViewById(R.id.usercenter_key_num_tv);

        usercenter_acount_attract_iv = (ImageView) view.findViewById(R.id.usercenter_acount_attract_iv);

        usercenter_attract_num_tv = (TextView) view.findViewById(R.id.usercenter_attract_num_tv);
        usercenter_acount_attract_iv = (ImageView) view.findViewById(R.id.usercenter_acount_attract_iv);

        power_value_tv = (TextView) view.findViewById(R.id.user_power_value_tv);


        user_center_event = (LinearLayout) view.findViewById(R.id.user_center_event);
        user_center_event.setOnClickListener(this);


        user_center_message = (RelativeLayout) view.findViewById(R.id.user_center_message);
        user_center_message_num_tv = (TextView) view.findViewById(R.id.user_center_message_num_tv);
        user_center_message.setOnClickListener(this);

        user_center_like = (LinearLayout) view.findViewById(R.id.user_center_like);
        user_center_like.setOnClickListener(this);

        user_center_like_update_iv = (ImageView) view.findViewById(R.id.user_center_like_update_iv);

        p_guo_iv = (LinearLayout) view.findViewById(R.id.user_center_P_guo);
        p_guo_iv.setOnClickListener(this);

        power_iv = (LinearLayout) view.findViewById(R.id.user_center_power);
        power_iv.setOnClickListener(this);


        user_center_prize = (LinearLayout) view.findViewById(R.id.user_center_prize);
        user_center_prize_iv = (ImageView) view.findViewById(R.id.user_center_prize_iv);
        user_center_prize.setOnClickListener(this);

        boolean prize = SharedprefUtil.getBoolean(mActivity, userid + "" + "prize", false);


        if (prize) {
            user_center_prize_iv.setVisibility(View.VISIBLE);
        } else {
            user_center_prize_iv.setVisibility(View.GONE);
        }

        user_center_hint_layout = (RelativeLayout) view.findViewById(R.id.user_center_hint_layout);
        user_center_hint_layout.setOnClickListener(this);

        user_center_order_rl = (RelativeLayout) view.findViewById(R.id.user_center_order_rl);
        user_center_order_rl.setOnClickListener(this);

        user_center_video_state_tv = (TextView) view.findViewById(R.id.user_center_video_state_tv);

    }

    private void localUserInfo() {
        UserInfo userInfo = DbHelperUtils.getUserInfo(userid);
        if (userInfo != null) {

            UserDto dto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
            if (dto != null) {
                initData(dto);
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initData(UserDto dto) {

        mPool = Glide.get(mActivity).getBitmapPool();
        blurtransformation = new BlurTransformation(mActivity, mPool, 15, 15);
        transformation = new CropCircleTransformation(mPool);

        if (dto == null) {
            return;
        }


        if (dto.getProfile().getSex() == 2) {
            usercenter_acount_attract_iv.setVisibility(View.VISIBLE);
            usercenter_attract_num_tv.setVisibility(View.VISIBLE);
            usercenter_attract_num_tv.setText(String.valueOf(remainCharm));
        } else {
            usercenter_acount_attract_iv.setVisibility(View.GONE);
            usercenter_attract_num_tv.setVisibility(View.GONE);
        }


        if (dto.getProfile() != null) {

            if (!CheckUtil.isEmpty(dto.getProfile().getHeadImg())) {

                Glide.with(getActivity()).load(dto.getProfile().getHeadImg()).bitmapTransform(transformation).into(user_head_img_iv);
            }

            if (dto.getProfile().getCompletion() >= 80) {
                user_center_hint_layout.setVisibility(View.GONE);
            } else {
                user_center_hint_layout.setVisibility(View.VISIBLE);
                user_center_done_hint_tv.setText("个人资料完善度" + done_info_num(dto.getProfile().getCompletion()) + "0%");
            }

            user_name_tv.setText(dto.getProfile().getName());

        }

        setLookme();

        if (dto.getVideoVerification() != null) {
            setLoginVideoStatus(dto.getVideoVerification().getStatus());

            LoginVideoStatusDto loginVideoStatusDto = DbHelperUtils.getLoginVideoStatusDto(userid);

            if (loginVideoStatusDto != null) {

                if (loginVideoStatusDto.getVideoVerificationStatus() != dto.getVideoVerification().getStatus()) {
                    user_center_video_hint_iv.setVisibility(View.VISIBLE);
                } else {

                    if (dto.getVideoVerification().getStatus() == 0) {

                        if (loginVideoStatusDto.getIsVisible() == 1) {
                            user_center_video_hint_iv.setVisibility(View.VISIBLE);
                        } else {
                            user_center_video_hint_iv.setVisibility(View.GONE);
                        }

                    } else {
                        user_center_video_hint_iv.setVisibility(View.GONE);

                    }


                }

            } else {

                if (dto.getVideoVerification().getStatus() == 0) {
                    user_center_video_hint_iv.setVisibility(View.VISIBLE);
                } else {
                    user_center_video_hint_iv.setVisibility(View.GONE);
                }

            }


        } else {

            LoginVideoStatusDto loginVideoStatusDto = DbHelperUtils.getLoginVideoStatusDto(userid);

            if (loginVideoStatusDto != null) {
                setLoginVideoStatus(loginVideoStatusDto.getVideoVerificationStatus());

                if (loginVideoStatusDto.getIsVisible() == 1) {
                    user_center_video_hint_iv.setVisibility(View.VISIBLE);
                } else {
                    user_center_video_hint_iv.setVisibility(View.GONE);
                }

            } else {

                setLoginVideoStatus(0);
                user_center_video_hint_iv.setVisibility(View.VISIBLE);

            }
        }


        //Vip等级
        switch (dto.getCurrentUserPosition()) {
            //不是vip
            case 0:
                user_center_vip_iv.setVisibility(View.GONE);
                break;
            //白银
            case 1:
                user_center_vip_iv.setVisibility(View.VISIBLE);
                user_center_vip_iv.setBackgroundResource(R.drawable.ic_usercenter_vip_silver);
                break;
            //黄金
            case 2:
                user_center_vip_iv.setVisibility(View.VISIBLE);
                user_center_vip_iv.setBackgroundResource(R.drawable.ic_usercenter_vip_gold);
                break;
            //铂金
            case 3:
                user_center_vip_iv.setVisibility(View.VISIBLE);
                user_center_vip_iv.setBackgroundResource(R.drawable.ic_usercenter_vip_platinum);
                break;
            //钻石
            case 4:
                user_center_vip_iv.setVisibility(View.VISIBLE);
                user_center_vip_iv.setBackgroundResource(R.drawable.ic_usercenter_vip_diamond);
                break;

        }

        if (dto.getProfile().getSex() == 1) {
            usercenter_acount_attract_iv.setVisibility(View.GONE);

            usercenter_attract_num_tv.setVisibility(View.GONE);
        }

        setLikeUpdate();
    }


    private void setLookme() {

        Visits dto = DbHelperUtils.getVisits(userid);

        if (dto != null) {
            if (dto.getNewadded() != 0) {
                add_look_num_tv.setVisibility(View.VISIBLE);
                add_look_num_tv.setText("+" + dto.getNewadded());
            } else {
                add_look_num_tv.setVisibility(View.GONE);
            }

            look_num_tv.setText(dto.getTotal() + "");

        } else {
            add_look_num_tv.setVisibility(View.GONE);
        }


    }

    private void setLoginVideoStatus(int status) {


        if (status == 0) {
            user_center_video_state_tv.setText("未认证");
        } else if (status == 1) {
            user_center_video_state_tv.setText("审核中");
        } else if (status == 2) {
            user_center_video_state_tv.setText("已认证");
        } else if (status == 3) {
            user_center_video_state_tv.setText("未认证");
        }

    }

    /**
     * 获取用户信息
     */
    private void getAcountInfo() {

        UserCenterInfoService userCenterInfoService = new UserCenterInfoService(mActivity);

        userCenterInfoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UserCenterInfoRespBean resp = (UserCenterInfoRespBean) respBean;

                if (resp.getResp() != null) {

                    pguo_count = resp.getResp().getAppleCount();

                    usercenter_apple_num_tv.setText(formatAccountValue(pguo_count));

                    mKeyCount = resp.getResp().getKeyCount();

                    usercenter_key_num_tv.setText(formatAccountValue(mKeyCount));


                    power_value_tv.setText(String.valueOf(resp.getResp().getMyPower().getCurrentValue()));

                    //下面两行就添魅力值

                    remainCharm = resp.getResp().getRemainCharm();

                    usercenter_attract_num_tv.setText(formatAccountValue(remainCharm));

                }
            }
        }, new OnCallBackFailListener());

        userCenterInfoService.enqueue();
    }

    private int done_info_num(int completionnum) {
        int num = 0;
        num = completionnum / 10;
        return num;
    }


    private String formatAccountValue(int num) {

        String value = "";
        if (num >= 1000 && num < 10000) {
            int i = num / 1000;
            int w = num % 1000;
            if (w == 0) {
                value = i + "K";
            } else {
                value = i + "K+";
            }

        } else if (num < 1000) {

            value = num + "";

        } else if (num > 10000) {

            int i = num / 10000;
            int w = num % 10000;
            if (w == 0) {
                value = i + "W";
            } else {
                value = i + "W+";
            }

        }
        return value;
    }


    /**
     * 设置未读消息数量
     *
     * @param userid
     */
    public void setNoReadMessageNum(String userid) {

        long no_read_num = ChatUtils.getNoReadNum(userid);

        if (no_read_num > 0) {
            if (no_read_num < 10) {
                user_center_message_num_tv.setBackgroundResource(R.drawable.circle_messaga_num_bg);
            } else {
                user_center_message_num_tv.setBackgroundResource(R.drawable.center_messaga_num_bg);
            }
            user_center_message_num_tv.setVisibility(View.VISIBLE);
            user_center_message_num_tv.setText(no_read_num + "");
        } else {
            user_center_message_num_tv.setVisibility(View.GONE);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人中心"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(mActivity);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("个人中心"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(mActivity); // 统计时长
        getAcountInfo();
        if (isgetUserinfo) {
            getUserInfo(userid);
        }

        localUserInfo();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        unSubscribe();

    }

    private void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("ReceiveNewMessage")

            }
    )
    public void ReceiveNewMessage(CommonEvent event) {
        if (event != null && event.getEvent() != null) {
            setNoReadMessageNum(userid + "");
        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("refreshMessageList")

            }
    )
    public void refreshMessageNum(CommonEvent event) {

        setNoReadMessageNum(userid + "");

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("refreshMessageNum")

            }
    )
    public void refreshMessage(CommonEvent event) {

        setNoReadMessageNum(userid + "");

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeMe")

            }
    )
    public void likeMe(CommonEvent event) {
        setLikeUpdate();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachOther")

            }
    )
    public void likeEachOther(CommonEvent event) {
        setLikeUpdate();
    }


    private void setLikeUpdate() {

        boolean likeMe = SharedprefUtil.getBoolean(ContextUtil.getContext(), userid + "likeMe", false);

        boolean likeEachOther = SharedprefUtil.getBoolean(ContextUtil.getContext(), userid + "likeEachOther", false);

        if (likeMe || likeEachOther) {

            user_center_like_update_iv.setVisibility(View.VISIBLE);

        } else {

            user_center_like_update_iv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        ViewsUtils.preventViewMultipleClick(v, 1000);
        int id = v.getId();
        Bundle bundle = new Bundle();
        switch (id) {

            case R.id.user_center_head_img_iv:// 头像--个人资料


                bundle.putInt(YpSettings.USERID, userid);

                ActivityUtil.jump(mActivity, UserInfoActivity.class, bundle, 0, 100);


                break;

            case R.id.user_center_order_rl:// 我的订单

                bundle.putInt(YpSettings.ORDER_TYPE, Constant.OrderType_Advisory);
                ActivityUtil.jump(mActivity, UserOrderListActivity.class, bundle, 0, 100);

                break;

            case R.id.user_center_look_num_layout:// 看过我

                bundle.putInt(YpSettings.USERID, userid);

                ActivityUtil.jump(mActivity, VisitorsActivity.class, bundle, 0, 100);
                break;


            case R.id.user_center_like:// 喜欢的人

//                ActivityUtil.jump(getActivity(), LikeActivity.class, null, 0, 100);
                ActivityUtil.jump(mActivity, LikeBaseActivity.class, null, 0, 100);

                break;

            case R.id.user_center_event:// 我的活动

                ActivityUtil.jump(mActivity, MyActivitiesActivity.class, null, 0, 100);
                break;

            case R.id.user_center_prize:// 我的奖品

                SharedprefUtil.saveBoolean(mActivity, userid + "" + "prize", false);


                user_center_prize_iv.setVisibility(View.GONE);

                RxBus.get().post("SyncVideoStateEvent", new SyncVideoStateEvent());

                ActivityUtil.jump(mActivity, MyBonusActivity.class, null, 0, 100);

                break;


            case R.id.user_center_P_guo:// 我的账户
                MobclickAgent.onEvent(mActivity, "btn_find_event_myapple");
                bundle.putInt("apple_count", pguo_count);
                bundle.putInt("key_count", mKeyCount);
                ActivityUtil.jump(mActivity, UserAccountActivity.class, bundle, 0, 100);
                break;

            case R.id.user_center_power://我的能量
                MobclickAgent.onEvent(mActivity, "btn_find_event_myenergy");
                ActivityUtil.jump(mActivity, MyEnergyActivity.class, bundle, 0, 100);
                break;

            case R.id.user_center_setting_iv:// 设置

                ActivityUtil.jump(mActivity, SettingActivity.class, bundle, 0, 100);


                break;

            case R.id.user_center_message:// 消息列表

                ActivityUtil.jump(mActivity, MessageListActivity.class, bundle, 0, 100);

                break;


            case R.id.user_center_hint_layout:// 个人信息编辑页面

                bundle.putInt(YpSettings.USERID, userid);

                ActivityUtil.jump(mActivity, UserInfoEditActivity.class, bundle, 0, 100);
                break;

            case R.id.user_center_video_layout://视频详情

                isgetUserinfo = true;
                DbHelperUtils.saveOrUpdateLoginVideoStatusDto(userid, 0, 0);
                user_center_video_hint_iv.setVisibility(View.GONE);
                RxBus.get().post("SyncVideoStateEvent", new SyncVideoStateEvent());
                bundle.putInt(YpSettings.USERID, userid);
                ActivityUtil.jump(mActivity, VideoDetailGetActivity.class, bundle, 0, 100);

                break;

        }
    }


    public void getUserInfo(final int userid) {

        double latitude = 0;
        double longtitude = 0;
        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            latitude = myLoc.getLoc().getLatitude();
            longtitude = myLoc.getLoc().getLongitude();
        }

        LatLng pt = new LatLng(latitude, longtitude);

        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        Double lat = null;
        Double log = null;

        if (latitude != 0 && longtitude != 0 && latitude != longtitude) {

            lat = pt.latitude;
            log = pt.longitude;

        }


        Subscription subscription = HttpFactory.getHttpApi()
                .getUserInfo(userid, true, true, true, false, true, true, true, true, true, true, true, true, true, lat, log)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(userdto -> {

                    Logger.e(userdto.toString());

                    String jsonstr = JsonUtils.toJson(userdto);

                    // 保存数据
                    DbHelperUtils.saveUserInfo(userid, jsonstr);
                    // 保存数据
                    DbHelperUtils.saveBaseUser(userid, userdto);

                    initData(userdto);


                }, throwable -> {


                });


        addSubscrebe(subscription);

    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }


}
