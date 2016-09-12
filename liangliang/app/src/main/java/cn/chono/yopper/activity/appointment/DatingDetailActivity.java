package cn.chono.yopper.activity.appointment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.util.LogUtils;
import com.tencent.TIMConversationType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportBean;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportService;
import cn.chono.yopper.Service.Http.ChatDatingUserAttampt.ChatDatingUserAttamptBean;
import cn.chono.yopper.Service.Http.ChatDatingUserAttampt.ChatDatingUserAttamptRespBean;
import cn.chono.yopper.Service.Http.ChatDatingUserAttampt.ChatDatingUserAttamptService;
import cn.chono.yopper.Service.Http.DatingDetail.DatingDetailBean;
import cn.chono.yopper.Service.Http.DatingDetail.DatingDetailRespBean;
import cn.chono.yopper.Service.Http.DatingDetail.DatingDetailService;
import cn.chono.yopper.Service.Http.DatingPublish.Marriage;
import cn.chono.yopper.Service.Http.DatingPublish.Travel;
import cn.chono.yopper.Service.Http.DatingsClose.DatingsCloseBean;
import cn.chono.yopper.Service.Http.DatingsClose.DatingsCloseRespBean;
import cn.chono.yopper.Service.Http.DatingsClose.DatingsCloseService;
import cn.chono.yopper.Service.Http.DatingsUserList.DatingUserListBean;
import cn.chono.yopper.Service.Http.DatingsUserList.DatingUserListMoreBean;
import cn.chono.yopper.Service.Http.DatingsUserList.DatingUserListMoreService;
import cn.chono.yopper.Service.Http.DatingsUserList.DatingUserListRespBean;
import cn.chono.yopper.Service.Http.DatingsUserList.DatingUserListService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.activity.chat.ChatActivity;
import cn.chono.yopper.activity.usercenter.VipOpenedActivity;
import cn.chono.yopper.activity.usercenter.VipRenewalsActivity;
import cn.chono.yopper.activity.video.VideoDetailGetActivity;
import cn.chono.yopper.adapter.DatingDetailSignUpAdapter;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.data.AppointOwner;
import cn.chono.yopper.data.AttributeDto;
import cn.chono.yopper.data.DatingUserListDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MessageType;
import cn.chono.yopper.data.TextMsg;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.AttamptRespDto;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.event.DatingsRefreshEvent;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.im.imObserver.ChatObserver;
import cn.chono.yopper.im.model.ImMessage;
import cn.chono.yopper.im.model.TextMessage;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.smack.entity.ChatDto;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.ui.gift.GiftActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCall;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.InfoTransformUtils;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.FlowCenterLayout;
import cn.chono.yopper.view.MiListView;
import cn.chono.yopper.view.MyDialog;
import cn.chono.yopper.view.ProgressBarView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 邀约详情
 *
 * @author sam.sun
 */
public class DatingDetailActivity extends MainFrameActivity implements OnClickListener {


    private LayoutInflater mInflater;

    private View contextView;

    private RelativeLayout dating_detail_root_layout;

    private ImageView dating_detail_userImg_iv;

    private TextView dating_detail_name_tv;

    private TextView dating_detail_distance_tv;

    private TextView dating_detail_age_tv;

    private ImageView dating_detail_vip_iv;

    private ImageView dating_detail_video_iv;

    private ImageView dating_detail_activity_talent_iv;

    private ImageView dating_detail_hot_iv;

    private LinearLayout dating_detail_type_info_layout;

    private ImageView dating_detail_datingtype_iv;

    private TextView dating_detail_title_tv;

    private ViewStub dating_detail_travel_base_info_vs;

    private ViewStub dating_detail_others_base_info_vs;

    private ViewStub dating_detail_time_distance_vs;

    private ViewStub dating_detail_travel_label_vs;

    private ViewStub dating_detail_signup_vs;

    private ViewStub dating_detail_married_info_vs;

    private LinearLayout dating_detail_status_pb_layout;
    private TextView dating_detail_improve_chengyidu_tv;

    private ProgressBar dating_detail_chengyi_pb;
    private ProgressBarView dating_detail_all_pbv;

    private TextView dating_detail_bottom_chat_tv;

    private LinearLayout dating_detail_hint_layout;

    private ImageView dating_detail_hint_close_iv;

    private TextView dating_detail_hint_shibie_tv;

    private View dating_bottom_cut_line;

    private LinearLayout dating_detail_bottom_ll;

    private LinearLayout dating_bottom_send_layout;

    private int loginUserid;

    private int userid;

    private String dating_id;

    private CropCircleTransformation circletransformation;
    private BitmapPool mPool;

    private Dialog loadingDiaog;

    private Dialog helpdialog;

    private boolean isPostchating = false;

    private AppointDetailDto appoindetaildto;

    private double lat = 0;
    private double lng = 0;

    private XRefreshView user_dating_detail_xrefreshview;

    private String userlistNextQuery;

    private String frompage = "";

    private int datingType;

    private int targetSex = 3;

    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooters;

    private ChatObserver mChatObserver;


    boolean isDatingDetailChat;

    boolean isDatingDetailDelectModify;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        loginUserid = LoginUser.getInstance().getUserId();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            dating_id = bundle.getString(YpSettings.DATINGS_ID);

            if (bundle.containsKey(YpSettings.DATINGS_TYPE)) {
                datingType = bundle.getInt(YpSettings.DATINGS_TYPE);
            }

            if (bundle.containsKey(YpSettings.USERID)) {
                userid = bundle.getInt(YpSettings.USERID);
            }

            if (bundle.containsKey(YpSettings.FROM_PAGE)) {
                frompage = bundle.getString(YpSettings.FROM_PAGE);
            }

        }

        mPool = Glide.get(this).getBitmapPool();
        circletransformation = new CropCircleTransformation(mPool);

        initComponent();
        setXrefreshListener();
        dating_detail_root_layout.setVisibility(View.GONE);
        getDatingDetail();

    }


    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        getBtnOption().setImageResource(R.drawable.option_more_icon);
        getBtnOption().setVisibility(View.VISIBLE);
        gettvOption().setVisibility(View.GONE);
        this.getOptionLayout().setVisibility(View.VISIBLE);

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);

                finish();
            }
        });

        this.getOptionLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);

                isDatingDetailDelectModify = true;

                if (loginUserid != userid) {
                    showOptionsDialog();
                } else {
                    showMyOptionsDialog();
                }
            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.act_dating_detail, null);


        dating_detail_root_layout = (RelativeLayout) contextView.findViewById(R.id.dating_detail_root_layout);

        dating_detail_userImg_iv = (ImageView) contextView.findViewById(R.id.dating_detail_userImg_iv);
        dating_detail_userImg_iv.setOnClickListener(this);

        dating_detail_name_tv = (TextView) contextView.findViewById(R.id.dating_detail_name_tv);

        dating_detail_distance_tv = (TextView) contextView.findViewById(R.id.dating_detail_distance_tv);

        dating_detail_age_tv = (TextView) contextView.findViewById(R.id.dating_detail_age_tv);

        dating_bottom_cut_line = contextView.findViewById(R.id.dating_bottom_cut_line);


        dating_detail_vip_iv = (ImageView) contextView.findViewById(R.id.dating_detail_vip_iv);

        dating_detail_video_iv = (ImageView) contextView.findViewById(R.id.dating_detail_video_iv);

        dating_detail_activity_talent_iv = (ImageView) contextView.findViewById(R.id.dating_detail_activity_talent_iv);


        dating_detail_hot_iv = (ImageView) contextView.findViewById(R.id.dating_detail_hot_iv);

        dating_detail_type_info_layout = (LinearLayout) contextView.findViewById(R.id.dating_detail_type_info_layout);

        dating_detail_datingtype_iv = (ImageView) contextView.findViewById(R.id.dating_detail_datingtype_iv);

        dating_detail_title_tv = (TextView) contextView.findViewById(R.id.dating_detail_title_tv);


        dating_detail_travel_base_info_vs = (ViewStub) contextView.findViewById(R.id.dating_detail_travel_base_info_vs);

        dating_detail_others_base_info_vs = (ViewStub) contextView.findViewById(R.id.dating_detail_others_base_info_vs);

        dating_detail_time_distance_vs = (ViewStub) contextView.findViewById(R.id.dating_detail_time_distance_vs);

        dating_detail_travel_label_vs = (ViewStub) contextView.findViewById(R.id.dating_detail_travel_label_vs);

        dating_detail_signup_vs = (ViewStub) contextView.findViewById(R.id.dating_detail_signup_vs);

        dating_detail_married_info_vs = (ViewStub) contextView.findViewById(R.id.dating_detail_married_info_vs);

        dating_detail_status_pb_layout = (LinearLayout) contextView.findViewById(R.id.dating_detail_status_pb_layout);
        dating_detail_improve_chengyidu_tv = (TextView) contextView.findViewById(R.id.dating_detail_improve_chengyidu_tv);
        dating_detail_improve_chengyidu_tv.setOnClickListener(this);

        dating_detail_chengyi_pb = (ProgressBar) contextView.findViewById(R.id.dating_detail_chengyi_pb);
        dating_detail_all_pbv = (ProgressBarView) contextView.findViewById(R.id.dating_detail_all_pbv);


        dating_detail_bottom_ll = (LinearLayout) contextView.findViewById(R.id.dating_detail_bottom_ll);

        dating_detail_bottom_chat_tv = (TextView) contextView.findViewById(R.id.dating_detail_bottom_chat_tv);
        dating_detail_bottom_chat_tv.setOnClickListener(this);

        dating_bottom_send_layout = (LinearLayout) contextView.findViewById(R.id.dating_bottom_send_layout);
        dating_bottom_send_layout.setOnClickListener(this);

        dating_detail_hint_layout = (LinearLayout) contextView.findViewById(R.id.dating_detail_hint_layout);
        dating_detail_hint_close_iv = (ImageView) contextView.findViewById(R.id.dating_detail_hint_close_iv);
        dating_detail_hint_close_iv.setOnClickListener(this);
        dating_detail_hint_shibie_tv = (TextView) contextView.findViewById(R.id.dating_detail_hint_shibie_tv);
        dating_detail_hint_shibie_tv.setOnClickListener(this);

        user_dating_detail_xrefreshview = (XRefreshView) contextView.findViewById(R.id.user_dating_detail_xrefreshview);

        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }


    private void setXrefreshListener() {


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        user_dating_detail_xrefreshview.setCustomHeaderView(mXRefreshViewHeaders);


        mXRefreshViewFooters = new XRefreshViewFooters(this);


        user_dating_detail_xrefreshview.setCustomFooterView(mXRefreshViewFooters);

        user_dating_detail_xrefreshview.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        user_dating_detail_xrefreshview.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        user_dating_detail_xrefreshview.setAutoLoadMore(true);

        user_dating_detail_xrefreshview.setPullRefreshEnable(false);


        mXRefreshViewFooters.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getDatingUserListMore();
                    }
                }, 1000);

            }
        });


        mXRefreshViewFooters.onAutoLoadMoreFail(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getDatingUserListMore();
                    }
                }, 1000);
            }
        });


        user_dating_detail_xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.e("下来刷新来了");

                    }
                }, 1500);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        getDatingUserListMore();
                    }
                }, 1000);
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("邀约详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
        setChatView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("邀约详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 获取约会详情
     */
    private void getDatingDetail() {

        loadingDiaog = DialogUtil.LoadingDialog(DatingDetailActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            lat = myLoc.getLoc().getLatitude();
            lng = myLoc.getLoc().getLongitude();
        }


        LatLng pt = new LatLng(lat, lng);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);
        DatingDetailBean datingDetailBean = new DatingDetailBean();
        datingDetailBean.setDatingId(dating_id);
        datingDetailBean.setLat(pt.latitude);
        datingDetailBean.setLng(pt.longitude);

        DatingDetailService datingDetailService = new DatingDetailService(this);
        datingDetailService.parameter(datingDetailBean);
        datingDetailService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();
                dating_detail_root_layout.setVisibility(View.VISIBLE);
                DatingDetailRespBean datingDetailRespBean = (DatingDetailRespBean) respBean;
                appoindetaildto = datingDetailRespBean.getResp();

                int datingStatus = appoindetaildto.getDatingStatus();

                if (datingStatus == 1) {

                    helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "", "该邀约已过期，无法与对方联系", "", "确定", datingStatusErrorBackCallListener);
                    if (!isFinishing()) {
                        helpdialog.show();
                    }

                } else if (datingStatus == 3) {

                    helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "", "该邀约已违规被系统删除，无法与对方联系", "", "确定", datingStatusErrorBackCallListener);
                    if (!isFinishing()) {
                        helpdialog.show();
                    }

                } else if (datingStatus == 4) {

                    helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "", "该邀约已被用户被删除，无法与对方联系", "", "确定", datingStatusErrorBackCallListener);
                    if (!isFinishing()) {
                        helpdialog.show();
                    }

                } else if (datingStatus == 2) {

                    helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "", "该邀约被系统正在审核，无法与对方联系", "", "确定", datingStatusErrorBackCallListener);
                    if (!isFinishing()) {
                        helpdialog.show();
                    }

                } else if (datingStatus == 0) {

                    datingType = appoindetaildto.getActivityType();
                    initViewDate(appoindetaildto);
                    userid = appoindetaildto.getOwner().getUserId();
                    if (loginUserid == userid) {
                        getDatingUserList();
                    }

                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();
                String msg = respBean.getMsg();
                LogUtils.e("邀约详情获取失败");
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, msg);
            }
        });

        datingDetailService.enqueue();

    }

    public String userName;
    public int userSex;
    public String userIconUrl;
    public String userHoroscope;
    public int userAge;

    /*
     * 设置数据
     */
    private void initViewDate(AppointDetailDto dto) {
        if (dto != null) {

            //是否查看自己邀约

            if (loginUserid == dto.getOwner().getUserId()) {

                dating_detail_status_pb_layout.setVisibility(View.GONE);
                dating_detail_distance_tv.setVisibility(View.GONE);

            } else {

                String location_str = CheckUtil.getSpacingTool(dto.getDistance());
                dating_detail_distance_tv.setVisibility(View.VISIBLE);
                dating_detail_distance_tv.setText(location_str);

                dating_detail_status_pb_layout.setVisibility(View.VISIBLE);


                if (dto.getOwner() != null) {
                    dating_detail_status_pb_layout.setVisibility(View.VISIBLE);

                    dating_detail_chengyi_pb.setProgress(dto.getOwner().getSincerity());

                    dating_detail_all_pbv.setProgress(dto.getOwner().getSincerity());
                    dating_detail_all_pbv.setProgressBar_max(100);
                    dating_detail_all_pbv.setPromptTextIsDisplayable(true);
                    dating_detail_all_pbv.setPromptTextCrompttext("诚意度");
                    dating_detail_all_pbv.setOutside_round_style(ProgressBarView.STROKE_FILL);

                } else {
                    dating_detail_status_pb_layout.setVisibility(View.GONE);
                }


                //通过视频认证不显示
                if (dto.getOwner().isVideoVerification()) {
                    dating_detail_hint_layout.setVisibility(View.GONE);
                } else {
                    dating_detail_hint_layout.setVisibility(View.VISIBLE);
                }

            }


            if (dto.getOwner().isVideoVerification()) {
                dating_detail_video_iv.setVisibility(View.VISIBLE);

            } else {
                dating_detail_video_iv.setVisibility(View.GONE);
            }

            switch (dto.getOwner().getCurrentUserPosition()) {
                case 0:
                    //不是VIP
                    dating_detail_vip_iv.setVisibility(View.GONE);
                    break;

                case 1:
                    //白银VIP
                    dating_detail_vip_iv.setVisibility(View.VISIBLE);
                    dating_detail_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_silver);
                    break;

                case 2:
                    //黄金VIP
                    dating_detail_vip_iv.setVisibility(View.VISIBLE);
                    dating_detail_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_gold);
                    break;

                case 3:
                    //铂金VIP
                    dating_detail_vip_iv.setVisibility(View.VISIBLE);
                    dating_detail_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                    break;

                case 4:
                    //钻石VIP
                    dating_detail_vip_iv.setVisibility(View.VISIBLE);
                    dating_detail_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                    break;

            }

            if (dto.getOwner().isActivityExpert()) {
                dating_detail_activity_talent_iv.setVisibility(View.VISIBLE);
            } else {
                dating_detail_activity_talent_iv.setVisibility(View.GONE);
            }


            if (!CheckUtil.isEmpty(dto.getOwner().getHeadImg())) {
                String imageurl = ImgUtils.DealImageUrl(dto.getOwner().getHeadImg(), 150, 150);
                Glide.with(this).load(imageurl).bitmapTransform(circletransformation).into(dating_detail_userImg_iv);
            }

            if (dto.getOwner().isHot()) {
                dating_detail_hot_iv.setVisibility(View.VISIBLE);
            } else {
                dating_detail_hot_iv.setVisibility(View.GONE);
            }

            if (!CheckUtil.isEmpty(dto.getOwner().getName())) {
                dating_detail_name_tv.setText(dto.getOwner().getName());
                userName = dto.getOwner().getName();
            }
            userHoroscope = CheckUtil.ConstellationMatching(dto.getOwner().getHoroscope());
            userSex = dto.getOwner().getSex();
            userIconUrl = dto.getOwner().getHeadImg();
            userAge = dto.getOwner().getAge();

            //性别：男
            if (dto.getOwner().getSex() == 1) {
                Drawable sexDrawable = getResources().getDrawable(R.drawable.ic_sex_man);
                sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
                dating_detail_age_tv.setCompoundDrawables(sexDrawable, null, null, null);
                dating_detail_age_tv.setTextColor(this.getResources().getColor(R.color.color_8cd2ff));

                dating_bottom_send_layout.setVisibility(View.GONE);
//
                dating_bottom_cut_line.setVisibility(View.GONE);

            } else {
                Drawable sexDrawable = getResources().getDrawable(R.drawable.ic_sex_woman);
                sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
                dating_detail_age_tv.setCompoundDrawables(sexDrawable, null, null, null);
                dating_detail_age_tv.setTextColor(this.getResources().getColor(R.color.color_fe8cd9));

                dating_bottom_send_layout.setVisibility(View.VISIBLE);
//
                dating_bottom_cut_line.setVisibility(View.VISIBLE);
            }


            if (dto.getOwner().isBirthdayPrivacy() || dto.getOwner().getAge() == 0) {
                dating_detail_age_tv.setText("-");
            } else {
                dating_detail_age_tv.setText(dto.getOwner().getAge() + "");
            }


            switch (dto.getActivityType()) {

                case Constant.APPOINT_TYPE_OTHERS:
                    dating_detail_datingtype_iv.setBackgroundResource(R.drawable.appoint_publish_other);
                    targetSex = dto.getOther().getTargetSex();
                    initOthersView(dto);
                    dating_detail_title_tv.setText(dto.getOther().getTheme());
                    this.getTvTitle().setText("邀约");
                    break;

                case Constant.APPOINT_TYPE_TRAVEL:
                    dating_detail_datingtype_iv.setBackgroundResource(R.drawable.appoint_publish_travel);
                    targetSex = InfoTransformUtils.getTravleTargetSex(dto.getTravel().getTargetObject());
                    initTravelView(dto.getTravel(), dto.getOwner().getSex());
                    this.getTvTitle().setText("约旅行");
                    break;

                case Constant.APPOINT_TYPE_MOVIE:

                    dating_detail_datingtype_iv.setBackgroundResource(R.drawable.appoint_publish_movice);

                    targetSex = dto.getMovie().getTargetSex();

                    initOthersView(dto);

                    if (dto.getOwner().getSex() != dto.getMovie().getTargetSex()) {
                        this.getTvTitle().setText("约人看电影");
                        dating_detail_title_tv.setText("约人看电影");
                    } else {
                        this.getTvTitle().setText("看电影");
                        dating_detail_title_tv.setText("看电影");
                    }

                    break;

                case Constant.APPOINT_TYPE_DOG:
                    initOthersView(dto);
                    targetSex = dto.getWalkTheDog().getTargetSex();
                    dating_detail_datingtype_iv.setBackgroundResource(R.drawable.appoint_publish_dog);
                    dating_detail_title_tv.setText(dto.getWalkTheDog().getDogType());
                    this.getTvTitle().setText("遛狗");
                    break;

                case Constant.APPOINT_TYPE_FITNESS:
                    initOthersView(dto);
                    targetSex = dto.getSports().getTargetSex();
                    dating_detail_datingtype_iv.setBackgroundResource(R.drawable.appoint_publish_fitness);
                    dating_detail_title_tv.setText(dto.getSports().getTheme());
                    this.getTvTitle().setText("约运动");

                    break;

                case Constant.APPOINT_TYPE_KTV:

                    dating_detail_datingtype_iv.setBackgroundResource(R.drawable.appoint_publish_ktv);
                    initOthersView(dto);
                    targetSex = dto.getSinging().getTargetSex();

                    if (dto.getOwner().getSex() != dto.getSinging().getTargetSex()) {
                        this.getTvTitle().setText("约人K歌");
                        dating_detail_title_tv.setText("约人K歌");
                    } else {
                        this.getTvTitle().setText("约K歌");
                        dating_detail_title_tv.setText("约K歌");
                    }

                    break;

                case Constant.APPOINT_TYPE_EAT:
                    dating_detail_datingtype_iv.setBackgroundResource(R.drawable.appoint_publish_eat);
                    initOthersView(dto);

                    targetSex = dto.getDine().getTargetSex();

                    if (dto.getOwner().getSex() != dto.getDine().getTargetSex()) {
                        this.getTvTitle().setText("约人吃饭");
                        dating_detail_title_tv.setText("约人吃饭");
                    } else {
                        this.getTvTitle().setText("吃美食");
                        dating_detail_title_tv.setText("吃美食");
                    }
                    break;

                case Constant.APPOINT_TYPE_MARRIED:
                    dating_detail_type_info_layout.setVisibility(View.GONE);
                    initMarriedView(dto.getMarriage());
                    targetSex = 0;
                    this.getTvTitle().setText("约定一生");
                    break;

            }

            setChatView();
        }
    }


    private void setChatView() {

        if (loginUserid != userid) {

            dating_detail_bottom_ll.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(dating_id)) {

                boolean isExist = DbHelperUtils.isExistChatRecordWithDating(loginUserid + "", userid + "", dating_id);

                if (isExist) {
                    dating_bottom_send_layout.setVisibility(View.GONE);
                    dating_bottom_cut_line.setVisibility(View.GONE);
                    dating_detail_bottom_chat_tv.setText("打开聊天");
                } else {
                    dating_detail_bottom_chat_tv.setText("先聊聊");
                }

            }


        } else {
            dating_detail_bottom_ll.setVisibility(View.GONE);
        }


    }

    private void initMarriedView(Marriage marriage) {

        dating_detail_travel_label_vs.setVisibility(View.GONE);
        dating_detail_others_base_info_vs.setVisibility(View.GONE);
        dating_detail_time_distance_vs.setVisibility(View.GONE);
        dating_detail_travel_base_info_vs.setVisibility(View.GONE);

        dating_detail_married_info_vs.setVisibility(View.VISIBLE);

        ImageView dating_detail_married_img_iv = (ImageView) this.findViewById(R.id.dating_detail_married_img_iv);

        TextView dating_detail_married_jiyu_tv = (TextView) this.findViewById(R.id.dating_detail_married_jiyu_tv);

        TextView dating_detail_married_time_tv = (TextView) this.findViewById(R.id.dating_detail_married_time_tv);

        TextView dating_detail_married_shuangqin_jianzai_tv = (TextView) this.findViewById(R.id.dating_detail_married_shuangqin_jianzai_tv);

        TextView dating_detail_married_dushengzinv_tv = (TextView) this.findViewById(R.id.dating_detail_married_dushengzinv_tv);

        TextView dating_detail_married_hujisuozai_tv = (TextView) this.findViewById(R.id.dating_detail_married_hujisuozai_tv);

        TextView dating_detail_married_xianjudi_tv = (TextView) this.findViewById(R.id.dating_detail_married_xianjudi_tv);

        TextView dating_detail_married_height_tv = (TextView) this.findViewById(R.id.dating_detail_married_height_tv);

        TextView dating_detail_married_weight_tv = (TextView) this.findViewById(R.id.dating_detail_married_weight_tv);

        TextView dating_detail_married_zhiye_tv = (TextView) this.findViewById(R.id.dating_detail_married_zhiye_tv);

        TextView dating_detail_married_xinzi_tv = (TextView) this.findViewById(R.id.dating_detail_married_xinzi_tv);

        TextView dating_detail_married_xueli_tv = (TextView) this.findViewById(R.id.dating_detail_married_xueli_tv);

        TextView dating_detail_married_lianai_jingli_tv = (TextView) this.findViewById(R.id.dating_detail_married_lianai_jingli_tv);

        TextView dating_detail_married_hunshi_tv = (TextView) this.findViewById(R.id.dating_detail_married_hunshi_tv);

        TextView dating_detail_married_zinv_tv = (TextView) this.findViewById(R.id.dating_detail_married_zinv_tv);

        TextView dating_detail_married_yinjiu_tv = (TextView) this.findViewById(R.id.dating_detail_married_yinjiu_tv);

        TextView dating_detail_married_jiankang_tv = (TextView) this.findViewById(R.id.dating_detail_married_jiankang_tv);

        TextView dating_detail_married_hunhou_zhufang_tv = (TextView) this.findViewById(R.id.dating_detail_married_hunhou_zhufang_tv);


        if (!CheckUtil.isEmpty(marriage.getPhotoUrl())) {
            dating_detail_married_img_iv.setVisibility(View.VISIBLE);
//            String imageurl = ImgUtils.DealImageUrl(marriage.getPhotoUrl(), 640, 640);
            Glide.with(this).load(marriage.getPhotoUrl()).into(dating_detail_married_img_iv);
        } else {
            dating_detail_married_img_iv.setVisibility(View.GONE);
        }

        if (!CheckUtil.isEmpty(marriage.getWish())) {
            dating_detail_married_jiyu_tv.setText(marriage.getWish());
        }

        String wishMarriageTime = InfoTransformUtils.getWishMarriageTime(marriage.getWishMarriageTime());
        dating_detail_married_time_tv.setText(wishMarriageTime);

        String parentsBeingAlive = InfoTransformUtils.getParentsBeingAlive(marriage.getParentsBeingAlive());
        dating_detail_married_shuangqin_jianzai_tv.setText(parentsBeingAlive);


        if (marriage.isOnlyChild()) {
            dating_detail_married_dushengzinv_tv.setText("是");
        } else {
            dating_detail_married_dushengzinv_tv.setText("否");
        }

        dating_detail_married_hujisuozai_tv.setText(marriage.getPermanentFirstArea() + " " + marriage.getPermanentSecondArea());

        dating_detail_married_xianjudi_tv.setText(marriage.getPresentFirstArea() + " " + marriage.getPresentSecondArea());

        dating_detail_married_height_tv.setText(marriage.getHeight() + "cm");

        dating_detail_married_weight_tv.setText(marriage.getWeight() + "kg");

        dating_detail_married_zhiye_tv.setText(marriage.getProfession());

        String income = InfoTransformUtils.getPersonalsIncome(marriage.getIncome());
        dating_detail_married_xinzi_tv.setText(income);

        String education = InfoTransformUtils.getEducation(marriage.getEducation());
        dating_detail_married_xueli_tv.setText(education);

        String loveHistory = InfoTransformUtils.getLoveHistory(marriage.getLoveHistory());
        dating_detail_married_lianai_jingli_tv.setText(loveHistory);

        if (marriage.isHasMarriageHistory()) {
            dating_detail_married_hunshi_tv.setText("有");
        } else {
            dating_detail_married_hunshi_tv.setText("无");
        }

        String childCondition = InfoTransformUtils.getChildrenCondition(marriage.getChildrenCondition());
        dating_detail_married_zinv_tv.setText(childCondition);


        String drinkStr = "";
        int[] drinkConditions = marriage.getDrinkConditions();
        if (drinkConditions != null && drinkConditions.length > 0) {
            for (int i = 0; i < drinkConditions.length; i++) {
                drinkStr = drinkStr + " " + InfoTransformUtils.getDrinkConditions(drinkConditions[i]);

            }
        }
        dating_detail_married_yinjiu_tv.setText(drinkStr);

        String healthCondition = InfoTransformUtils.getHealthCondition(marriage.getHealthCondition());
        dating_detail_married_jiankang_tv.setText(healthCondition);

        String marriedHouseCondition = InfoTransformUtils.getMarriedHouseCondition(marriage.getMarriedHouseCondition());
        dating_detail_married_hunhou_zhufang_tv.setText(marriedHouseCondition);


    }


    private void initTravelView(Travel travel, int sex) {

        dating_detail_others_base_info_vs.setVisibility(View.GONE);

        dating_detail_time_distance_vs.setVisibility(View.GONE);

        dating_detail_travel_base_info_vs.setVisibility(View.VISIBLE);


        TextView dating_detail_travel_content_tv = (TextView) this.findViewById(R.id.dating_detail_travel_content_tv);

        TextView dating_detail_travel_obj_tv = (TextView) this.findViewById(R.id.dating_detail_travel_obj_tv);

        ImageView dating_detail_travel_obj_iv = (ImageView) this.findViewById(R.id.dating_detail_travel_obj_iv);

        TextView dating_detail_travel_time_tv = (TextView) this.findViewById(R.id.dating_detail_travel_time_tv);

        TextView dating_detail_travel_plan_time_tv = (TextView) this.findViewById(R.id.dating_detail_travel_plan_time_tv);

        TextView dating_detail_travel_trip_mode_tv = (TextView) this.findViewById(R.id.dating_detail_travel_trip_mode_tv);

        TextView dating_detail_travel_cost_tv = (TextView) this.findViewById(R.id.dating_detail_travel_cost_tv);

        ImageView dating_detail_travel_img_iv = (ImageView) this.findViewById(R.id.dating_detail_travel_img_iv);


        String title = "";
        if (!CheckUtil.isEmpty(travel.getAddress())) {
            title = travel.getAddress() + "  ";
        }

        String[] meaningTags = travel.getMeaningTags();
        if (meaningTags != null && meaningTags.length > 0) {
            for (int i = 0; i < meaningTags.length; i++) {
                if (i == meaningTags.length - 1) {
                    title = title + meaningTags[i];
                } else {
                    title = title + meaningTags[i] + ",";
                }

            }
        }

        if (CheckUtil.isEmpty(title)) {
            title = "约人旅行";
        }

        dating_detail_title_tv.setText(title);

        if (!CheckUtil.isEmpty(travel.getPhotoUrl())) {
            dating_detail_travel_img_iv.setVisibility(View.VISIBLE);
//            String imageurl = ImgUtils.DealImageUrl(travel.getPhotoUrl(), 640, 640);
            Glide.with(this).load(travel.getPhotoUrl()).into(dating_detail_travel_img_iv);
        } else {
            dating_detail_travel_img_iv.setVisibility(View.GONE);
        }

        if (!CheckUtil.isEmpty(travel.getDescription())) {
            dating_detail_travel_content_tv.setText(travel.getDescription());
        }

        String method = InfoTransformUtils.getMethod(travel.getMethod());
        dating_detail_travel_trip_mode_tv.setText(method);

        String cost = InfoTransformUtils.getTravelCostType(travel.getTravelCostType(), sex);
        dating_detail_travel_cost_tv.setText(cost);

        String plantime = InfoTransformUtils.getPlanTime(travel.getPlanTime());
        dating_detail_travel_plan_time_tv.setText(plantime);

        String targetobj = InfoTransformUtils.getTargetObject(travel.getTargetObject());
        dating_detail_travel_obj_tv.setText(targetobj);

        switch (travel.getTargetObject()) {
            case Constant.TargetObject_Type_Tyrant_M:
                dating_detail_travel_obj_iv.setBackgroundResource(R.drawable.travel_obj_tuhaonan_icon);
                break;
            case Constant.TargetObject_Type_Overbearing_Chairman:
                dating_detail_travel_obj_iv.setBackgroundResource(R.drawable.travel_obj_ceo_icon);
                break;
            case Constant.TargetObject_Type_Rich_Handsome:
                dating_detail_travel_obj_iv.setBackgroundResource(R.drawable.travel_obj_gaofushuai_icon);
                break;
            case Constant.TargetObject_Type_Female_Temperament:
                dating_detail_travel_obj_iv.setBackgroundResource(R.drawable.travel_obj_qizhinv_icon);
                break;
            case Constant.TargetObject_Type_Silly_Sweet_White:
                dating_detail_travel_obj_iv.setBackgroundResource(R.drawable.travel_obj_shabaitian_icon);
                break;
            case Constant.TargetObject_Type_Phu_Bai:
                dating_detail_travel_obj_iv.setBackgroundResource(R.drawable.travel_obj_baifumei_icon);
                break;
        }


        if (travel.getWishTags() != null && travel.getWishTags().length > 0) {
            dating_detail_travel_label_vs.inflate();
            FlowCenterLayout dating_detail_travel_label_flow = (FlowCenterLayout) this.findViewById(R.id.dating_detail_travel_label_flow);
            initTravelLableViews(travel.getWishTags(), dating_detail_travel_label_flow);
        }

        if (travel.getMeetingTravelTimeType() == Constant.Travel_Time_Type_Specific_Date) {
            long time = ISO8601.getTime(travel.getMeetingTime());
            dating_detail_travel_time_tv.setText(TimeUtil.getDateFormatString(time, System.currentTimeMillis()));
        } else {
            String menttime = InfoTransformUtils.getMeetingTravelTimeType(travel.getMeetingTravelTimeType());
            dating_detail_travel_time_tv.setText(menttime);
        }


    }


    private void initTravelLableViews(String[] lable, FlowCenterLayout dating_detail_travel_label_flow) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 12;
        lp.rightMargin = 12;
        lp.topMargin = 12;
        lp.bottomMargin = 12;

        for (int i = 0; i < lable.length; i++) {
            TextView view = new TextView(this);
            String lable_str = lable[i];
            view.setText(lable_str);
            view.setPadding(0, 5, 0, 5);
            view.setTextSize(13);
            view.setTextColor(getResources().getColor(R.color.color_b2b2b2));
            view.setBackgroundResource(R.drawable.dating_travel_lable_show_bg);
            dating_detail_travel_label_flow.addView(view, lp);
            dating_detail_travel_label_flow.requestLayout();
        }
    }


    private void initOthersView(AppointDetailDto dto) {

        dating_detail_travel_base_info_vs.setVisibility(View.GONE);
        dating_detail_travel_label_vs.setVisibility(View.GONE);

        dating_detail_others_base_info_vs.setVisibility(View.VISIBLE);
        dating_detail_time_distance_vs.setVisibility(View.VISIBLE);


        LinearLayout dating_detail_others_title_layout = (LinearLayout) this.findViewById(R.id.dating_detail_others_title_layout);
        TextView dating_detail_others_title_tv = (TextView) this.findViewById(R.id.dating_detail_others_title_tv);

        LinearLayout dating_detail_others_content_layout = (LinearLayout) this.findViewById(R.id.dating_detail_others_content_layout);
        TextView dating_detail_others_content_tv = (TextView) this.findViewById(R.id.dating_detail_others_content_tv);

        LinearLayout dating_detail_others_obj_layout = (LinearLayout) this.findViewById(R.id.dating_detail_others_obj_layout);
        TextView dating_detail_others_obj_tv = (TextView) this.findViewById(R.id.dating_detail_others_obj_tv);

        LinearLayout dating_detail_others_cost_layout = (LinearLayout) this.findViewById(R.id.dating_detail_others_cost_layout);
        TextView dating_detail_others_cost_tv = (TextView) this.findViewById(R.id.dating_detail_others_cost_tv);

        LinearLayout dating_detail_others_pickup_layout = (LinearLayout) this.findViewById(R.id.dating_detail_others_pickup_layout);
        TextView dating_detail_others_pickup_tv = (TextView) this.findViewById(R.id.dating_detail_others_pickup_tv);

        LinearLayout dating_detail_others_friends_layout = (LinearLayout) this.findViewById(R.id.dating_detail_others_friends_layout);
        TextView dating_detail_others_friends_tv = (TextView) this.findViewById(R.id.dating_detail_others_friends_tv);

        ImageView dating_detail_others_img_iv = (ImageView) this.findViewById(R.id.dating_detail_others_img_iv);

        TextView dating_detail_time_tv = (TextView) this.findViewById(R.id.dating_detail_time_tv);

        TextView dating_detail_address_tv = (TextView) this.findViewById(R.id.dating_detail_address_tv);

        String imageurl = "";
        int meetingTimeType = 0;
        String meetingTime = "";
        String sexstr = "";
        String description = "";
        String carry = "";
        String companionCondition = "";

        String address = "";

        switch (dto.getActivityType()) {

            case Constant.APPOINT_TYPE_OTHERS:
                imageurl = dto.getOther().getPhotoUrl();
                dating_detail_others_title_layout.setVisibility(View.GONE);
                dating_detail_others_cost_layout.setVisibility(View.GONE);
                meetingTimeType = dto.getOther().getMeetingTimeType();
                meetingTime = dto.getOther().getMeetingTime();

                sexstr = InfoTransformUtils.getTargetSex(dto.getOther().getTargetSex());

                description = dto.getOther().getDescription();

                carry = InfoTransformUtils.getCarry(dto.getOther().getCarry());
                companionCondition = InfoTransformUtils.getDatingDetailCompanionCondition(dto.getOther().getCompanionCondition(), dto.getOwner().getSex());

                address = dto.getOther().getAddress();

                break;


            case Constant.APPOINT_TYPE_MOVIE:
                imageurl = dto.getMovie().getPhotoUrl();

                dating_detail_others_title_tv.setText(dto.getMovie().getName());

                String costStr = InfoTransformUtils.getCostType(dto.getMovie().getCostType());
                dating_detail_others_cost_tv.setText(costStr);

                meetingTimeType = dto.getMovie().getMeetingTimeType();
                meetingTime = dto.getMovie().getMeetingTime();

                sexstr = InfoTransformUtils.getTargetSex(dto.getMovie().getTargetSex());
                description = dto.getMovie().getDescription();

                carry = InfoTransformUtils.getCarry(dto.getMovie().getCarry());

                address = dto.getMovie().getAddress();

                break;


            case Constant.APPOINT_TYPE_DOG:

                imageurl = dto.getWalkTheDog().getPhotoUrl();
                dating_detail_others_title_layout.setVisibility(View.GONE);
                dating_detail_others_cost_layout.setVisibility(View.GONE);

                meetingTimeType = dto.getWalkTheDog().getMeetingTimeType();
                meetingTime = dto.getWalkTheDog().getMeetingTime();
                sexstr = InfoTransformUtils.getTargetSex(dto.getWalkTheDog().getTargetSex());

                description = dto.getWalkTheDog().getDescription();

                carry = InfoTransformUtils.getCarry(dto.getWalkTheDog().getCarry());
                companionCondition = InfoTransformUtils.getDatingDetailCompanionCondition(dto.getWalkTheDog().getCompanionCondition(), dto.getOwner().getSex());

                address = dto.getWalkTheDog().getAddress();

                break;


            case Constant.APPOINT_TYPE_FITNESS:

                imageurl = dto.getSports().getPhotoUrl();

                dating_detail_others_title_layout.setVisibility(View.GONE);

                String sportscostStr = InfoTransformUtils.getCostType(dto.getSports().getCostType());
                dating_detail_others_cost_tv.setText(sportscostStr);

                meetingTimeType = dto.getSports().getMeetingTimeType();
                meetingTime = dto.getSports().getMeetingTime();

                sexstr = InfoTransformUtils.getTargetSex(dto.getSports().getTargetSex());

                description = dto.getSports().getDescription();

                carry = InfoTransformUtils.getCarry(dto.getSports().getCarry());
                companionCondition = InfoTransformUtils.getDatingDetailCompanionCondition(dto.getSports().getCompanionCondition(), dto.getOwner().getSex());

                address = dto.getSports().getAddress();

                break;

            case Constant.APPOINT_TYPE_KTV:
                imageurl = dto.getSinging().getPhotoUrl();

                dating_detail_others_title_layout.setVisibility(View.GONE);

                String singingcostStr = InfoTransformUtils.getCostType(dto.getSinging().getCostType());
                dating_detail_others_cost_tv.setText(singingcostStr);

                meetingTimeType = dto.getSinging().getMeetingTimeType();
                meetingTime = dto.getSinging().getMeetingTime();
                sexstr = InfoTransformUtils.getTargetSex(dto.getSinging().getTargetSex());

                description = dto.getSinging().getDescription();
                carry = InfoTransformUtils.getCarry(dto.getSinging().getCarry());
                companionCondition = InfoTransformUtils.getDatingDetailCompanionCondition(dto.getSinging().getCompanionCondition(), dto.getOwner().getSex());
                address = dto.getSinging().getAddress();

                break;

            case Constant.APPOINT_TYPE_EAT:
                imageurl = dto.getDine().getPhotoUrl();
                dating_detail_others_title_layout.setVisibility(View.GONE);

                String dinecostStr = InfoTransformUtils.getCostType(dto.getDine().getCostType());
                dating_detail_others_cost_tv.setText(dinecostStr);

                meetingTimeType = dto.getDine().getMeetingTimeType();
                meetingTime = dto.getDine().getMeetingTime();
                sexstr = InfoTransformUtils.getTargetSex(dto.getDine().getTargetSex());

                description = dto.getDine().getDescription();

                carry = InfoTransformUtils.getCarry(dto.getDine().getCarry());
                companionCondition = InfoTransformUtils.getDatingDetailCompanionCondition(dto.getDine().getCompanionCondition(), dto.getOwner().getSex());

                address = dto.getDine().getAddress();

                break;
        }

        if (!CheckUtil.isEmpty(imageurl)) {
            LogUtils.e("imageUrl=" + imageurl);
            dating_detail_others_img_iv.setVisibility(View.VISIBLE);
//            imageurl = ImgUtils.DealImageUrl(imageurl, 640, 640);
            Glide.with(this).load(imageurl).into(dating_detail_others_img_iv);
        } else {
            dating_detail_others_img_iv.setVisibility(View.GONE);
        }


        if (!CheckUtil.isEmpty(description)) {
            dating_detail_others_content_layout.setVisibility(View.VISIBLE);
            dating_detail_others_content_tv.setText(description);
        } else {
            dating_detail_others_content_layout.setVisibility(View.GONE);
        }

        if (!CheckUtil.isEmpty(carry)) {
            dating_detail_others_pickup_layout.setVisibility(View.VISIBLE);
            dating_detail_others_pickup_tv.setText(carry);
        } else {
            dating_detail_others_pickup_layout.setVisibility(View.GONE);
        }

        if (!CheckUtil.isEmpty(companionCondition)) {
            dating_detail_others_friends_layout.setVisibility(View.VISIBLE);
            dating_detail_others_friends_tv.setText(companionCondition);
        } else {
            dating_detail_others_friends_layout.setVisibility(View.GONE);
        }


        dating_detail_others_obj_tv.setText(sexstr);


        if (meetingTimeType == Constant.MeetingTime_Type_Select_Time) {
            long time = ISO8601.getTime(meetingTime);
            dating_detail_time_tv.setText(TimeUtil.getDateFormatString(time, System.currentTimeMillis()));
        } else {
            String menttime = InfoTransformUtils.getMeetingTime(meetingTimeType);
            dating_detail_time_tv.setText(menttime);
        }


        dating_detail_address_tv.setText(address);

    }


    private DatingDetailSignUpAdapter userListAdapter;

    private void initSignUpView(List<AppointOwner> list) {

        if (list != null && list.size() > 0) {

            dating_detail_signup_vs.inflate();

            TextView dating_detail_signup_count_tv = (TextView) this.findViewById(R.id.dating_detail_signup_count_tv);

            dating_detail_signup_count_tv.setText("共" + list.size() + "人参与");

            MiListView dating_detail_signup_recyclerview = (MiListView) this.findViewById(R.id.dating_detail_signup_recyclerview);

            userListAdapter = new DatingDetailSignUpAdapter(DatingDetailActivity.this, list);
            dating_detail_signup_recyclerview.setAdapter(userListAdapter);
            userListAdapter.notifyDataSetChanged();

        }

    }


    private void checkSendGift() {

        Subscription subscription = HttpFactory.getHttpApi().getGiftStatusWithDating(dating_id, userid)


                .compose(SchedulersCompat.applyIoSchedulers())

                .compose(RxResultHelper.handleResult())

                .subscribe(dto -> {

                    //结果（0：成功 1：需要上传头像 2：对方拒绝接收非视频认证用户消息 3：拒绝签收礼物）

                    String msgStr = dto.msg;

                    if (dto.result == 1) {
                        msgStr = "需要上传头像";
                    } else if (dto.result == 2) {
                        msgStr = "对方拒绝接收非视频认证用户消息";
                    } else if (dto.result == 3) {
                        msgStr = "拒绝签收礼物";
                    }

                    if (dto.result == 1 || dto.result == 2 || dto.result == 3) {

                        helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "", msgStr, "", "确定", new BackCallListener() {
                            @Override
                            public void onCancel(View view, Object... obj) {
                                helpdialog.dismiss();

                            }

                            @Override
                            public void onEnsure(View view, Object... obj) {
                                helpdialog.dismiss();
                            }
                        });


                        helpdialog.show();

                        return;
                    }


                    Bundle b = new Bundle();
                    b.putInt(YpSettings.USERID, userid);
                    b.putString(YpSettings.USER_NAME, userName);
                    b.putString(YpSettings.USER_HOROSCOPE, userHoroscope);
                    b.putInt(YpSettings.USER_SEX, userSex);
                    b.putInt(YpSettings.USER_AGE, userAge);
                    b.putString(YpSettings.DATINGS_ID, dating_id);
                    b.putString(YpSettings.USER_ICON, userIconUrl);
                    ActivityUtil.jump(DatingDetailActivity.this, GiftActivity.class, b, 0, 100);


                }, throwable -> {


                    ApiResp apiResp = ErrorHanding.handle(throwable);


                    if (apiResp == null) {


                        DialogUtil.showDisCoverNetToast(DatingDetailActivity.this);
                    } else {

                        DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, apiResp.getMsg());

                    }


                });

        addhttpSubscrebe(subscription);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.dating_detail_userImg_iv:

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.USERID, userid);
                ActivityUtil.jump(DatingDetailActivity.this, UserInfoActivity.class, bundle, 0, 100);

                break;
            case R.id.dating_bottom_send_layout:


                if (targetSex != 0) {

                    int loginSex = DbHelperUtils.getDbUserSex(loginUserid);
                    if (loginSex == targetSex) {

                        checkSendGift();

                    } else {
                        DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, "与邀约要求性别不符");
                    }
                } else {
                    checkSendGift();
                }


                break;

            case R.id.dating_detail_bottom_chat_tv:

                if (appoindetaildto != null) {

                    isDatingDetailChat = true;

                    boolean isExist = DbHelperUtils.isExistChatRecordWithDating(loginUserid + "", userid + "", dating_id);

                    if (!isExist) {

                        if (targetSex != 0) {

                            int loginSex = DbHelperUtils.getDbUserSex(loginUserid);

                            if (loginSex == targetSex) {

                                if (!isPostchating) {
                                    //
                                    isPostchating = true;
                                    loadingDiaog = DialogUtil.LoadingDialog(DatingDetailActivity.this, null);
                                    if (!isFinishing()) {
                                        loadingDiaog.show();
                                    }
                                    postChatRequest();
                                }
                            } else {
                                DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, "与邀约要求性别不符");
                            }

                        } else {

                            if (!isPostchating) {
                                //
                                isPostchating = true;
                                loadingDiaog = DialogUtil.LoadingDialog(DatingDetailActivity.this, null);
                                if (!isFinishing()) {
                                    loadingDiaog.show();
                                }
                                postChatRequest();
                            }
                        }

                    } else {

                        Bundle bd = new Bundle();

                        bd.putInt(YpSettings.USERID, userid);

                        bd.putString(YpSettings.DATINGS_ID, dating_id);

                        ActivityUtil.jump(DatingDetailActivity.this, ChatActivity.class, bd, 0, 100);
                    }


                }

                break;
            case R.id.dating_detail_improve_chengyidu_tv:

                Bundle bd = new Bundle();

                bd.putString(YpSettings.BUNDLE_KEY_WEB_URL, "about/Intimacy");
                bd.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);
                bd.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "诚意度");
                ActivityUtil.jump(DatingDetailActivity.this, SimpleWebViewActivity.class, bd, 0, 100);

                break;

            case R.id.dating_detail_hint_close_iv:

                dating_detail_hint_layout.setVisibility(View.GONE);

                break;

            case R.id.dating_detail_hint_shibie_tv:

                Bundle bdS = new Bundle();
                bdS.putString(YpSettings.BUNDLE_KEY_WEB_URL, "about/SafetyTips");
                bdS.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);
                bdS.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "安全识别");
                ActivityUtil.jump(DatingDetailActivity.this, SimpleWebViewActivity.class, bdS, 0, 100);

                break;


        }
    }


    /**
     * 聊天
     */
    private void postChatRequest() {

        ChatDatingUserAttamptBean attamptBean = new ChatDatingUserAttamptBean();
        attamptBean.setTargetUserId(userid);
        attamptBean.setDatingId(dating_id);

        ChatDatingUserAttamptService attamptService = new ChatDatingUserAttamptService(this);
        attamptService.parameter(attamptBean);
        attamptService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();
                isPostchating = false;

                ChatDatingUserAttamptRespBean attamptRespBean = (ChatDatingUserAttamptRespBean) respBean;
                AttamptRespDto dto = attamptRespBean.getResp();
                chatPostResultHint(dto);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                isPostchating = false;

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this);
                } else {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, msg);
                }
            }
        });
        attamptService.enqueue();
    }


    /**
     * 聊天结果判断显示提示dialog
     *
     * @param dto
     */
    private void chatPostResultHint(AttamptRespDto dto) {

        if (dto != null) {
            if (dto.getViewStatus() == 0) {
                if (!CheckUtil.isEmpty(dto.getMessage())) {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, dto.getMessage());
                } else {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, "无法发起聊天");
                }
            } else if (dto.getViewStatus() == 1) {

                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.USERID, userid);

                bundle.putString(YpSettings.DATINGS_ID, dating_id);

                if (!CheckUtil.isEmpty(dto.getSendMsg())) {

                    mChatObserver = new ChatObserver(userid + "", TIMConversationType.C2C);

                    long date = System.currentTimeMillis();
                    TextMsg textmsg = new TextMsg(MessageType.Text, dto.getSendMsg(), 0, dating_id, 0);

                    String msg_str = JsonUtils.toJson(textmsg);

                    AttributeDto attributeDto = new AttributeDto();
                    attributeDto.setMask(0);
                    attributeDto.setDateid(dating_id);
                    attributeDto.setCounsel(0);
                    attributeDto.setType(MessageType.Text);

                    String attributeDto_str = JsonUtils.toJson(attributeDto);

                    ImMessage message = new TextMessage(dto.getSendMsg(), attributeDto_str);

                    String TIMMessageStr = JsonUtils.toJson(message.getMessage());

                    String msgid = message.getMessage().getMsgId();

                    ChatUtils.SaveOrUpdateChatMsgToDB(userid + "", msg_str, date, ChatDto.s_type, ChatDto.readed_status, msgid, ChatDto.succeed, dating_id, 0, TIMMessageStr);
                    ChatUtils.saveMessageRecord(msg_str, userid + "", ChatDto.succeed, ChatDto.s_type, date, 0, dating_id, TIMMessageStr);

                    mChatObserver.sendMessage(message.getMessage(), null);

                }

                ActivityUtil.jump(DatingDetailActivity.this, ChatActivity.class, bundle, 0, 100);


                RxBus.get().post("");

            } else if (dto.getViewStatus() == 2) {
                //头像不通过 帮助 取消
                helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "", dto.getMessage(), "查看帮助", "立即上传", helpAndUploadBackCallListener);
                if (!isFinishing()) {
                    helpdialog.show();
                }
            } else if (dto.getViewStatus() == 3) {
                // 对方拒绝接收非视频认证用户消息
                helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "", dto.getMessage(), "取消", "立即认证", verficationBackCallListener);
                if (!isFinishing()) {
                    helpdialog.show();
                }

            } else if (dto.getViewStatus() == 6) {
                //确定
                helpdialog = DialogUtil.createHotHintDialog(DatingDetailActivity.this, hotbackCallListener);
                if (!isFinishing()) {
                    helpdialog.show();
                }
            } else if (dto.getViewStatus() == 7) {

                DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, "该用户已被系统封禁，无法与他联系");
            }
        }
    }


    private BackCallListener datingStatusErrorBackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
            finish();
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
        }
    };


    //帮助以及上传头像 dialog回调
    private BackCallListener helpAndUploadBackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
            Bundle bundles = new Bundle();
            bundles.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
            ActivityUtil.jump(DatingDetailActivity.this, UserInfoActivity.class, bundles, 0, 100);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
            //跳转到web 查看帮助

            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Standard/AvatarAudit");
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "头像审核规范");
            bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

            ActivityUtil.jump(DatingDetailActivity.this, SimpleWebViewActivity.class, bundle, 0, 100);

        }
    };

    //视频认证 dialog回调
    private BackCallListener verficationBackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());
            ActivityUtil.jump(DatingDetailActivity.this, VideoDetailGetActivity.class, bundle, 0, 100);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
        }
    };


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

            int userPosition = DbHelperUtils.getOldVipPosition(loginUserid);

            bundle.putInt("userPosition", userPosition);


            if (0 == userPosition) {
                ActivityUtil.jump(DatingDetailActivity.this, VipOpenedActivity.class, bundle, 0, 100);
            } else {
                ActivityUtil.jump(DatingDetailActivity.this, VipRenewalsActivity.class, bundle, 0, 100);
            }


        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }

        }
    };


    private Dialog optionsDialog, reportDialog;

    public void showMyOptionsDialog() {
        // 初始化一个自定义的Dialog
        optionsDialog = new MyDialog(DatingDetailActivity.this, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);


                select_operate_dialog_title_tv.setText("更多操作");
                select_operate_dialog_one_tv.setText("删除邀约");

                if (datingType == Constant.APPOINT_TYPE_MARRIED) {
                    select_operate_dialog_two_tv.setText("修改邀约");
                    select_operate_dialog_two_layout.setVisibility(View.VISIBLE);
                } else {
                    select_operate_dialog_two_layout.setVisibility(View.GONE);
                }

                select_operate_dialog_one_layout.setVisibility(View.VISIBLE);

                select_operate_dialog_three_layout.setVisibility(View.GONE);

                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        optionsDialog.dismiss();

                        helpdialog = DialogUtil.createHintOperateDialog(DatingDetailActivity.this, "提示", "是否删除邀约?", "取消", "确认", doMyoptionsBackCallListener);
                        if (!isFinishing()) {
                            helpdialog.show();
                        }

                    }

                });

                select_operate_dialog_two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        optionsDialog.dismiss();

                        Bundle bundle = new Bundle();
                        bundle.putInt(YpSettings.APPOINTMENT_INTENT_YTPE, Constant.APPOINT_TYPE_MARRIED);
                        bundle.putString("datingId", dating_id);
                        bundle.putString(YpSettings.FROM_PAGE, "DatingDetailActivity");

                        Intent sendInt = new Intent(DatingDetailActivity.this, MarriageSeekingActivity.class);
                        sendInt.putExtras(bundle);

                        startActivityForResult(sendInt, YpSettings.DATINGS_TO_MARRIAGE);


                    }

                });


            }
        });
        optionsDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        optionsDialog.show();

    }


    public void showOptionsDialog() {
        // 初始化一个自定义的Dialog
        optionsDialog = new MyDialog(DatingDetailActivity.this, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                select_operate_dialog_title_tv.setText("更多操作");
                select_operate_dialog_one_tv.setText("举报");

                select_operate_dialog_one_layout.setVisibility(View.VISIBLE);
                select_operate_dialog_two_layout.setVisibility(View.GONE);
                select_operate_dialog_three_layout.setVisibility(View.GONE);

                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        optionsDialog.dismiss();

                        reportDialog = DialogUtil.createBubbleReportDialog(DatingDetailActivity.this, "举报原因", "骚扰信息", "广告欺诈", "不健康内容", new ReportMiBackCall());
                        if (!isFinishing()) {
                            reportDialog.show();
                        }

                    }

                });


            }
        });
        optionsDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        optionsDialog.show();

    }


    private BackCallListener doMyoptionsBackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
            loadingDiaog = DialogUtil.LoadingDialog(DatingDetailActivity.this, null);
            if (!isFinishing()) {
                loadingDiaog.show();
            }
            doCloseDatingRequest();
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                helpdialog.dismiss();
            }
        }
    };


    private class ReportMiBackCall extends BackCall {
        @Override
        public void deal(int which, Object... obj) {

            switch (which) {
                case R.id.select_operate_dialog_one_layout:

                    if (!isFinishing()) {
                        reportDialog.dismiss();
                    }
                    doReportRequest("骚扰信息");

                    break;
                case R.id.select_operate_dialog_two_layout:

                    if (!isFinishing()) {
                        reportDialog.dismiss();
                    }
                    doReportRequest("广告欺诈");

                    break;

                case R.id.select_operate_dialog_three_layout:

                    if (!isFinishing()) {
                        reportDialog.dismiss();
                    }
                    doReportRequest("不健康内容");

                    break;
            }
            super.deal(which, obj);
        }
    }


    /**
     * 举报
     */
    private void doReportRequest(String content) {

//		String url = "Type=2&Id=" + userID + "&Content=涉黄";
        loadingDiaog = DialogUtil.LoadingDialog(DatingDetailActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        BubblingReportBean reportBean = new BubblingReportBean();
        reportBean.setId(dating_id);
        reportBean.setType("Dating");
        reportBean.setContent(content);

        BubblingReportService reportService = new BubblingReportService(this);
        reportService.parameter(reportBean);
        reportService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();
                DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, "举报成功");


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {

                super.onFail(respBean);
                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, msg);
            }
        });
        reportService.enqueue();
    }


    private void doCloseDatingRequest() {

        DatingsCloseBean closeBean = new DatingsCloseBean();
        closeBean.setId(dating_id);

        DatingsCloseService closeService = new DatingsCloseService(this);
        closeService.parameter(closeBean);
        closeService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();
                DatingsCloseRespBean dto = (DatingsCloseRespBean) respBean;
                if (dto.isResp()) {

                    getUserInfo(LoginUser.getInstance().getUserId());

                    if (frompage.equals("UserAppointListActivity")) {

                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(2));
//                        EventBus.getDefault().post(new DatingsRefreshEvent(2));

                    } else if (frompage.equals("AppointmentFragment")) {

                        RxBus.get().post("DatingsRefreshEvent", new DatingsRefreshEvent(1));
//                        EventBus.getDefault().post(new DatingsRefreshEvent(1));
                    }

                    finish();

                } else {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, "删除失败");
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {

                super.onFail(respBean);
                loadingDiaog.dismiss();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, msg);
            }
        });
        closeService.enqueue();
    }


    private void getDatingUserList() {

        DatingUserListBean datingUserListBean = new DatingUserListBean();
        datingUserListBean.setDatingId(dating_id);


        DatingUserListService datingUserListService = new DatingUserListService(this);
        datingUserListService.parameter(datingUserListBean);
        datingUserListService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DatingUserListRespBean datingUserListRespBean = (DatingUserListRespBean) respBean;
                DatingUserListDto dto = datingUserListRespBean.getResp();

                userlistNextQuery = dto.getNextQuery();
                initSignUpView(dto.getList());


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

            }
        });

        datingUserListService.enqueue();

    }

    private void getDatingUserListMore() {

        if (CheckUtil.isEmpty(userlistNextQuery)) {

            mXRefreshViewFooters.setLoadcomplete(true);
            user_dating_detail_xrefreshview.stopLoadMore(false);

            return;
        }


        loadingDiaog = DialogUtil.LoadingDialog(DatingDetailActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        DatingUserListMoreBean datingUserListMoreBean = new DatingUserListMoreBean();
        datingUserListMoreBean.setNextQuery(userlistNextQuery);


        DatingUserListMoreService datingUserListMoreService = new DatingUserListMoreService(this);
        datingUserListMoreService.parameter(datingUserListMoreBean);
        datingUserListMoreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                DatingUserListRespBean datingUserListRespBean = (DatingUserListRespBean) respBean;
                DatingUserListDto dto = datingUserListRespBean.getResp();

                userlistNextQuery = dto.getNextQuery();
                if (userListAdapter != null) {
                    List<AppointOwner> list = userListAdapter.getDatas();
                    list.addAll(dto.getList());
                    userListAdapter.setData(list);
                    userListAdapter.notifyDataSetChanged();
                }

                if (TextUtils.isEmpty(userlistNextQuery)) {
                    mXRefreshViewFooters.setLoadcomplete(true);
                    user_dating_detail_xrefreshview.stopLoadMore(false);

                } else {
                    mXRefreshViewFooters.setLoadcomplete(false);
                    user_dating_detail_xrefreshview.stopLoadMore();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();

                mXRefreshViewFooters.setLoadcomplete(false);
                user_dating_detail_xrefreshview.stopLoadMore();

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(DatingDetailActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(DatingDetailActivity.this, msg);
            }
        });

        datingUserListMoreService.enqueue();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


            case YpSettings.DATINGS_TO_MARRIAGE:
                if (resultCode == YpSettings.DATINGS_TO_MARRIAGE) {
                    getDatingDetail();
                    if (loginUserid == userid) {
                        getDatingUserList();
                    }
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isDatingDetailDelectModify) {

            CommonItemEvent commonItemEvent = new CommonItemEvent();

            RxBus.get().post("DatingDetailDelectModify", commonItemEvent);
        }


        if (isDatingDetailChat) {

            CommonItemEvent commonItemEvent = new CommonItemEvent();

            RxBus.get().post("DatingDetailChat", commonItemEvent);

        }


        if (mChatObserver != null) {
            mChatObserver.stop();
        }

        unSubscribe();

    }




    CompositeSubscription mCompositeSubscription;

    private void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }


    private void addhttpSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }


}
