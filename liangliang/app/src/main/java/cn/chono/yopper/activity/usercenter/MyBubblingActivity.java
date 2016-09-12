package cn.chono.yopper.activity.usercenter;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BubbleLimit.BubbleLimitRespBean;
import cn.chono.yopper.Service.Http.BubbleLimit.BubbleLimitService;
import cn.chono.yopper.Service.Http.BubblingBubblePraise.BubblingBubblePraiseBean;
import cn.chono.yopper.Service.Http.BubblingBubblePraise.BubblingBubblePraiseService;
import cn.chono.yopper.Service.Http.BubblingList.BubblingListBean;
import cn.chono.yopper.Service.Http.BubblingList.BubblingListRespBean;
import cn.chono.yopper.Service.Http.BubblingList.BubblingListService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.activity.near.BubblingAddressActivity;
import cn.chono.yopper.activity.near.BubblingInfoActivity;
import cn.chono.yopper.activity.near.ZoomViewerActivity;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.activity.video.VideoDetailGetActivity;
import cn.chono.yopper.adapter.DiscoverBubblingAdapter;
import cn.chono.yopper.adapter.DiscoverBubblingAdapter.OnItemClickLitener;
import cn.chono.yopper.api.HttpFactory;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingDto;
import cn.chono.yopper.data.BubblingList;
import cn.chono.yopper.data.BubblingList.Location;
import cn.chono.yopper.data.ChatAttamptRespDto;
import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserBubblingLocalDto;
import cn.chono.yopper.data.ZoomViewerDto;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.entity.likeBean.PrivateAlbumBody;
import cn.chono.yopper.event.BubblingListEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.ui.VideoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtils;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 我的冒泡
 *
 * @author sam.sun
 */
public class MyBubblingActivity extends MainFrameActivity implements OnItemClickLitener {

    private XRefreshView xrefreshView;

    private RecyclerView mRecyclerView;
    private TextView my_bubble_title_tv;

    private LinearLayout my_bubble_option_layout;
    private LinearLayout my_bubble_back_layout;

    private DiscoverBubblingAdapter adapter;

    private double latitude;
    private double longtitude;
    private double radii = 5.0;

    private int total;

    private ViewStub my_bubble_error_location_vs;
    private ViewStub my_bubble_error_network_vs;
    private ViewStub my_bubble_error_no_data_vs;

    private int userID;

    private String Time = "";

    private boolean hasLocalData = false;

    private Dialog loadingDiaog;

    private Dialog limitdialog;

    private boolean isPostLimit = false;

    private int _start = 0;


    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bubbling_list_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userID = bundle.getInt(YpSettings.USERID, 0);
        }
        initView();
        setXrefreshViewListener();
        initData();

        RxBus.get().register(this);

        loadingDiaog = DialogUtil.LoadingDialog(MyBubblingActivity.this, null);

        if (!isFinishing()) {
            loadingDiaog.show();
        }
        _start = 0;
        onRefreshData(_start);

    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("我（ta）的冒泡列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        if (userID == LoginUser.getInstance().getUserId()) {
            my_bubble_title_tv.setText("我的冒泡");
        } else {
            my_bubble_title_tv.setText("TA的冒泡");
        }

        boolean isRefresh = SharedprefUtil.getBoolean(MyBubblingActivity.this, YpSettings.BUBBLING_PUBLISH, false);
        if (isRefresh) {
            _start = 0;
            onRefreshData(_start);
            SharedprefUtil.saveBoolean(MyBubblingActivity.this, YpSettings.BUBBLING_PUBLISH, false);// 保存为true，标记冒泡列表要刷新
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我（ta）的冒泡列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 组件初始化
     */
    private void initView() {

        xrefreshView = (XRefreshView) this.findViewById(R.id.my_bubble_xrefreshview_View);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.my_bubble_listView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        my_bubble_back_layout = (LinearLayout) this.findViewById(R.id.my_bubble_back_layout);

        my_bubble_option_layout = (LinearLayout) this.findViewById(R.id.my_bubble_option_layout);

        my_bubble_title_tv = (TextView) this.findViewById(R.id.my_bubble_title_tv);

        my_bubble_option_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isPostLimit) {
                    isPostLimit = true;
                    loadingDiaog = DialogUtil.LoadingDialog(MyBubblingActivity.this, null);
                    if (!isFinishing()) {
                        loadingDiaog.show();
                    }
                    getBubbleLimit();
                }


            }
        });

        my_bubble_back_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        adapter = new DiscoverBubblingAdapter(this);

        adapter.setOnItemClickLitener(this);

        mRecyclerView.setAdapter(adapter);

        my_bubble_error_location_vs = (ViewStub) findViewById(R.id.my_bubble_error_location_vs);
        my_bubble_error_network_vs = (ViewStub) findViewById(R.id.my_bubble_error_network_vs);
        my_bubble_error_no_data_vs = (ViewStub) findViewById(R.id.my_bubble_error_no_data_vs);


    }


    private void handleNoDataError() {

        my_bubble_error_no_data_vs.setVisibility(View.VISIBLE);

        LinearLayout error_no_data_layout = (LinearLayout) this.findViewById(R.id.error_no_data_layout);
        error_no_data_layout.setVisibility(View.VISIBLE);

        TextView error_no_data_tv = (TextView) this.findViewById(R.id.error_no_data_tv);

        ImageView error_no_data_img = (ImageView) this.findViewById(R.id.error_no_data_img);

        error_no_data_tv.setText(R.string.error_bubbling_wo_hini);
        error_no_data_img.setImageResource(R.drawable.error_bubbling_message);
    }


    private void handleLocationError() {

        my_bubble_error_location_vs.setVisibility(View.VISIBLE);

        LinearLayout error_location_layout = (LinearLayout) this.findViewById(R.id.error_location_layout);
        error_location_layout.setVisibility(View.VISIBLE);

        TextView error_location_tv = (TextView) this.findViewById(R.id.error_location_tv);

        error_location_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);
                try {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    MyBubblingActivity.this.startActivity(intent);
                }
            }
        });
    }


    private void handleNetError() {

        my_bubble_error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) this.findViewById(R.id.error_network_layout);
        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) this.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);

                my_bubble_error_network_vs.setVisibility(View.GONE);
                my_bubble_error_no_data_vs.setVisibility(View.GONE);
                my_bubble_error_location_vs.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.GONE);
                loadingDiaog = DialogUtil.LoadingDialog(MyBubblingActivity.this, null);

                if (!isFinishing()) {
                    loadingDiaog.show();
                }
                _start = 0;
                onRefreshData(0);
            }
        });
    }


    private void setXrefreshViewListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 0) {

                    Glide.with(MyBubblingActivity.this).resumeRequests();

                } else {
                    Glide.with(MyBubblingActivity.this).pauseRequests();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        mRecyclerView.setHasFixedSize(true);

        xrefreshView.setPullLoadEnable(true);

        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xrefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        xrefreshView.setAutoLoadMore(true);


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);


        mXRefreshViewFooters = new XRefreshViewFooters(this);

        mXRefreshViewFooters.setRecyclerView(mRecyclerView);

        adapter.setCustomLoadMoreView(mXRefreshViewFooters);


        mXRefreshViewFooters.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoadMoreData();
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

                        onLoadMoreData();
                    }
                }, 1000);
            }
        });


        xrefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        _start = 0;
                        onRefreshData(_start);

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        onLoadMoreData();


                    }
                }, 1000);

            }

        });
    }

    private void initData() {
        String localeStr = getDataLocal(userID);

        if (!CheckUtil.isEmpty(localeStr)) {
            BubblingDto bubblinDto = JsonUtils.fromJson(localeStr, BubblingDto.class);

            if (bubblinDto != null) {
                if (bubblinDto.getList() != null && bubblinDto.getList().size() > 0) {
                    // 有本地数据
                    total = bubblinDto.getTotal();
                    hasLocalData = true;
                    my_bubble_error_location_vs.setVisibility(View.GONE);
                    my_bubble_error_network_vs.setVisibility(View.GONE);
                    my_bubble_error_no_data_vs.setVisibility(View.GONE);
                    xrefreshView.setVisibility(View.VISIBLE);
                    adapter.setData(bubblinDto.getList());
                    adapter.notifyDataSetChanged();

                } else {
                    // 没有本地数据
                    hasLocalData = false;
                    my_bubble_error_location_vs.setVisibility(View.GONE);
                    my_bubble_error_network_vs.setVisibility(View.GONE);
                    my_bubble_error_no_data_vs.setVisibility(View.GONE);
                    xrefreshView.setVisibility(View.GONE);
                }

            } else {
                // 没有本地数据
                hasLocalData = false;
                my_bubble_error_location_vs.setVisibility(View.GONE);
                my_bubble_error_network_vs.setVisibility(View.GONE);
                my_bubble_error_no_data_vs.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.GONE);

            }

        } else {
            // 没有本地数据
            hasLocalData = false;
            my_bubble_error_location_vs.setVisibility(View.GONE);
            my_bubble_error_network_vs.setVisibility(View.GONE);
            my_bubble_error_no_data_vs.setVisibility(View.GONE);
            xrefreshView.setVisibility(View.GONE);

        }
    }

    private void onRefreshData(final int start) {


        LocInfo myLoc = Loc.getLoc();

        if (myLoc != null && myLoc.getLoc() != null) {

            latitude = myLoc.getLoc().getLatitude();

            longtitude = myLoc.getLoc().getLongitude();
        }


        LatLng pt = new LatLng(latitude, longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        if (latitude == 0 && longtitude == 0) {

            if (hasLocalData) {
                my_bubble_error_location_vs.setVisibility(View.GONE);
                my_bubble_error_network_vs.setVisibility(View.GONE);
                my_bubble_error_no_data_vs.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.VISIBLE);
                //提示未开启定位
//                DialogUtil.showDisCoverNetToast(MyBubblingActivity.this, "获取位置失败");
            } else {

                handleLocationError();
                my_bubble_error_network_vs.setVisibility(View.GONE);
                my_bubble_error_no_data_vs.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.GONE);
            }
            loadingDiaog.dismiss();

            mXRefreshViewHeaders.onRefreshFail();

            xrefreshView.stopRefresh();
            return;
        }

        Time = "";


        BubblingListBean nearbyBean = new BubblingListBean();
        nearbyBean.setLng(pt.longitude);
        nearbyBean.setLat(pt.latitude);
        nearbyBean.setStart(start);
        nearbyBean.setRows(20);
        nearbyBean.setR(radii);
        nearbyBean.setTime(Time);
        nearbyBean.setUserId(userID);

        BubblingListService nearbyService = new BubblingListService(this);

        nearbyService.parameter(nearbyBean);
        nearbyService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                xrefreshView.stopRefresh();

                BubblingListRespBean nearbyRespBean = (BubblingListRespBean) respBean;
                BubblingDto bubblinDto = nearbyRespBean.getResp();
                String responseStr = JsonUtils.toJson(bubblinDto);

                // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                // 如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                loadingDiaog.dismiss();

                my_bubble_error_location_vs.setVisibility(View.GONE);
                my_bubble_error_network_vs.setVisibility(View.GONE);

                if (bubblinDto != null) {

                    List<BubblingList> list = bubblinDto.getList();

                    if (list != null && list.size() > 0) {// 有数据


                        LogUtils.e("lsitksfisj fs fs fs=" + list.size());


                        Time = list.get(0).getCreateTime();// 拿第一条数据的时间给下一次请求使用

                        total = bubblinDto.getTotal();

                        _start = _start + bubblinDto.getList().size();

                        my_bubble_error_no_data_vs.setVisibility(View.GONE);

                        xrefreshView.setVisibility(View.VISIBLE);

                        adapter.setData(list);

                        adapter.notifyDataSetChanged();

                    } else {// 还是没有数据

                        handleNoDataError();

                        xrefreshView.setVisibility(View.GONE);
                    }

                    AsyncTask<Void, Void, Integer> async = saveDataLocal(userID, responseStr);

                    if (null == async) {
                        return;
                    }

                    async.execute();

                } else {// 没有数据
                    handleNoDataError();
                    xrefreshView.setVisibility(View.GONE);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                mXRefreshViewHeaders.onRefreshFail();
                xrefreshView.stopRefresh();
                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                // 动画回收
                loadingDiaog.dismiss();
                my_bubble_error_location_vs.setVisibility(View.GONE);
                handleNetError();
                xrefreshView.setVisibility(View.GONE);
                my_bubble_error_no_data_vs.setVisibility(View.GONE);
            }
        });
        nearbyService.enqueue();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                my_bubble_error_location_vs.setVisibility(View.GONE);
                my_bubble_error_network_vs.setVisibility(View.GONE);
                my_bubble_error_no_data_vs.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.VISIBLE);

            }
        }, 2000);


    }

    private void onLoadMoreData() {

        if (total <= _start) {

            LogUtils.e("我失败了。。。。。");

            mXRefreshViewFooters.setLoadcomplete(true);

            xrefreshView.stopLoadMore(false);

            return;
        }


        LatLng pt = new LatLng(latitude, longtitude);

        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        BubblingListBean nearbyBean = new BubblingListBean();

        nearbyBean.setLng(pt.longitude);

        nearbyBean.setLat(pt.latitude);

        nearbyBean.setStart(_start);

        nearbyBean.setRows(20);

        nearbyBean.setR(radii);

        nearbyBean.setTime(Time);

        nearbyBean.setUserId(userID);

        BubblingListService nearbyService = new BubblingListService(this);

        nearbyService.parameter(nearbyBean);
        nearbyService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BubblingListRespBean nearbyRespBean = (BubblingListRespBean) respBean;
                BubblingDto bubblinDto = nearbyRespBean.getResp();


                if (bubblinDto != null) {
                    List<BubblingList> list = bubblinDto.getList();
                    if (list != null && list.size() > 0) {
                        Time = list.get(0).getCreateTime();// 拿第一条数据的时间给下一次请求使用
                        total = bubblinDto.getTotal();
                        _start = _start + list.size();
                        adapter.addData(list);
                        adapter.notifyDataSetChanged();

                    }

                }

                if (total >= _start) {

                    LogUtils.e("total");
                    mXRefreshViewFooters.setLoadcomplete(false);

                    xrefreshView.stopLoadMore();

                } else {


                    LogUtils.e("start");

                    mXRefreshViewFooters.setLoadcomplete(true);

                    xrefreshView.stopLoadMore(false);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                LogUtils.e("onFail");

                mXRefreshViewFooters.setLoadcomplete(false);
                xrefreshView.stopLoadMore(false);

                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                my_bubble_error_location_vs.setVisibility(View.GONE);
                my_bubble_error_network_vs.setVisibility(View.GONE);
                my_bubble_error_no_data_vs.setVisibility(View.GONE);
                // 提示连接失败
                DialogUtil.showDisCoverNetToast(MyBubblingActivity.this);

            }
        });
        nearbyService.enqueue();


    }

    @Override
    public void onItemClick(View view, int position, String bubblingID, BubblingList bubblingList) {
        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUBBLING_LIST_ID, bubblingID);
        bundle.putSerializable(YpSettings.BUBBLING_LIST, bubblingList);

        ActivityUtil.jump(MyBubblingActivity.this, BubblingInfoActivity.class, bundle, 0, 100);
    }

    @Override
    public void onIconItemClick(View view, int position, List<String> list, int imgViewWidth, int imgViewHight) {
        ZoomViewerDto sq = new ZoomViewerDto();
        sq.list = list;
        sq.position = position;

        Bundle bundle = new Bundle();
        bundle.putSerializable(YpSettings.ZOOM_LIST_DTO, sq);
        ActivityUtil.jump(MyBubblingActivity.this, ZoomViewerActivity.class, bundle, 0, 200);

    }

    /**
     * 点赞
     */
    @Override
    public void onPraiseClick(View view, final int position, boolean isLike, String id) {
        boolean doisLike = false;
        if (isLike) {
            doisLike = false;
        } else {
            doisLike = true;
        }
        adapter.praiseIsLike(position, doisLike);
        onPraisehttp(position, doisLike, id);
    }

    /**
     * 去个人资料
     */

    @Override
    public void onUserIconItemClick(View view, int position, int userID) {
        Bundle userbundle = new Bundle();
        userbundle.putInt(YpSettings.USERID, userID);
        ActivityUtil.jump(MyBubblingActivity.this, UserInfoActivity.class, userbundle, 0, 100);
    }

    @Override
    public void onEvaluateClick(View view, int position, String bubblingID, BubblingList bubblingList) {
        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUBBLING_LIST_ID, bubblingID);
        bundle.putSerializable(YpSettings.BUBBLING_LIST, bubblingList);
        ActivityUtil.jump(MyBubblingActivity.this, BubblingInfoActivity.class, bundle, 0, 100);

    }

    @Override
    public void onTypeLayoutItemClick(View view, int position, Location location, String bubblingID, BubblingList bubblingList) {

        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUBBLING_LIST_ID, bubblingID);
        bundle.putSerializable(YpSettings.BUBBLING_LIST, bubblingList);
        ActivityUtil.jump(MyBubblingActivity.this, BubblingInfoActivity.class, bundle, 0, 100);
    }


    @Override
    public void checkLookVideo(View view, int position, int userid, int targetId, int videoid, int videoType) {
        if (null == loadingDiaog) {

            loadingDiaog = DialogUtil.LoadingDialog(this, null);

        }

        loadingDiaog.show();

        Subscription subscription = null;

        if (videoType == 3) {//形象视频

            subscription = HttpFactory.getHttpApi().getUnlockVideo(targetId, videoid)

                    .compose(SchedulersCompat.applyIoSchedulers())

                    .compose(RxResultHelper.handleResult())

                    .subscribe(videoBean -> {

                        Logger.e("校验视频通过 === " + videoBean.toString());

                        loadingDiaog.dismiss();

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

                                bundle.putInt(YpSettings.USERID, targetId);

                                bundle.putString("type", "DiscoverBubblingFragment");

                                AppUtils.jump(this, VideoActivity.class, bundle);
                            }

                        } else if (status == 1) {

                            RxBus.get().post("vipDialog", videoid);
                        }
                    }, throwable -> {

                        Logger.e("校验视频异常");

                        loadingDiaog.dismiss();

                        ApiResp apiResp = ErrorHanding.handle(throwable);

                        if (apiResp == null) {

                            DialogUtil.showDisCoverNetToast(this);

                        } else {

                            DialogUtil.showDisCoverNetToast(this, apiResp.getMsg());

                        }

                    });
        } else if (videoType == 1) {//认证视频
            Bundle bundle = new Bundle();
            bundle.putInt(YpSettings.USERID, targetId);
            ActivityUtil.jump(MyBubblingActivity.this, VideoDetailGetActivity.class, bundle, 0, 100);
        }

        if (subscription != null) {

            addSubscrebe(subscription);

        }
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

    @Override
    public void onCoveringAblumClick(View view, int userId, int targetId, boolean islock, int position) {

        showUnlockDialog("解锁需要10个苹果，是否付出苹果查看(解锁成功当天内可以任意查看对方私密照片)", "取消使用", "查看", userId, targetId, position);
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
                if (isFinishing()) {
                    loadingDiaog.show();
                }
                PrivateAlbumBody body = new PrivateAlbumBody();
                body.setUserId(userId);
                body.setLookedUserId(targetId);
                //left btn
                Subscription subscription = HttpFactory.getHttpApi().putUnlockAlbum(body)
                        .compose(SchedulersCompat.applyIoSchedulers())
                        .compose(RxResultHelper.handleResult())
                        .subscribe(lockAlbum -> {
                            Logger.e("相册解锁成功");
                            loadingDiaog.dismiss();
                            int status = lockAlbum.getResult();
                            if (status == 0) {
//                                mDiscoverBubblingAdapter.unlockAblum(position, true);
                                RxBus.get().post("OnLock",targetId);
                                onRefreshAdapter(targetId);
                            } else if (status == 1) {
                                showBuyDialog("苹果数量不足，请先购买苹果", "取消", "购买苹果", false);
                            }
                        }, throwable -> {
                            Logger.e("相册解锁抛出异常" + throwable.toString());
                            DialogUtil.showDisCoverNetToast(this, ErrorHanding.handleError(throwable));
                            loadingDiaog.dismiss();
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

    public void onRefreshAdapter(int userId) {
        Logger.e("userId == " + userId + "的私密相册已经被炸掉了");

        List<BubblingList> list = adapter.getData();

        int size = list.size();

        //取出该用户所有的冒泡
        for (int i = 0; i < size; i++) {

            if (list.get(i).getUser().getId() == userId) {

                list.get(i).setUnlockPrivateImage(true);

            }

        }

        adapter.unlockAblum(list);

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

                    ActivityUtil.jump(this, VipOpenedActivity.class, bundle, 0, 100);
                } else {
                    Bundle appleBundle = new Bundle();

                    appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);


                    ActivityUtil.jump(this, AppleListActivity.class, appleBundle, 0, 100);
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
     * 获取缓存数据
     *
     * @param userId
     * @return
     */
    private String getDataLocal(int userId) {

        try {
            UserBubblingLocalDto localDto = App.db.findFirst(Selector.from(UserBubblingLocalDto.class).where("userId", " =", userId));
            if (localDto == null) {
                return null;
            }
            return localDto.getData();
        } catch (DbException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 保存数据
     *
     * @param userId
     * @param data
     * @return
     */
    private AsyncTask<Void, Void, Integer> saveDataLocal(final int userId, final String data) {

        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {

                try {
                    UserBubblingLocalDto localDto = App.db.findFirst(Selector.from(UserBubblingLocalDto.class).where("userId", " =", userId));

                    if (localDto != null) {
                        localDto.setUserId(userId);
                        localDto.setData(data);
                        localDto.setTime(System.currentTimeMillis());
                        App.db.update(localDto);
                    } else {
                        localDto = new UserBubblingLocalDto();
                        localDto.setUserId(userId);
                        localDto.setData(data);
                        localDto.setTime(System.currentTimeMillis());
                        App.db.save(localDto);
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        return task;

    }

    /**
     * 点赞请求
     *
     * @param position
     * @param islike
     * @param id
     */
    private void onPraisehttp(final int position, boolean islike, String id) {


        BubblingBubblePraiseBean praiseBean = new BubblingBubblePraiseBean();
        praiseBean.setId(id);
        praiseBean.setLike(islike);

        BubblingBubblePraiseService praiseService = new BubblingBubblePraiseService(MyBubblingActivity.this);

        praiseService.parameter(praiseBean);
        praiseService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                String statusCode = respBean.getStatus();


                if (TextUtils.equals("404", statusCode)) {// 内容不存在了

                    DialogUtil.showDisCoverNetToast(MyBubblingActivity.this, "该冒泡不存在");
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(MyBubblingActivity.this, "您访问的信息存在违规的内容");
                    return;
                }
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(MyBubblingActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(MyBubblingActivity.this, msg);


            }
        });
        praiseService.enqueue();


    }


    private void getBubbleLimit() {

        BubbleLimitService bubbleLimitService = new BubbleLimitService(MyBubblingActivity.this);
        bubbleLimitService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();
                isPostLimit = false;
                BubbleLimitRespBean bubbleLimitRespBean = (BubbleLimitRespBean) respBean;
                ChatAttamptRespDto dto = bubbleLimitRespBean.getResp();
                if (dto != null) {
                    if (dto.isSuccess() == true) {
                        SharedprefUtil.saveBoolean(MyBubblingActivity.this, YpSettings.BUBBLING_ADDRESS_STR, false);
                        Bundle bundle = new Bundle();
                        SharedprefUtil.save(MyBubblingActivity.this, YpSettings.BUBBLING_FROM_TAG_KEY, YpSettings.BUBBLING_FROM_TAG_USERCENTER_MY);
                        ActivityUtil.jump(MyBubblingActivity.this, BubblingAddressActivity.class, bundle, 0, 200);
                    } else {
                        limitdialog = DialogUtil.createHintOperateDialog(MyBubblingActivity.this, "", dto.getMessage(), "查看帮助", "确定", backCallListener);
                        if (!MyBubblingActivity.this.isFinishing()) {
                            limitdialog.show();
                        }
                    }
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();
                isPostLimit = false;
                // 没有网络的场合，去提示页
                DialogUtil.showDisCoverNetToast(MyBubblingActivity.this);
            }
        });
        bubbleLimitService.enqueue();


    }

    private BackCallListener backCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!isFinishing()) {
                limitdialog.dismiss();
            }
            //跳转到web 查看帮助

            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Standard/AvatarAudit");
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "头像审核规范");
            bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

            ActivityUtil.jump(MyBubblingActivity.this, SimpleWebViewActivity.class, bundle, 0, 100);
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!isFinishing()) {
                limitdialog.dismiss();
            }

        }
    };


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("BubblingListEvent")

            }

    )
    public void refreshBubbling(BubblingListEvent o) {

        int type = o.getEventType();
        if (type == 2) {
            if (xrefreshView.getVisibility() == View.GONE) {
                onRefreshData(0);
            } else {
                xrefreshView.startRefresh();
            }

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        unSubscribe();
    }

}
