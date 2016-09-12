package cn.chono.yopper.activity.near;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.andview.refreshview.XScrollView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.util.LogUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BubbleInfo.BubbleInfoBean;
import cn.chono.yopper.Service.Http.BubbleInfo.BubbleInfoRespBean;
import cn.chono.yopper.Service.Http.BubbleInfo.BubbleInfoService;
import cn.chono.yopper.Service.Http.BubblingBubbleComments.BubblingBubbleCommentsBean;
import cn.chono.yopper.Service.Http.BubblingBubbleComments.BubblingBubbleCommentsRespBean;
import cn.chono.yopper.Service.Http.BubblingBubbleComments.BubblingBubbleCommentsService;
import cn.chono.yopper.Service.Http.BubblingBubbleCommentsList.BubblingBubbleCommentsListBean;
import cn.chono.yopper.Service.Http.BubblingBubbleCommentsList.BubblingBubbleCommentsListRespBean;
import cn.chono.yopper.Service.Http.BubblingBubbleCommentsList.BubblingBubbleCommentsListService;
import cn.chono.yopper.Service.Http.BubblingBubbleLikes.BubblingBubbleLikesBean;
import cn.chono.yopper.Service.Http.BubblingBubbleLikes.BubblingBubbleLikesRespBean;
import cn.chono.yopper.Service.Http.BubblingBubbleLikes.BubblingBubbleLikesService;
import cn.chono.yopper.Service.Http.BubblingBubblePraise.BubblingBubblePraiseBean;
import cn.chono.yopper.Service.Http.BubblingBubblePraise.BubblingBubblePraiseService;
import cn.chono.yopper.Service.Http.BubblingDelete.BubblingDeleteBean;
import cn.chono.yopper.Service.Http.BubblingDelete.BubblingDeleteRespBean;
import cn.chono.yopper.Service.Http.BubblingDelete.BubblingDeleteService;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportBean;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportRespBean;
import cn.chono.yopper.Service.Http.BubblingReport.BubblingReportService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.activity.usercenter.VipOpenedActivity;
import cn.chono.yopper.activity.video.VideoDetailGetActivity;
import cn.chono.yopper.adapter.BubblingExpresionAdapter;
import cn.chono.yopper.adapter.BubblingExpresionAdapter.OnItemExpresionClickLitener;
import cn.chono.yopper.adapter.BubblingPraiseIconAdapter;
import cn.chono.yopper.adapter.BubblingPraiseIconAdapter.OnItemClickLitener;
import cn.chono.yopper.adapter.DiscoverBubblingInfoIconAdapter;
import cn.chono.yopper.adapter.DiscoverBubblingInfoIconAdapter.OnIconItemClickLitener;
import cn.chono.yopper.adapter.EmoViewPagerAdapter;
import cn.chono.yopper.adapter.EmoteAdapter;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.baidu.overlay.BaiDuOverLay;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingEvaluateDto;
import cn.chono.yopper.data.BubblingEvaluateDto.User;
import cn.chono.yopper.data.BubblingList;
import cn.chono.yopper.data.BubblingPraiseDto;
import cn.chono.yopper.data.EvaluateBubbling;
import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.ZoomViewerDto;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.entity.likeBean.PrivateAlbumBody;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.ui.VideoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtils;
import cn.chono.yopper.utils.BackCall;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.FaceTextUtils;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;
import cn.chono.yopper.view.ResizeLayout;
import cn.chono.yopper.view.vewgroup.YPGridView;
import cn.chono.yopper.view.vewgroup.YPListView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BubblingInfoActivity extends MainFrameActivity implements
        OnClickListener, OnTouchListener, ResizeLayout.OnResizeListener,
        OnItemClickLitener, OnItemExpresionClickLitener, OnIconItemClickLitener {

    /**
     * 用户头像
     */
    public ImageView userIcon;

    public ImageView bubbing_vip_iv;

    /**
     * 名字
     */
    public TextView nameTv;
    /**
     * 星座
     */
    public TextView constellationTv;
    /**
     * 时间
     */
    public TextView tiemTv;
    /**
     * 内容
     */
    public TextView contentTv;
    /**
     * 图片
     */
    public YPGridView ypGridView;

    public RelativeLayout bubbling_detail_layout;
    public ImageView bubbling_detail_one_img_iv;
    public ImageView bubbling_detail_private_imageiv;
    public RelativeLayout bubbling_detail_video_layout;
    public ImageView bubbling_detail_video_coverimg_iv;
    public ImageView bubbling_detail_video_hint_iv;

    /**
     * 类型icon
     */

    public ImageView typeIcon;

    private LinearLayout typeLayout;
    /**
     * 类型
     */
    public TextView typeTv;
    /**
     * 位置
     */
    public TextView locationNumberTv;
    /**
     * 点赞
     */
    public TextView praiseNumberTv;
    /**
     * 评价
     */
    public TextView evaluateNumberTv;
    /**
     * 图片列表适配器
     */
    public DiscoverBubblingInfoIconAdapter iconAdapter;

    /**
     * 输入框
     */
    private EditText input_et;
    /**
     * 表情切换 but
     */
    private Button expresion_btn;
    /**
     * 发送but
     */
    private TextView send_btn;

    /**
     * 表情图片 布局
     */
    private LinearLayout more_face_layout;
    /**
     * 表情容器
     */
    private ViewPager more_face_view_pager;
    /**
     * 表情容器下面的布局
     */
    private LinearLayout more_face_type_layout;
    /**
     * 发送
     */
    private TextView more_face_send_layout;

    private boolean isopenFace = false;

    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;

    // 根布局
    private ResizeLayout root_layout;

    private InputHandler inputHandler = new InputHandler();

    private int curTat;// 当前的状态

    private int face_or_others;// 100时点击了表情按钮 200时点击了更多按钮 300时候listview

    private XScrollView srcollViewLayout;

    /**
     * 点赞icon
     */
    private ImageView info_praise_icon;
    /**
     * 点赞人icon
     */
    private YPGridView praise_grid;
    /**
     * 点赞人更多
     */
    private ImageView praise_but;
    /**
     * 没有点赞人
     */
    private TextView praise_no;
    /**
     * 没有评论
     */
    private TextView evaluate_no;
    /**
     * 评论列表
     */
    private YPListView evaluate_list;

    private boolean isLikes = false;

    private BubblingExpresionAdapter expresionAdapter;
    private BubblingPraiseIconAdapter praiseAdapter;

    private BubblingList bubblingList;
    private String bubblingID;
    private int position;

    private BitmapPool mPool;
    private Drawable praise_number_icon;
    private Drawable praise_icon_no;
    private CropCircleTransformation transformation;

    private XRefreshView xrefreshView;

    private int _rows = 20;
    private int _start = 0;
    private int _total;

    private double _latitude = 0.0;
    private double _longtitude = 0.0;

    private LinearLayout bubblingLayout;

    private int toUserID = -1;

    private Dialog bubbleOperateDialog;
    private Dialog bubbleReportDialog;

    private Dialog _deleteDialog;
    private Dialog _hintDialog;

    /**
     * 标记冒泡
     */
    private int HINT_REPORT_OK_TAG = 100;
    /**
     * 标记冒泡
     */
    private int HINT_DETELE_NO_TAG = 210;

    private RelativeLayout content_layout;
    private LinearLayout editLayout;

    private int _bibllingUserId;

    private LinearLayout error_no_data_remove_layout;// 已经删除
    private LinearLayout error_message_layout;// 错误


    private BaiDuOverLay baiDuOverLay;

    private BubblingList.Location location;

    private RelativeLayout input_et_layout;

    private int nettag;

    private Dialog loadingDiaog;


    private LinearLayout more_face_indicator;

    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.bubbling_info_activity);
        RxBus.get().register(this);
        PushAgent.getInstance(this).onAppStart();
        initView();
        setXrefreshViewListener();
        initEmoView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bubblingID = bundle.getString(YpSettings.BUBBLING_LIST_ID);
            position = bundle.getInt(YpSettings.BUBBLING_LIST_POSITION);
            if (bundle.containsKey(YpSettings.BUBBLING_LIST)) {
                bubblingList = (BubblingList) bundle.getSerializable(YpSettings.BUBBLING_LIST);
            }
        }

    }


    @Override
    public void onResume() {
        if (baiDuOverLay != null) {
            baiDuOverLay.onResume();
        }
        super.onResume();
        MobclickAgent.onPageStart("冒泡详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
        Loc.sendLocControlMessage(true);
        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            _latitude = myLoc.getLoc().getLatitude();
            _longtitude = myLoc.getLoc().getLongitude();
        }

        if (bubblingList == null) {
            error_message_layout.setVisibility(View.GONE);
            error_no_data_remove_layout.setVisibility(View.GONE);

            content_layout.setVisibility(View.GONE);
            editLayout.setVisibility(View.GONE);
            _start = 0;

            onRefreshBubbleInfo(bubblingID);
            praiseListHttp(bubblingID, 0, 6);
            evaluateListHttp(bubblingID, _start);

        } else {
            _bibllingUserId = bubblingList.getUser().getId();
            setViewData(bubblingList);
            error_message_layout.setVisibility(View.GONE);
            error_no_data_remove_layout.setVisibility(View.GONE);

            content_layout.setVisibility(View.VISIBLE);
            editLayout.setVisibility(View.VISIBLE);
            _start = 0;
            onRefreshBubbleInfo(bubblingID);
            praiseListHttp(bubblingID, 0, 6);
            evaluateListHttp(bubblingID, _start);
        }

    }

    @Override
    public void onPause() {
        if (baiDuOverLay != null) {
            baiDuOverLay.onPause();
        }
        super.onPause();
        MobclickAgent.onPageEnd("冒泡详情"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
        Loc.sendLocControlMessage(false);
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        xrefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        root_layout = (ResizeLayout) findViewById(R.id.root_layout);
        srcollViewLayout = (XScrollView) findViewById(R.id.srcollViewLayout);
        userIcon = (ImageView) findViewById(R.id.userIcon);
        bubbing_vip_iv = (ImageView) findViewById(R.id.bubbing_vip_iv);

        nameTv = (TextView) findViewById(R.id.nameTv);
        constellationTv = (TextView) findViewById(R.id.constellationTv);
        tiemTv = (TextView) findViewById(R.id.tiemTv);
        contentTv = (TextView) findViewById(R.id.contentTv);
        ypGridView = (YPGridView) findViewById(R.id.ypGridView);

        bubbling_detail_layout = (RelativeLayout) findViewById(R.id.bubbing_layout);
        bubbling_detail_one_img_iv = (ImageView) findViewById(R.id.bubbling_one_img_iv);
        bubbling_detail_private_imageiv = (ImageView) findViewById(R.id.bubbling_one_private_img_iv);

        bubbling_detail_video_layout = (RelativeLayout) findViewById(R.id.bubbling_video_layout);
        bubbling_detail_video_coverimg_iv = (ImageView) findViewById(R.id.bubbling_video_coverimg_iv);
        bubbling_detail_video_hint_iv = (ImageView) findViewById(R.id.bubbling_video_hint_iv);

        int weight = (int) (UnitUtil.getScreenWidthPixels(this) * 0.46);
        ViewGroup.LayoutParams imgpara = bubbling_detail_one_img_iv.getLayoutParams();
        imgpara.height = weight;
        imgpara.width = weight;
        bubbling_detail_one_img_iv.setLayoutParams(imgpara);
        bubbling_detail_private_imageiv.setLayoutParams(imgpara);

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) bubbling_detail_video_layout.getLayoutParams();
        linearParams.height = weight;
        linearParams.width = weight;
        bubbling_detail_video_layout.setLayoutParams(linearParams);

        typeIcon = (ImageView) findViewById(R.id.typeIcon);
        typeTv = (TextView) findViewById(R.id.typeTv);
        typeLayout = (LinearLayout) findViewById(R.id.typeLayout);
        locationNumberTv = (TextView) findViewById(R.id.locationNumberTv);
        praiseNumberTv = (TextView) findViewById(R.id.praiseNumberTv);
        evaluateNumberTv = (TextView) findViewById(R.id.evaluateNumberTv);
        iconAdapter = new DiscoverBubblingInfoIconAdapter(BubblingInfoActivity.this);
        iconAdapter.setOnIconItemClickLitener(this);

        ypGridView.setAdapter(iconAdapter);
        input_et = (EditText) findViewById(R.id.input_et);

        expresion_btn = (Button) findViewById(R.id.expresion_btn);
        send_btn = (TextView) findViewById(R.id.send_btn);

        more_face_layout = (LinearLayout) findViewById(R.id.more_face_layout);
        more_face_view_pager = (ViewPager) findViewById(R.id.more_face_view_pager);

        more_face_indicator = (LinearLayout) findViewById(R.id.more_face_indicator);
        more_face_type_layout = (LinearLayout) findViewById(R.id.more_face_type_layout);
        more_face_send_layout = (TextView) findViewById(R.id.more_face_send_layout);

        info_praise_icon = (ImageView) findViewById(R.id.info_praise_icon);
        praise_grid = (YPGridView) findViewById(R.id.praise_grid);
        praise_but = (ImageView) findViewById(R.id.praise_but);
        praise_no = (TextView) findViewById(R.id.praise_no);
        evaluate_no = (TextView) findViewById(R.id.evaluate_no);
        evaluate_list = (YPListView) findViewById(R.id.evaluate_list);

        input_et_layout = (RelativeLayout) findViewById(R.id.input_et_layout);

        more_face_layout.setVisibility(View.GONE);
        more_face_layout.setTag(100);
        curTat = BIGGER;// 界面控制在了刚进入什么都是隐藏的状态，故这个值可以这么给初始化

        getTvTitle().setText("冒泡详情");
        getBtnOption().setImageResource(R.drawable.option_more_icon);
        getBtnOption().setVisibility(View.VISIBLE);
        gettvOption().setVisibility(View.GONE);
        getOptionLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftInputViewLayout();
                int userID = LoginUser.getInstance().getUserId();
                if (userID != bubblingList.getUser().getId()) {
                    bubbleOperateDialog = DialogUtil.createOperateDialog(BubblingInfoActivity.this, "更多操作", "举报", "删除", "", false, true, true, new OperateMiBackCall());
                } else {
                    bubbleOperateDialog = DialogUtil.createOperateDialog(BubblingInfoActivity.this, "更多操作", "举报", "删除", "", false, false, true, new OperateMiBackCall());
                }
                if (!isFinishing()) {
                    bubbleOperateDialog.show();
                }

            }
        });
        getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftInputViewLayout();
                finish();
            }
        });

        praiseAdapter = new BubblingPraiseIconAdapter(this);
        praiseAdapter.setOnItemClickLitener(this);
        praiseAdapter.setData(new ArrayList<BubblingPraiseDto.BubblingPraise>());
        praise_grid.setAdapter(praiseAdapter);

        expresionAdapter = new BubblingExpresionAdapter(this);
        expresionAdapter.setOnItemClickLitener(this);
        evaluate_list.setAdapter(expresionAdapter);

        mPool = Glide.get(this).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);

        praise_number_icon = this.getResources().getDrawable(R.drawable.praise_number_icon);
        praise_icon_no = this.getResources().getDrawable(R.drawable.praise_icon_no);

        content_layout = (RelativeLayout) findViewById(R.id.content_layout);
        editLayout = (LinearLayout) findViewById(R.id.editLayout);

        error_no_data_remove_layout = (LinearLayout) findViewById(R.id.error_no_data_remove_layout);
        error_message_layout = (LinearLayout) findViewById(R.id.error_message_layout);

        userIcon.setOnClickListener(this);
        praiseNumberTv.setOnClickListener(this);
        evaluateNumberTv.setOnClickListener(this);
        expresion_btn.setOnClickListener(this);
        praise_but.setOnClickListener(this);
        info_praise_icon.setOnClickListener(this);
        send_btn.setOnClickListener(this);
        more_face_send_layout.setOnClickListener(this);
        typeLayout.setOnClickListener(this);

        input_et.setOnTouchListener(this);
        input_et_layout.setOnTouchListener(this);
        srcollViewLayout.setOnTouchListener(this);
        content_layout.setOnTouchListener(this);

        root_layout.setOnResizeListener(this);

    }

    /**
     * 下拉上拉组件初始化以及事件
     */
    private void setXrefreshViewListener() {

        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshViewFooter = new XRefreshViewFooters(this);


        xrefreshView.setCustomFooterView(mXRefreshViewFooter);

        xrefreshView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xrefreshView.setMoveForHorizontal(true);

        //滑动到底部自动加载更多
        xrefreshView.setAutoLoadMore(true);

        xrefreshView.setPullRefreshEnable(false);


        mXRefreshViewFooter.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        evaluateListHttp(bubblingID, _start);
                    }
                }, 1000);

            }
        });


        mXRefreshViewFooter.onAutoLoadMoreFail(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        evaluateListHttp(bubblingID, _start);
                    }
                }, 1000);
            }
        });


        xrefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xrefreshView.stopRefresh();

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                hideSoftInputView();

                new Handler().postDelayed(new Runnable() {

                    public void run() {

                        evaluateListHttp(bubblingID, _start);
                    }
                }, 1000);
            }
        });
    }

    /**
     * 删除冒泡
     */
    private void initDeleteDialog() {
        if (_deleteDialog == null) {
            _deleteDialog = DialogUtil.createHintOperateDialog(BubblingInfoActivity.this, "", "确认删除冒泡？", "否", "是", deletebackCallListener);
            _deleteDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

        }
    }


    @SuppressWarnings({"unchecked"})
    private void setViewData(final BubblingList bubblingList) {
        if (bubblingList == null) {
            return;
        }

        switch (bubblingList.getUser().getCurrentUserPosition()) {
            case 0:
                //不是VIP
                bubbing_vip_iv.setVisibility(View.GONE);
                break;

            case 1:
                //白银VIP
                bubbing_vip_iv.setVisibility(View.VISIBLE);
                bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_silver);
                break;

            case 2:
                //黄金VIP
                bubbing_vip_iv.setVisibility(View.VISIBLE);
                bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_gold);
                break;

            case 3:
                //铂金VIP
                bubbing_vip_iv.setVisibility(View.VISIBLE);
                bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                break;

            case 4:
                //钻石VIP
                bubbing_vip_iv.setVisibility(View.VISIBLE);
                bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                break;

        }


        String uresUrl = bubblingList.getUser().getHeadImg();
        if (!CheckUtil.isEmpty(uresUrl)) {
            String uresIcoUrl = ImgUtils.DealImageUrl(uresUrl, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
            Glide.with(this).load(uresIcoUrl).bitmapTransform(transformation).error(R.drawable.error_user_icon).into(userIcon);
        }
        if (!CheckUtil.isEmpty(bubblingList.getUser().getName())) {
            nameTv.setText(bubblingList.getUser().getName());
        } else {
            nameTv.setText("未知");
        }
        int sex = bubblingList.getUser().getSex();

        if (sex == 1) {
            Drawable sexDrawable = this.getResources().getDrawable(R.drawable.ic_sex_man);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            constellationTv.setCompoundDrawables(sexDrawable, null, null, null);
            constellationTv.setTextColor(this.getResources().getColor(R.color.color_8cd2ff));
        } else if (sex == 2) {
            Drawable sexDrawable = this.getResources().getDrawable(R.drawable.ic_sex_woman);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            constellationTv.setCompoundDrawables(sexDrawable, null, null, null);
            constellationTv.setTextColor(this.getResources().getColor(R.color.color_fe8cd9));
        }

        long time = ISO8601.getTime(bubblingList.getCreateTime());
        String timeStr = TimeUtil.normalTimeFormat(time);
        tiemTv.setText(timeStr);
        constellationTv.setText(CheckUtil.ConstellationMatching(bubblingList.getUser().getHoroscope()));

        if (!CheckUtil.isEmpty(bubblingList.getText())) {
            contentTv.setVisibility(View.VISIBLE);
            if (bubblingList.getSource() == 0) {
                contentTv.setTextColor(this.getResources().getColor(R.color.color_000000));
            } else if (bubblingList.getSource() == 1) {
                contentTv.setTextColor(this.getResources().getColor(R.color.color_999999));
            }
            contentTv.setText(bubblingList.getText());
        } else {
            contentTv.setVisibility(View.GONE);
        }

        if (bubblingList.getType() == 0 || bubblingList.getType() == 2) {
            if (bubblingList.getImageUrls().size() == 1) {
                ypGridView.setVisibility(View.GONE);
                bubbling_detail_layout.setVisibility(View.VISIBLE);
                bubbling_detail_one_img_iv.setVisibility(View.VISIBLE);
                bubbling_detail_video_layout.setVisibility(View.GONE);
                String imgurl = bubblingList.getImageUrls().get(0);
                if (!CheckUtil.isEmpty(imgurl)) {
                    String dealimgurl = ImgUtils.DealImageUrl(imgurl, YpSettings.IMG_SIZE_300, YpSettings.IMG_SIZE_300);
                    Glide.with(this).load(dealimgurl).centerCrop().error(R.drawable.error_default_icon).into(bubbling_detail_one_img_iv);

                    Logger.e("是否是加密照片：：" + bubblingList.isUnlockPrivateImage());
                    if (bubblingList.isUnlockPrivateImage()) {//false 加密  true 不加密
                        bubbling_detail_private_imageiv.setVisibility(View.GONE);
                    } else {
                        bubbling_detail_private_imageiv.setVisibility(View.VISIBLE);
                    }
                } else {
                    bubbling_detail_one_img_iv.setVisibility(View.GONE);
                    bubbling_detail_private_imageiv.setVisibility(View.GONE);
                }

                bubbling_detail_private_imageiv.setOnClickListener(v -> {
                    RxBus.get().post("bubblinginfolock", true);
                });

                bubbling_detail_one_img_iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ZoomViewerDto sq = new ZoomViewerDto();

                        sq.list = bubblingList.getImageUrls();

                        sq.position = 0;


                        Bundle bundle = new Bundle();
                        bundle.putSerializable(YpSettings.ZOOM_LIST_DTO, sq);
                        ActivityUtil.jump(BubblingInfoActivity.this, ZoomViewerActivity.class, bundle, 0, 200);
                    }
                });

            } else {
                ypGridView.setVisibility(View.VISIBLE);
                bubbling_detail_one_img_iv.setVisibility(View.GONE);
                bubbling_detail_video_layout.setVisibility(View.GONE);

                iconAdapter.setData(bubblingList);
                iconAdapter.notifyDataSetChanged();
            }

        } else if (bubblingList.getType() == 1 || bubblingList.getType() == 3) {
            ypGridView.setVisibility(View.GONE);
            bubbling_detail_one_img_iv.setVisibility(View.GONE);
            bubbling_detail_video_layout.setVisibility(View.VISIBLE);
            bubbling_detail_video_coverimg_iv.setVisibility(View.VISIBLE);
            bubbling_detail_video_hint_iv.setVisibility(View.VISIBLE);

            String imgurl = bubblingList.getImageUrls().get(0);
            if (!CheckUtil.isEmpty(imgurl)) {

                String dealimgurl = ImgUtils.DealVideoImageUrl(imgurl, YpSettings.IMG_SIZE_300, YpSettings.IMG_SIZE_300);

                Glide.with(this).load(dealimgurl).centerCrop().error(R.drawable.error_default_icon).into(bubbling_detail_video_coverimg_iv);


                bubbling_detail_video_hint_iv.setBackgroundResource(R.drawable.video_play_icon);

            } else {
                bubbling_detail_video_layout.setVisibility(View.GONE);
            }

            final int userid = bubblingList.getUser().getId();

            bubbling_detail_video_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.e(" Bubblinginfo ====video ===" + bubblingList.getType());
                    if (bubblingList.getType() == 1) {

                        Bundle bundle = new Bundle();

                        bundle.putInt(YpSettings.USERID, userid);

                        ActivityUtil.jump(BubblingInfoActivity.this, VideoDetailGetActivity.class, bundle, 0, 100);

                    } else if (bubblingList.getType() == 3) {//形象视频

                        checkLookVideo(userid, bubblingList.getVideoId());
                    }
                }
            });
        }

        location = bubblingList.getLocation();
        if (location != null) {

            String typeUrl = location.getTypeImgUrl();
            if (!CheckUtil.isEmpty(typeUrl) && !CheckUtil.isEmpty(location.getName())) {
                String typeIcoUrl = ImgUtils.DealImageUrl(typeUrl, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
                Integer locationID = location.getId();
                if (locationID == null || locationID <= 0) {
                    typeIcon.setBackgroundResource(R.drawable.discover_type_icon_no);
                    Glide.with(this).load(typeIcoUrl).into(typeIcon);
                } else {
                    typeIcon.setBackgroundResource(R.drawable.discover_type_icon);
                    Glide.with(this).load(typeIcoUrl).bitmapTransform(transformation).into(typeIcon);
                }
                typeLayout.setVisibility(View.VISIBLE);
                typeTv.setText(location.getName() + "");
            } else {
                typeLayout.setVisibility(View.GONE);
            }

        } else {
            typeLayout.setVisibility(View.GONE);
        }

        locationNumberTv.setText(CheckUtil.getSpacingTool(bubblingList.getDistance()));
        praiseNumberTv.setText(bubblingList.getTotalLikes() + "");
        evaluateNumberTv.setText(bubblingList.getTotalComments() + "");
        final boolean isLiked = bubblingList.isLiked();
        setIsLiked(isLiked);

    }

    private void setIsLiked(boolean isLiked) {
        if (isLiked) {
            // / 这一步必须要做,否则不会显示.
            praise_number_icon.setBounds(0, 0, praise_number_icon.getMinimumWidth(), praise_number_icon.getMinimumHeight());
            praiseNumberTv.setCompoundDrawables(praise_number_icon, null, null, null);

        } else {
            // / 这一步必须要做,否则不会显示.
            praise_icon_no.setBounds(0, 0, praise_icon_no.getMinimumWidth(), praise_icon_no.getMinimumHeight());
            praiseNumberTv.setCompoundDrawables(praise_icon_no, null, null, null);
        }

    }

    /**
     * 获取指定冒泡详情
     *
     * @param BubbleId
     */
    private void onRefreshBubbleInfo(String BubbleId) {

        loadingDiaog = DialogUtil.LoadingDialog(BubblingInfoActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        LatLng pt = new LatLng(_latitude, _longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        BubbleInfoBean bubbleInfoBean = new BubbleInfoBean();
        bubbleInfoBean.setBubbleId(BubbleId);
        bubbleInfoBean.setLat(pt.latitude);
        bubbleInfoBean.setLng(pt.longitude);

        BubbleInfoService infoService = new BubbleInfoService(this);
        infoService.parameter(bubbleInfoBean);
        infoService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();
                BubbleInfoRespBean infoRespBean = (BubbleInfoRespBean) respBean;
                bubblingList = infoRespBean.getResp();

                // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                // 如果没有数据，则显示无数据视图，如果有数据则刷新视图

                if (bubblingList != null) {
                    error_message_layout.setVisibility(View.GONE);
                    error_no_data_remove_layout.setVisibility(View.GONE);

                    content_layout.setVisibility(View.VISIBLE);
                    editLayout.setVisibility(View.VISIBLE);

                    bubblingID = bubblingList.getId();
                    _bibllingUserId = bubblingList.getUser().getId();
                    setViewData(bubblingList);

                } else {
                    error_message_layout.setVisibility(View.GONE);
                    error_no_data_remove_layout.setVisibility(View.GONE);
                    content_layout.setVisibility(View.GONE);
                    editLayout.setVisibility(View.GONE);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();

                String statusCode = respBean.getStatus();
                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    error_message_layout.setVisibility(View.GONE);
                    error_no_data_remove_layout.setVisibility(View.VISIBLE);

                    content_layout.setVisibility(View.GONE);
                    editLayout.setVisibility(View.GONE);
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    error_message_layout.setVisibility(View.VISIBLE);
                    error_no_data_remove_layout.setVisibility(View.GONE);
                    content_layout.setVisibility(View.GONE);
                    editLayout.setVisibility(View.GONE);
                    return;
                }

                error_message_layout.setVisibility(View.GONE);
                error_no_data_remove_layout.setVisibility(View.GONE);
                content_layout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.VISIBLE);

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, msg);

            }
        });

        infoService.enqueue();


    }

    /**
     * 点赞请求
     *
     * @param islike
     * @param id
     */
    @SuppressWarnings("unused")
    private void onPraisehttp(boolean islike, String id) {


        BubblingBubblePraiseBean praiseBean = new BubblingBubblePraiseBean();
        praiseBean.setId(id);
        praiseBean.setLike(islike);

        BubblingBubblePraiseService praiseService = new BubblingBubblePraiseService(this);

        praiseService.parameter(praiseBean);
        praiseService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                praiseListHttp(bubblingID, 0, 6);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String statusCode = respBean.getStatus();

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "该冒泡不存在");
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "您访问的信息存在违规的内容");
                    return;
                }
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, msg);


            }
        });
        praiseService.enqueue();

    }

    /**
     * 点赞列表
     */
    private void praiseListHttp(String id, int start, int rows) {

        BubblingBubbleLikesBean likesBean = new BubblingBubbleLikesBean();

        likesBean.setId(id);
        likesBean.setStart(start);
        likesBean.setRows(rows);

        BubblingBubbleLikesService likesService = new BubblingBubbleLikesService(this);
        likesService.parameter(likesBean);
        likesService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BubblingBubbleLikesRespBean bubbleLikesRespBean = (BubblingBubbleLikesRespBean) respBean;
                BubblingPraiseDto bubblingPraiseDto = bubbleLikesRespBean.getResp();


                if (bubblingPraiseDto != null) {
                    int size = bubblingPraiseDto.getList().size();
                    if (size > 0) {

                        praiseAdapter.setData(bubblingPraiseDto.getList());
                        praiseAdapter.notifyDataSetChanged();

                        praise_no.setVisibility(View.GONE);
                        praise_grid.setVisibility(View.VISIBLE);
                    } else {
                        praiseAdapter.setData(new ArrayList<BubblingPraiseDto.BubblingPraise>());
                        praiseAdapter.notifyDataSetChanged();

                        praise_no.setVisibility(View.VISIBLE);
                        praise_grid.setVisibility(View.GONE);

                    }

                } else {
                    praiseAdapter.setData(new ArrayList<BubblingPraiseDto.BubblingPraise>());
                    praiseAdapter.notifyDataSetChanged();
                    praise_no.setVisibility(View.VISIBLE);
                    praise_grid.setVisibility(View.GONE);


                }

                // 请求成功后都要获取一个 adapter的长度
                int listSize = praiseAdapter.getDataSize();
                setPraiseButVisibility(listSize);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                // 请求成功后都要获取一个 adapter的长度
                int listSize = praiseAdapter.getDataSize();
                setPraiseButVisibility(listSize);

                String statusCode = respBean.getStatus();

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了

                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "该冒泡不存在");
                    return;

                } else if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "您访问的信息存在违规的内容");
                    return;

                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this);
                    return;
                }

                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, msg);


            }
        });
        likesService.enqueue();


    }

    /**
     * 设置点赞人更多按钮显示状态
     *
     * @param listSize
     */
    private void setPraiseButVisibility(int listSize) {
        LogUtils.e("listSize===========" + listSize);
        if (listSize > 0) {
            praise_but.setVisibility(View.VISIBLE);
        } else {
            praise_but.setVisibility(View.GONE);
        }

    }

    /**
     * 评论列表
     */
    private void evaluateListHttp(String id, int start) {

        if (_total < start) {
            mXRefreshViewFooter.setLoadcomplete(true);
            xrefreshView.setLoadComplete(false);
            return;
        }


        BubblingBubbleCommentsListBean commentsBean = new BubblingBubbleCommentsListBean();
        commentsBean.setId(id);
        commentsBean.setStart(start);
        commentsBean.setRows(_rows);
        BubblingBubbleCommentsListService commentsService = new BubblingBubbleCommentsListService(this);

        commentsService.parameter(commentsBean);

        commentsService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);


                BubblingBubbleCommentsListRespBean bubblingBubbleCommentsRespBean = (BubblingBubbleCommentsListRespBean) respBean;

                BubblingEvaluateDto bubblingEvaluateDto = bubblingBubbleCommentsRespBean.getResp();


                if (bubblingEvaluateDto != null) {
                    _rows = bubblingEvaluateDto.getRows();
                    _total = bubblingEvaluateDto.getTotal();
                    int size = bubblingEvaluateDto.getList().size();

                    if (_start == 0) {
                        _start = _start + size;
                        if (size > 0) {
                            expresionAdapter.setData(bubblingEvaluateDto.getList());
                            expresionAdapter.notifyDataSetChanged();
                            evaluate_list.setVisibility(View.VISIBLE);
                            evaluate_no.setVisibility(View.GONE);
                        } else {
                            evaluate_list.setVisibility(View.GONE);
                            evaluate_no.setVisibility(View.VISIBLE);
                        }

                    } else {
                        _start = _start + size;
                        expresionAdapter.addData(bubblingEvaluateDto.getList());
                        expresionAdapter.notifyDataSetChanged();
                    }
                }


                if (_start >= _total) {
                    mXRefreshViewFooter.setLoadcomplete(true);
                    xrefreshView.stopLoadMore(false);

                } else {
                    mXRefreshViewFooter.setLoadcomplete(false);
                    xrefreshView.stopLoadMore();
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String statusCode = respBean.getStatus();

                mXRefreshViewFooter.setLoadcomplete(false);
                xrefreshView.stopLoadMore(false);

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "该冒泡不存在");
                    return;
                }
                if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "您访问的信息存在违规的内容");
                    return;

                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, msg);

            }
        });

        commentsService.enqueue();


    }

    /**
     * 评论请求
     *
     * @param id
     */
    private void onEvaluatehttp(String id, String comment, int toUserID) {


        BubblingBubbleCommentsBean commentsBean = new BubblingBubbleCommentsBean();

        commentsBean.setId(id);
        commentsBean.setComment(comment);
        commentsBean.setToUserID(null);
        if (-1 != toUserID) {
            BubblingBubbleCommentsBean.ToUserID userID = new BubblingBubbleCommentsBean.ToUserID();
            userID.setToUserId(toUserID);
            commentsBean.setToUserID(userID);
        }

        BubblingBubbleCommentsService commentsService = new BubblingBubbleCommentsService(this);

        commentsService.parameter(commentsBean);
        commentsService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                BubblingBubbleCommentsRespBean commentsRespBean = (BubblingBubbleCommentsRespBean) respBean;

                EvaluateBubbling evaluate = commentsRespBean.getResp();
                if (evaluate != null) {
                    evaluatehttpHandler.sendEmptyMessage(1);
                } else {
                    evaluatehttpHandler.sendEmptyMessage(0);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String statusCode = respBean.getStatus();

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "该冒泡不存在");
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "您访问的信息存在违规的内容");
                    return;
                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, msg);
            }
        });
        commentsService.enqueue();

    }

    private Handler evaluatehttpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 评价失败后
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "评论失败");
                    break;
                case 1:// 评价成功后
                    _start = 0;
                    evaluateListHttp(bubblingID, _start);

                    onRefreshBubbleInfo(bubblingID);
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "评论成功");
                    input_et.setHint("输入评论内容");
                    break;

                default:
                    break;
            }

        }
    };

    /**
     * 举报请求
     *
     * @param bubblingID
     * @param content
     */
    private void doReportHttp(String bubblingID, String content) {
//        String url = HttpURL.bubbling_report + "?" + "Type=bubble" + "&Id="
//                + bubblingID + "&Content=" + content;


        BubblingReportBean reportBean = new BubblingReportBean();
        reportBean.setId(bubblingID);
        reportBean.setType("bubble");
        reportBean.setContent(content);


        BubblingReportService reportService = new BubblingReportService(this);
        reportService.parameter(reportBean);
        reportService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                BubblingReportRespBean reportRespBean = (BubblingReportRespBean) respBean;
                BubblingReportRespBean.Messages messages = reportRespBean.getResp();
                if (null != messages) {
                    String msg = messages.getMessage();

                    _hintDialog = DialogUtil.createHintOperateDialog(BubblingInfoActivity.this, "", msg, "", "确定", netbackCallListener);

                    nettag = HINT_REPORT_OK_TAG;

                    if (!isFinishing()) {
                        _hintDialog.show();
                    }
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String statusCode = respBean.getStatus();

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "该冒泡不存在");
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "您访问的信息存在违规的内容");
                    return;

                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, msg);
            }
        });
        reportService.enqueue();


    }

    /**
     * 删除自己的冒泡
     *
     * @param bubblingID
     */
    private void onDeleteHttp(String bubblingID) {


        BubblingDeleteBean deleteBean = new BubblingDeleteBean();
        deleteBean.setBubblingId(bubblingID);


        BubblingDeleteService deleteService = new BubblingDeleteService(this);
        deleteService.parameter(deleteBean);
        deleteService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BubblingDeleteRespBean deleteRespBean = (BubblingDeleteRespBean) respBean;

                boolean resp = deleteRespBean.isResp();

                if (resp) {
                    SharedprefUtil.saveBoolean(BubblingInfoActivity.this, YpSettings.BUBBLING_PUBLISH, true);// 保存为true，标记冒泡列表要刷新

                    finish();

                } else {
                    _hintDialog = DialogUtil.createHintOperateDialog(BubblingInfoActivity.this, "", "删除失败", "", "确定", netbackCallListener);
                    nettag = HINT_DETELE_NO_TAG;
                }
                if (!isFinishing()) {
                    _hintDialog.show();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String statusCode = respBean.getStatus();

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "该冒泡不存在");
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "您访问的信息存在违规的内容");
                    return;
                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, msg);
            }
        });
        deleteService.enqueue();


    }

    private BackCallListener deletebackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                onDeleteHttp(bubblingID);
                _deleteDialog.dismiss();
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                _deleteDialog.dismiss();
            }

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expresion_btn:// 表情切换 but

                input_et.requestFocus();


                if (isopenFace) {
                    more_face_layout.setVisibility(View.GONE);
                    isopenFace = false;
                    changeInput();
                    expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                    return;
                }

                isopenFace = false;

                more_face_layout.setTag(1000);
                face_or_others = 100;
                // 弹出照片 拍照 位置 语音 赠送P果等功能面板
                LogUtils.e("当前状态" + curTat);
                if (curTat == SMALLER) {// 软键盘显示了
                    changeInput();
                } else {
                    setMoreLayoutVisible(true);
                }

                break;
            case R.id.send_btn:// 发送but

                ViewsUtils.preventViewMultipleClick(v, 1000);

                String cmt = input_et.getText().toString().trim();
                LogUtils.e("-----cmt-----" + cmt);

                if (!TextUtils.isEmpty(cmt)) {
                    onEvaluatehttp(bubblingID, cmt, toUserID);
                    input_et.setText("");
                    hideSoftInputViewLayout();
                    SharedprefUtil.saveBoolean(BubblingInfoActivity.this, YpSettings.BUBBLING_PUBLISH, true);// 保存为true，标记冒泡列表要刷新
                }
                break;
            case R.id.more_face_send_layout:// 发送but

                ViewsUtils.preventViewMultipleClick(v, 1000);
                String comment = input_et.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)) {
                    onEvaluatehttp(bubblingID, comment, toUserID);
                    input_et.setText("");
                    hideSoftInputViewLayout();
                    SharedprefUtil.saveBoolean(BubblingInfoActivity.this, YpSettings.BUBBLING_PUBLISH, true);// 保存为true，标记冒泡列表要刷新
                }

                break;
            case R.id.praise_but:// 点赞人更多

                ViewsUtils.preventViewMultipleClick(v, 1000);
                hideSoftInputViewLayout();
                Bundle bundle = new Bundle();
                bundle.putString(YpSettings.BUBBLING_LIST_ID, bubblingID);
                ActivityUtil.jump(BubblingInfoActivity.this, BubblingPraiseListActivity.class, bundle, 0, 200);

                break;
            case R.id.praiseNumberTv:// 点赞

                ViewsUtils.preventViewMultipleClick(v, 1000);

                boolean islske = bubblingList.isLiked();
                int number = bubblingList.getTotalLikes();
                if (islske) {
                    isLikes = false;
                    number = number - 1;
                } else {
                    isLikes = true;
                    number = number + 1;
                }
                bubblingList.setLiked(isLikes);
                bubblingList.setTotalLikes(number);
                setIsLiked(isLikes);
                praiseNumberTv.setText(number + "");
                onPraisehttp(isLikes, bubblingID);
                SharedprefUtil.saveBoolean(BubblingInfoActivity.this, YpSettings.BUBBLING_PUBLISH, true);// 保存为true，标记冒泡列表要刷新

                break;

            case R.id.evaluateNumberTv:
                toUserID = -1;
                input_et.setText("");
                input_et.setHint("评论");
                break;

            case R.id.userIcon:// 冒泡人头像
                ViewsUtils.preventViewMultipleClick(v, 1000);
                hideSoftInputViewLayout();
                Bundle userbundle = new Bundle();
                userbundle.putInt(YpSettings.USERID, _bibllingUserId);

                ActivityUtil.jump(BubblingInfoActivity.this, UserInfoActivity.class, userbundle, 0, 200);

                break;

            case R.id.typeLayout:// 类型监听
                ViewsUtils.preventViewMultipleClick(v, 1000);
                hideSoftInputViewLayout();

                baiDuOverLay = new BaiDuOverLay(BubblingInfoActivity.this);
                baiDuOverLay.setBaiDuData(location);
                baiDuOverLay.showBaiDuOverLay();
                break;

            default:
                break;
        }

    }

    private class ReportMiBackCall extends BackCall {
        @Override
        public void deal(int which, Object... obj) {

            switch (which) {
                case R.id.select_operate_dialog_one_layout:

                    if (!isFinishing()) {
                        bubbleReportDialog.dismiss();
                    }
                    doReportHttp(bubblingID, "骚扰信息");

                    break;
                case R.id.select_operate_dialog_two_layout:

                    if (!isFinishing()) {
                        bubbleReportDialog.dismiss();
                    }
                    doReportHttp(bubblingID, "广告欺诈");

                    break;

                case R.id.select_operate_dialog_three_layout:

                    if (!isFinishing()) {
                        bubbleReportDialog.dismiss();
                    }
                    doReportHttp(bubblingID, "不健康内容");

                    break;

            }
            super.deal(which, obj);
        }
    }

    private class OperateMiBackCall extends BackCall {
        @Override
        public void deal(int which, Object... obj) {

            switch (which) {
                case R.id.select_operate_dialog_one_layout:


                    if (!isFinishing()) {
                        bubbleOperateDialog.dismiss();
                    }

                    bubbleReportDialog = DialogUtil.createBubbleReportDialog(BubblingInfoActivity.this, "举报原因", "骚扰信息", "广告欺诈", "不健康内容", new ReportMiBackCall());
                    if (!isFinishing()) {
                        bubbleReportDialog.show();
                    }

                    break;
                case R.id.select_operate_dialog_two_layout:

                    if (!isFinishing()) {
                        bubbleOperateDialog.dismiss();
                    }

                    int userID = LoginUser.getInstance().getUserId();

                    if (userID == bubblingList.getUser().getId()) {
                        initDeleteDialog();
                        _deleteDialog.show();
                    }

                    break;
            }
            super.deal(which, obj);
        }
    }

    private BackCallListener netbackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (nettag == HINT_REPORT_OK_TAG) {// 举报成功
                _hintDialog.dismiss();
            } else if (nettag == HINT_DETELE_NO_TAG) {
                _hintDialog.dismiss();
            } else {
                _hintDialog.dismiss();
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                _hintDialog.dismiss();
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.input_et:
                more_face_layout.setVisibility(View.GONE);

                break;
            case R.id.input_et_layout:
                more_face_layout.setVisibility(View.GONE);

                break;

            case R.id.srcollViewLayout:
                hideSoftInputViewLayout();
                break;
            case R.id.content_layout:
                hideSoftInputViewLayout();
                break;

            default:
                break;
        }

        return false;
    }

    private void hideSoftInputViewLayout() {
        hideSoftInputView();
        more_face_layout.setVisibility(View.GONE);

    }

    @Override
    public void OnResize(int w, int h, int oldw, int oldh) {
        int change = BIGGER;
        if (h < oldh) {
            change = SMALLER;
        }
        Message msg = new Message();
        msg.what = 1;
        msg.arg1 = change;
        inputHandler.sendMessage(msg);

    }

    private class InputHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_RESIZE: {

                    if (msg.arg1 == BIGGER) {
                        curTat = BIGGER;
                        setMoreLayoutVisible(true);
                    } else {
                        curTat = SMALLER;
                        setMoreLayoutVisible(false);

                    }
                }
                break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 反复切换软键盘
     */
    private void changeInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    List<String> emos;

    /**
     * 初始化表情布局
     *
     * @param
     * @return void
     * @throws
     * @Title: initEmoView
     */
    private void initEmoView() {

        emos = FaceTextUtils.emoList;

        List<View> views = new ArrayList<View>();
        for (int i = 0; i < 3; ++i) {
            views.add(getGridView(i));
        }
        initLayout();
        more_face_view_pager.setAdapter(new EmoViewPagerAdapter(views));
        more_face_view_pager.addOnPageChangeListener(new PageChageListener());

    }


    class PageChageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            // 更改指示器图片
            for (int i = 0; i < indicators.length; i++) {
                indicators[arg0].setBackgroundResource(R.drawable.near_detail_img_selected);
                if (arg0 != i) {
                    indicators[i].setBackgroundResource(R.drawable.near_detail_img_no_selected);
                }
            }

        }

    }

    private ImageView[] indicators = null;

    private LinearLayout.LayoutParams params;

    /**
     * 手机密度
     */
    private ImageView indicator_view;

    private void initLayout() {
        indicators = new ImageView[3]; // 定义指示器数组大小

        for (int i = 0; i < 3; i++) {

            int imagePadding = 10;
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(imagePadding, imagePadding, imagePadding, imagePadding);

            // 循环加入指示器
            indicator_view = new ImageView(this);
            indicator_view.setBackgroundResource(R.drawable.near_detail_img_no_selected);

            indicators[i] = indicator_view;
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.near_detail_img_selected);
            }
            more_face_indicator.addView(indicators[i], params);

        }
        more_face_indicator.setVisibility(View.VISIBLE);
    }

    private View getGridView(final int i) {
        View view = View.inflate(this, R.layout.include_emo_gridview, null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 0) {
            list.addAll(emos.subList(0, 18));
        } else if (i == 1) {
            list.addAll(emos.subList(18, 36));
        } else if (i == 2) {
            list.addAll(emos.subList(36, emos.size()));
        }
        final EmoteAdapter gridAdapter = new EmoteAdapter(BubblingInfoActivity.this, list);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                if (i == 0 || i == 1) {
                    if (position == 17) {
                        faceDeletefun();
                    } else {
                        inputFaceFun(position + i * 18);
                    }
                } else {
                    if (position == 5) {
                        faceDeletefun();
                    } else {
                        inputFaceFun(position + 36);
                    }
                }

            }
        });
        return view;
    }

    private void faceDeletefun() {

        int start = input_et.getSelectionStart();
        if (start > 0) {

            boolean no_face = true;
            String emo_str_one = "";
            String emo_str_two = "";
            String emo_str_three = "";
            String input_str = input_et.getText().toString();
            if (input_str.length() >= 4) {
                emo_str_one = input_str.substring(start - 4, start);
            }
            if (input_str.length() >= 3) {
                emo_str_two = input_str.substring(start - 3, start);
            }
            if (input_str.length() >= 5) {
                emo_str_three = input_str.substring(start - 5, start);
            }

            String face_str = "";
            for (int i = 0; i < FaceTextUtils.expression_text.length - 1; i++) {
                face_str = "[" + FaceTextUtils.expression_text[i].toString() + "]";
                if (emo_str_one.equals(face_str)) {
                    // String conent = input_str.substring(0, start - 5)
                    // + input_str.substring(start + 1);
                    input_et.getEditableText().delete(start - 4, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(BubblingInfoActivity.this, input_et.getText().toString());
                    input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    input_et.setSelection(start - 4);
                    no_face = false;
                    break;
                }

                if (emo_str_two.equals(face_str)) {
                    // String conent = input_str.substring(0, start - 4)
                    // + input_str.substring(start + 1);
                    input_et.getEditableText().delete(start - 3, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(BubblingInfoActivity.this, input_et.getText().toString());
                    input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    input_et.setSelection(start - 3);
                    no_face = false;
                    break;
                }
                if (emo_str_three.equals(face_str)) {
                    // String conent = input_str.substring(0, start - 4)
                    // + input_str.substring(start + 1);
                    input_et.getEditableText().delete(start - 5, start);
                    SpannableString spannableString = FaceTextUtils.toSpannableString(BubblingInfoActivity.this, input_et.getText().toString());
                    input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                    input_et.setSelection(start - 5);
                    no_face = false;
                    break;
                }
            }

            if (no_face) {
                input_et.getEditableText().delete(start - 1, start);
                SpannableString spannableString = FaceTextUtils.toSpannableString(BubblingInfoActivity.this, input_et.getText().toString());
                input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
                input_et.setSelection(start - 1);
            }

        }
    }

    private void inputFaceFun(int position) {
        String emo_Str = "[" + FaceTextUtils.expression_text[position] + "]";
        int start = input_et.getSelectionStart();
        int contentStrLength = input_et.getText().toString().length();
        int emo_StrLength = emo_Str.length();
        CharSequence content = null;
        if (contentStrLength + emo_StrLength >= 100) {
            return;
        } else {
            content = input_et.getText().insert(start, emo_Str);
        }
        SpannableString spannableString = FaceTextUtils.toSpannableString(
                BubblingInfoActivity.this, content.toString());
        input_et.setText(spannableString, TextView.BufferType.SPANNABLE);
        // chat_input_et.setText(content);
        // 定位光标位置
        CharSequence info = input_et.getText();
        int inputStrSize = input_et.getText().toString().length();
        if (info instanceof Spannable) {
            Spannable spanText = (Spannable) info;
            if (inputStrSize < 100) {
                Selection.setSelection(spanText, start + emo_Str.length());
            }

        }
    }

    private void setMoreLayoutVisible(boolean show) {
        if (null != more_face_layout) {
            int id = (Integer) more_face_layout.getTag();
            if (show && id == 1000) {
                if (face_or_others == 100) {
                    more_face_layout.setVisibility(View.VISIBLE);
                    expresion_btn.setBackgroundResource(R.drawable.chat_input_btn_bg);
                    isopenFace = true;

                } else {
                    more_face_layout.setVisibility(View.GONE);
                    expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                    isopenFace = false;
                }

            } else {
                more_face_layout.setVisibility(View.GONE);
                expresion_btn.setBackgroundResource(R.drawable.chat_expresion_btn_bg);
                isopenFace = false;
            }
        }
    }

    @Override
    public void onExpresionItemClick(View view, int position, User user) {// 评论列表点击事件

        ViewsUtils.preventViewMultipleClick(view, 1000);

        if (user == null) {
            return;
        }

        input_et.setText("");
        String userNmae = "回复" + user.getName();
        input_et.setHint(userNmae);
        toUserID = user.getId();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        input_et.requestFocus();

    }

    @Override
    public void onExpresionIconItemClick(View view, int position, int userID) {

        ViewsUtils.preventViewMultipleClick(view, 1000);

        hideSoftInputViewLayout();
        Bundle userbundle = new Bundle();
        userbundle.putInt(YpSettings.USERID, userID);
        ActivityUtil.jump(BubblingInfoActivity.this, UserInfoActivity.class, userbundle, 0, 200);

    }

    @Override
    public void onPraiseIconItemClick(View view, int position, int userID) {

        ViewsUtils.preventViewMultipleClick(view, 1000);
        hideSoftInputViewLayout();
        Bundle userbundle = new Bundle();
        userbundle.putInt(YpSettings.USERID, userID);
        ActivityUtil.jump(BubblingInfoActivity.this, UserInfoActivity.class, userbundle, 0, 200);

    }

    @Override
    public void onIconItemClick(View view, int position, List<String> list, int imgViewWidth, int imgViewHight) {

        hideSoftInputViewLayout();

        ViewsUtils.preventViewMultipleClick(view, 1000);

        ZoomViewerDto sq = new ZoomViewerDto();

        sq.list = list;

        sq.position = position;

        Bundle bundle = new Bundle();

        bundle.putSerializable(YpSettings.ZOOM_LIST_DTO, sq);

        ActivityUtil.jump(BubblingInfoActivity.this, ZoomViewerActivity.class, bundle, 0, 200);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("bubblinginfolock")

            }

    )
    public void onTopEvent(Object lock) {
        showUnlockDialog("解锁需要10个苹果，是否付出苹果查看(解锁成功当天内可以任意查看对方私密照片)", "取消", "查看", LoginUser.getInstance().getUserId(), bubblingList.getUser().getId(), position);
    }

    @Override
    public void onDestroy() {
        if (baiDuOverLay != null) {
            baiDuOverLay.onDestroy();
        }
        super.onDestroy();
        RxBus.get().unregister(this);
        unSubscribe();
    }

    MyDialog dialog;
    MyDialog buyAppleDialog;

    public void showUnlockDialog(String tvStr, String btn1, String btn2, int userId, int targetId, int position) {
        dialog = new MyDialog(this, R.style.MyDialog, R.layout.view_unlock_dialog, contentView -> {
            TextView remindTv = (TextView) contentView.findViewById(R.id.remind_tv);

            ImageView imageView = (ImageView) contentView.findViewById(R.id.remind_iv);

            imageView.setVisibility(View.GONE);

            Button sureBtn = (Button) contentView.findViewById(R.id.sure_btn);
            Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);

            remindTv.setText(tvStr);
            remindTv.setGravity(Gravity.CENTER);
            cancelBtn.setText(btn1);
            sureBtn.setText(btn2);

            sureBtn.setOnClickListener(v -> {

                if (!isFinishing()) {
                    loadingDiaog.show();
                }
                //left btn
                PrivateAlbumBody body = new PrivateAlbumBody();
                body.setUserId(userId);
                body.setLookedUserId(targetId);

                Subscription subscription = HttpFactory.getHttpApi().putUnlockAlbum(body)
                        .compose(SchedulersCompat.applyIoSchedulers())
                        .compose(RxResultHelper.handleResult())
                        .subscribe(lockAlbum -> {
                            RxBus.get().post("OnLock", targetId);
                            int status = lockAlbum.getResult();
                            if (status == 0) {
                                bubblingList.setUnlockPrivateImage(true);
                                iconAdapter.setData(bubblingList);
                                iconAdapter.notifyDataSetChanged();
                                bubbling_detail_private_imageiv.setVisibility(View.GONE);
                                DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, "解锁成功");
                            } else if (status == 1) {
                                showBuyDialog("苹果数量不足，请先购买苹果", "取消", "购买苹果", false);
                            }
                            loadingDiaog.dismiss();
                        }, throwable -> {
                            loadingDiaog.dismiss();
                            DialogUtil.showDisCoverNetToast(BubblingInfoActivity.this, ErrorHanding.handleError(throwable));
                        });
                addSubscrebe(subscription);
                dialog.dismiss();

            });

            cancelBtn.setOnClickListener(v -> {
                //right btn
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    public void showBuyDialog(String tvStr, String btn1, String btn2, boolean isVideo) {
        buyAppleDialog = new MyDialog(this, R.style.MyDialog, R.layout.view_unlock_dialog, contentView -> {
            TextView remindTv = (TextView) contentView.findViewById(R.id.remind_tv);

            ImageView imageView = (ImageView) contentView.findViewById(R.id.remind_iv);

            imageView.setVisibility(View.GONE);

            Button sureBtn = (Button) contentView.findViewById(R.id.sure_btn);
            Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);

            remindTv.setText(tvStr);
            remindTv.setGravity(Gravity.CENTER);
            cancelBtn.setText(btn1);
            sureBtn.setText(btn2);

            sureBtn.setOnClickListener(v -> {
                if (isVideo) {
                    Bundle bundle = new Bundle();

                    bundle.putInt("apple_count", 0);

                    bundle.putInt("userPosition", 0);

                    ActivityUtil.jump(BubblingInfoActivity.this, VipOpenedActivity.class, bundle, 0, 100);
                } else {
                    Bundle appleBundle = new Bundle();

                    appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);


                    ActivityUtil.jump(BubblingInfoActivity.this, AppleListActivity.class, appleBundle, 0, 100);
                }

                buyAppleDialog.dismiss();
            });

            cancelBtn.setOnClickListener(v -> {
                //right btn
                buyAppleDialog.dismiss();

            });
        });
        buyAppleDialog.show();
    }

    /**
     * 校验是否可以查看视频
     *
     * @param userId
     * @param videoid
     */
    public void checkLookVideo(int userId, int videoid) {
        Subscription subscription = HttpFactory.getHttpApi().getUnlockVideo(userId, videoid)
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(videoBean -> {
                    int status = videoBean.getResult();
                    if (status == 0) {
                        Logger.e("去查看视频咯");
                        GeneralVideos generalVideos = new GeneralVideos();

                        generalVideos.setCoverImgUrl(videoBean.getCoverImgUrl());

                        generalVideos.setVideoId(videoBean.getVideoId());

                        generalVideos.setVideoUrl(videoBean.getVideoUrl());


                        if (!TextUtils.isEmpty(generalVideos.getVideoUrl())) {

                            Bundle bundle = new Bundle();

                            bundle.putSerializable("Data", generalVideos);

                            bundle.putInt("position", 0);

                            bundle.putInt(YpSettings.USERID, userId);

                            bundle.putString("type", "DiscoverBubblingFragment");

                            AppUtils.jump(BubblingInfoActivity.this, VideoActivity.class, bundle);

                        }

                    } else if (status == 1) {
                        showBuyDialog(videoBean.getMsg(), "取消", "查看VIP", true);
                    }
                }, throwable -> {

                    DialogUtil.showDisCoverNetToast(this, ErrorHanding.handleError(throwable));

                });
        addSubscrebe(subscription);
    }

    protected CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
}