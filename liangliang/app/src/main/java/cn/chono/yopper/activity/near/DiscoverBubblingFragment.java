package cn.chono.yopper.activity.near;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BubblingBubblePraise.BubblingBubblePraiseBean;
import cn.chono.yopper.Service.Http.BubblingBubblePraise.BubblingBubblePraiseService;
import cn.chono.yopper.Service.Http.BubblingNearby.BubblingNearbyBean;
import cn.chono.yopper.Service.Http.BubblingNearby.BubblingNearbyRespBean;
import cn.chono.yopper.Service.Http.BubblingNearby.BubblingNearbyService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.YpBaseFragment;
import cn.chono.yopper.activity.base.IndexActivity;
import cn.chono.yopper.activity.order.AppleListActivity;
import cn.chono.yopper.activity.usercenter.VipOpenedActivity;
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
import cn.chono.yopper.data.DiscoverBubblingLocalDto;
import cn.chono.yopper.data.GeneralVideos;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.ZoomViewerDto;
import cn.chono.yopper.entity.ApiResp;
import cn.chono.yopper.entity.exception.ErrorHanding;
import cn.chono.yopper.entity.likeBean.PrivateAlbumBody;
import cn.chono.yopper.event.BubblingListEvent;
import cn.chono.yopper.event.OnNearTabEvent;
import cn.chono.yopper.event.OnTopEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.ui.VideoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtils;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DiscoverBubblingFragment extends YpBaseFragment implements OnItemClickLitener {

    private Activity mActivity;

    private XRefreshView bubblingXRefreshView;

    private RecyclerView bubblingRecyclerView;

    private DiscoverBubblingAdapter mDiscoverBubblingAdapter;

    private double latitude;
    private double longtitude;
    private double radii = 5.0;

    private int start = 0;
    private int total = 0;

    private boolean haveFilterData = false;

    private boolean haveFilterListData = false;

    private boolean haveGpsTag;


    private ViewStub bubble_error_no_data_vs;

    private ViewStub bubble_error_location_vs;

    private ViewStub bubble_error_network_vs;

    private String _Time = "";

    private LocInfo myLoc;

    private boolean goSetLocationg = false;


    private XRefreshViewFooters mXRefreshViewFooters;

    private XRefreshViewHeaders mXRefreshViewHeaders;

    private View contentView;

    private int tab_Id;

    private Dialog loadingDiaog;

    private boolean isinitData = false;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.discover_bubbling_fragment, container, false);
        PushAgent.getInstance(getActivity()).onAppStart();

        RxBus.get().register(this);

        initView(contentView);
        setXrefreshViewListener();

        loadingDiaog = DialogUtil.LoadingDialog(getActivity(), null);

        tab_Id = SharedprefUtil.getInt(getActivity(), YpSettings.DISCOVER_TAB_ID, 0);

        if (tab_Id == 2 && !isinitData) {
            isinitData = true;
            initData();
            getLocAndInit();
        }


        return contentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    @Override
    public void onResume() {
        super.onResume();

        if (IndexActivity.selected_menu_type == 1) {

            MobclickAgent.onPageStart("附近 (冒泡)"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
            MobclickAgent.onResume(getActivity()); // 统计时长

        }

        if (goSetLocationg) {
            getLocAndInit();
        }
    }

    private void getLocAndInit() {

        haveGpsTag = Loc.isGpsAvailable();
        if (!haveGpsTag) {
            // 未开启定位权限


            bubble_error_network_vs.setVisibility(View.GONE);

            if (haveFilterData) {
                bubble_error_location_vs.setVisibility(View.GONE);
                if (haveFilterListData) {
                    bubblingXRefreshView.setVisibility(View.VISIBLE);
                    bubble_error_no_data_vs.setVisibility(View.GONE);
                } else {
                    bubblingXRefreshView.setVisibility(View.GONE);
                    handleNoDataError();

                }
                // 提示未开启定位
                locDialog = DialogUtil.createHintOperateDialog(getActivity(), "定位服务未开启", "开启定位服务以看到附近用户", "取消", "立即开启", locbackCallListener);
                locDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                if (!getActivity().isFinishing()) {
                    locDialog.show();
                }

            } else {
                handleLocationError();
                bubblingXRefreshView.setVisibility(View.GONE);
                bubble_error_no_data_vs.setVisibility(View.GONE);
            }

        } else {
            bubble_error_location_vs.setVisibility(View.GONE);
            bubble_error_network_vs.setVisibility(View.GONE);

            if (haveFilterData) {
                if (haveFilterListData) {
                    bubblingXRefreshView.setVisibility(View.VISIBLE);
                    bubble_error_no_data_vs.setVisibility(View.GONE);
                } else {
                    bubblingXRefreshView.setVisibility(View.GONE);
                    handleNoDataError();
                    if (!getActivity().isFinishing()) {
                        loadingDiaog.show();
                    }
                }

            } else {
                bubblingXRefreshView.setVisibility(View.GONE);
                bubble_error_no_data_vs.setVisibility(View.GONE);

                if (!getActivity().isFinishing()) {
                    loadingDiaog.show();
                }
            }
            getLocinfo();
            boolean isRefresh = SharedprefUtil.getBoolean(getActivity(), YpSettings.BUBBLING_PUBLISH, false);
            if (isRefresh) {
                start = 0;
                onRefreshData();
                SharedprefUtil.saveBoolean(getActivity(), YpSettings.BUBBLING_PUBLISH, false);// 保存为true，标记冒泡列表要刷新
            }
        }
    }

    private Dialog locDialog;

    private BackCallListener locbackCallListener = new BackCallListener() {
        @Override
        public void onEnsure(View view, Object... obj) {
            if (!getActivity().isFinishing()) {
                locDialog.dismiss();
            }
            goSetLocationg = true;
            try {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        }

        @Override
        public void onCancel(View view, Object... obj) {
            if (!getActivity().isFinishing()) {
                locDialog.dismiss();
            }

        }
    };


    private void getLocinfo() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                int count = 0;
                while (true) {
                    boolean isExist = Loc.IsLocExist();
                    if (isExist) {
                        break;
                    }
                    count++;
                    if (count > 9) {
                        break;
                    }
                    SystemClock.sleep(1000);
                }

                myLoc = Loc.getLoc();
                if (myLoc != null && myLoc.getLoc() != null) {
                    LocHandler.sendEmptyMessage(1);

                } else {
                    LocHandler.sendEmptyMessage(0);
                }
            }
        }).start();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("附近 (冒泡)"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(getActivity()); // 统计时长

    }

    /**
     * 组件初始化
     *
     * @param contentView
     */
    private void initView(View contentView) {
        bubblingXRefreshView = (XRefreshView) contentView.findViewById(R.id.xrefreshview_View);
        bubblingRecyclerView = (RecyclerView) contentView.findViewById(R.id.listView);
        bubblingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        bubblingRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDiscoverBubblingAdapter = new DiscoverBubblingAdapter(getActivity());

        mDiscoverBubblingAdapter.setOnItemClickLitener(this);

        bubblingRecyclerView.setAdapter(mDiscoverBubblingAdapter);

        bubble_error_no_data_vs = (ViewStub) contentView.findViewById(R.id.bubble_error_no_data_vs);

        bubble_error_location_vs = (ViewStub) contentView.findViewById(R.id.bubble_error_location_vs);

        bubble_error_network_vs = (ViewStub) contentView.findViewById(R.id.bubble_error_network_vs);

    }


    private void handleNoDataError() {

        bubble_error_no_data_vs.setVisibility(View.VISIBLE);

        LinearLayout error_no_data_layout = (LinearLayout) contentView.findViewById(R.id.error_no_data_layout);
        error_no_data_layout.setVisibility(View.VISIBLE);

        TextView error_no_data_tv = (TextView) contentView.findViewById(R.id.error_no_data_tv);

        ImageView error_no_data_img = (ImageView) contentView.findViewById(R.id.error_no_data_img);

        error_no_data_tv.setText(R.string.error_bubbling_hini);
        error_no_data_img.setImageResource(R.drawable.error_bubbling_message);
    }


    private void handleLocationError() {

        bubble_error_location_vs.setVisibility(View.VISIBLE);

        LinearLayout error_location_layout = (LinearLayout) contentView.findViewById(R.id.error_location_layout);
        error_location_layout.setVisibility(View.VISIBLE);

        TextView error_location_tv = (TextView) contentView.findViewById(R.id.error_location_tv);

        error_location_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);
                try {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    getActivity().startActivity(intent);
                }
            }
        });
    }


    private void handleNetError() {

        bubble_error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) contentView.findViewById(R.id.error_network_layout);

        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) contentView.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);


                bubble_error_network_vs.setVisibility(View.GONE);
                bubble_error_location_vs.setVisibility(View.GONE);
                bubble_error_no_data_vs.setVisibility(View.GONE);
                bubblingXRefreshView.setVisibility(View.GONE);
                if (!getActivity().isFinishing()) {
                    loadingDiaog.show();
                }
                start = 0;
                onRefreshData();
            }
        });
    }


    private void setXrefreshViewListener() {

        bubblingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 0) {

                    Glide.with(getActivity()).resumeRequests();

                } else {
                    Glide.with(getActivity()).pauseRequests();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        bubblingRecyclerView.setHasFixedSize(true);

        bubblingXRefreshView.setPullLoadEnable(true);

        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        bubblingXRefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        bubblingXRefreshView.setAutoLoadMore(true);


        mXRefreshViewFooters = new XRefreshViewFooters(getActivity());

        mXRefreshViewFooters.setRecyclerView(bubblingRecyclerView);

        mXRefreshViewHeaders = new XRefreshViewHeaders(getActivity());

        bubblingXRefreshView.setCustomHeaderView(mXRefreshViewHeaders);


        mDiscoverBubblingAdapter.setCustomLoadMoreView(mXRefreshViewFooters);


        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        bubblingXRefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        bubblingXRefreshView.setAutoLoadMore(true);


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


        mXRefreshViewFooters.onAutoLoadMoreFail(new SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                LogUtils.e("--------------------占击了");

                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        onLoadMoreData();


                    }
                }, 1000);

            }
        });


        bubblingXRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        start = 0;
                        onRefreshData();

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                LogUtils.e("--------------------上拉了");
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        onLoadMoreData();


                    }
                }, 1000);

            }

        });
    }


    private void initData() {

        String dataStr = grtDataLocal(LoginUser.getInstance().getUserId());

        if (!TextUtils.isEmpty(dataStr)) {
            BubblingDto bubblinDto = JsonUtils.fromJson(dataStr, BubblingDto.class);

            if (bubblinDto != null) {
                List<BubblingList> list = bubblinDto.getList();
                haveFilterData = true;
                if (list != null && list.size() > 0) {// 有数据
                    haveFilterListData = true;
                    bubble_error_no_data_vs.setVisibility(View.GONE);
                    bubblingXRefreshView.setVisibility(View.VISIBLE);

                    mDiscoverBubblingAdapter.setData(list);
                    mDiscoverBubblingAdapter.notifyDataSetChanged();

                } else {// 还是没有数据
                    haveFilterListData = false;
                    handleNoDataError();
                    bubblingXRefreshView.setVisibility(View.GONE);
                }

            } else {// 还是数据为空时
                haveFilterData = false;
                haveFilterListData = false;
                bubblingXRefreshView.setVisibility(View.GONE);
                if (!getActivity().isFinishing()) {
                    loadingDiaog.show();
                }
            }

        } else {// 为空就是没有数据
            haveFilterData = false;
            haveFilterListData = false;
            // 没有本地数据
            bubblingXRefreshView.setVisibility(View.GONE);
            if (!getActivity().isFinishing()) {
                loadingDiaog.show();
            }

        }

    }

    private Handler LocHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {

                if (msg.what == 0) {// 定位失败

                    // 动画回收
                    loadingDiaog.dismiss();

                    if (haveFilterData) {
                        if (haveFilterListData) {
                            bubblingXRefreshView.setVisibility(View.VISIBLE);
                            bubble_error_no_data_vs.setVisibility(View.GONE);
                        } else {
                            bubblingXRefreshView.setVisibility(View.GONE);
                            handleNoDataError();
                        }
                        // 提示定位失败
//						DialogUtil.showDisCoverNetToast(getActivity(),"获取位置失败");
                    } else {
                        bubblingXRefreshView.setVisibility(View.GONE);
                        bubble_error_no_data_vs.setVisibility(View.GONE);
                        handleLocationError();
                    }

                    LogUtils.e("--------------你妈----------");

                } else if (msg.what == 1) {// 找到位置
                    latitude = myLoc.getLoc().getLatitude();
                    longtitude = myLoc.getLoc().getLongitude();
                    start = 0;

                    LogUtils.e("--------------LocHandler----------");
                    onRefreshData();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private void onRefreshData() {


        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            latitude = myLoc.getLoc().getLatitude();
            longtitude = myLoc.getLoc().getLongitude();
        }

        if (latitude == 0 && longtitude == 0) {

            bubblingXRefreshView.stopRefresh();
            if (loadingDiaog != null) {
                loadingDiaog.dismiss();
            }
            return;
        }

        LatLng pt = new LatLng(latitude, longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        _Time = "";


        BubblingNearbyBean nearbyBean = new BubblingNearbyBean();
        nearbyBean.setLng(pt.longitude);
        nearbyBean.setLat(pt.latitude);
        nearbyBean.setStart(start);
        nearbyBean.setRows(20);
        nearbyBean.setR(radii);
        nearbyBean.setTime(_Time);

        BubblingNearbyService nearbyService = new BubblingNearbyService(getActivity());

        nearbyService.parameter(nearbyBean);
        nearbyService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BubblingNearbyRespBean nearbyRespBean = (BubblingNearbyRespBean) respBean;
                BubblingDto bubblinDto = nearbyRespBean.getResp();
                String responseStr = JsonUtils.toJson(bubblinDto);


                // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                // 如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                // 动画回收
                loadingDiaog.dismiss();

                bubble_error_location_vs.setVisibility(View.GONE);
                bubble_error_network_vs.setVisibility(View.GONE);


                if (bubblinDto != null) {
                    haveFilterData = true;

                    List<BubblingList> list = bubblinDto.getList();
                    total = bubblinDto.getTotal();

                    if (list != null && list.size() > 0) {// 有数据

                        _Time = list.get(0).getCreateTime();// 拿第一条数据的时间给下一次请求使用

                        start = start + bubblinDto.getList().size();
                        haveFilterListData = true;
                        bubble_error_no_data_vs.setVisibility(View.GONE);
                        bubblingXRefreshView.setVisibility(View.VISIBLE);
                        mDiscoverBubblingAdapter.setData(list);
                        mDiscoverBubblingAdapter.notifyDataSetChanged();

                    } else {// 还是没有数据
                        haveFilterListData = false;
                        handleNoDataError();
                        bubblingXRefreshView.setVisibility(View.GONE);
                    }

                    AsyncTask<Void, Void, Integer> async = saveDataLocal(
                            LoginUser.getInstance().getUserId(),
                            responseStr);
                    async.execute();

                } else {// 没有数据

                    haveFilterData = false;
                    haveFilterListData = false;
                    handleNoDataError();
                    bubblingXRefreshView.setVisibility(View.GONE);

                }

                if (total >= start) {

                    mXRefreshViewFooters.setLoadcomplete(false);

                } else {
                    mXRefreshViewFooters.setLoadcomplete(true);
                }

                bubblingXRefreshView.stopRefresh();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mXRefreshViewHeaders.onRefreshFail();

                bubblingXRefreshView.stopRefresh();
                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                // 动画回收
                // 动画回收
                // 动画回收
                loadingDiaog.dismiss();

                bubble_error_location_vs.setVisibility(View.GONE);

                if (haveFilterData) {
                    if (haveFilterListData) {
                        bubblingXRefreshView.setVisibility(View.VISIBLE);
                        bubble_error_no_data_vs.setVisibility(View.GONE);
                    } else {
                        bubblingXRefreshView.setVisibility(View.GONE);
                        handleNoDataError();
                    }

                    bubble_error_network_vs.setVisibility(View.GONE);
                    // 提示连接失败
                    DialogUtil.showDisCoverNetToast(getActivity());
                } else {
                    handleNetError();
                    bubblingXRefreshView.setVisibility(View.GONE);
                    bubble_error_no_data_vs.setVisibility(View.GONE);
                }


            }
        });
        nearbyService.enqueue();


    }

    private void onLoadMoreData() {

        if (total <= start) {

            mXRefreshViewFooters.setLoadcomplete(true);

            bubblingXRefreshView.stopLoadMore(false);
            return;
        }


        LatLng pt = new LatLng(latitude, longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);


        BubblingNearbyBean nearbyBean = new BubblingNearbyBean();
        nearbyBean.setLng(pt.longitude);
        nearbyBean.setLat(pt.latitude);
        nearbyBean.setStart(start);
        nearbyBean.setRows(20);
        nearbyBean.setR(radii);
        nearbyBean.setTime(_Time);

        BubblingNearbyService nearbyService = new BubblingNearbyService(getActivity());

        nearbyService.parameter(nearbyBean);
        nearbyService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BubblingNearbyRespBean nearbyRespBean = (BubblingNearbyRespBean) respBean;
                BubblingDto bubblinDto = nearbyRespBean.getResp();


                if (bubblinDto != null) {
                    List<BubblingList> list = bubblinDto.getList();
                    total = bubblinDto.getTotal();
                    if (list != null && list.size() > 0) {
                        _Time = list.get(0).getCreateTime();// 拿第一条数据的时间给下一次请求使用
                        start = start + list.size();
                        mDiscoverBubblingAdapter.addData(list);
                        mDiscoverBubblingAdapter.notifyDataSetChanged();
                    }

                }


                if (total >= start) {
                    mXRefreshViewFooters.setLoadcomplete(false);

                    bubblingXRefreshView.stopLoadMore();

                } else {

                    mXRefreshViewFooters.setLoadcomplete(true);

                    bubblingXRefreshView.stopLoadMore(false);
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mXRefreshViewFooters.setLoadcomplete(false);
                bubblingXRefreshView.stopLoadMore(false);

                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                bubble_error_location_vs.setVisibility(View.GONE);
                bubble_error_network_vs.setVisibility(View.GONE);
                bubble_error_no_data_vs.setVisibility(View.GONE);


            }
        });
        nearbyService.enqueue();


    }

    @Override
    public void onItemClick(View view, int position, String bubblingID, BubblingList bubblingList) {

        Logger.e("点击了冒泡 item =" + position + "是否已经解锁 == " + bubblingList.isUnlockPrivateImage());
        ViewsUtils.preventViewMultipleClick(view, 1000);

        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUBBLING_LIST_ID, bubblingID);
        bundle.putSerializable(YpSettings.BUBBLING_LIST, bubblingList);
        bundle.putInt(YpSettings.BUBBLING_LIST_POSITION, position);

        ActivityUtil.jump(getActivity(), BubblingInfoActivity.class, bundle, 0, 100);

    }

    @Override
    public void onIconItemClick(View view, int position, List<String> list, int imgViewWidth, int imgViewHight) {

        ZoomViewerDto sq = new ZoomViewerDto();

        sq.list = list;

        sq.position = position;

        Bundle bundle = new Bundle();
        bundle.putSerializable(YpSettings.ZOOM_LIST_DTO, sq);
        ActivityUtil.jump(getActivity(), ZoomViewerActivity.class, bundle, 0, 200);

    }

    @Override
    public void onPraiseClick(View view, final int position, boolean isLike, String id) {
        ViewsUtils.preventViewMultipleClick(view, 1000);
        boolean doisLike = false;
        if (isLike) {
            doisLike = false;
        } else {
            doisLike = true;
        }
        mDiscoverBubblingAdapter.praiseIsLike(position, doisLike);
        onPraisehttp(position, doisLike, id);

    }

    @Override
    public void onUserIconItemClick(View view, int position, int userID) {

        ViewsUtils.preventViewMultipleClick(view, 1000);

        Bundle userbundle = new Bundle();

        userbundle.putInt(YpSettings.USERID, userID);

        userbundle.putString(YpSettings.FROM_PAGE, YpSettings.BubblingItemRefresh);

        ActivityUtil.jump(getActivity(), UserInfoActivity.class, userbundle, 0, 100);

    }

    @Override
    public void onEvaluateClick(View view, int position, String bubblingID, BubblingList bubblingList) {
        ViewsUtils.preventViewMultipleClick(view, 1000);
        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUBBLING_LIST_ID, bubblingID);
        bundle.putSerializable(YpSettings.BUBBLING_LIST, bubblingList);
        bundle.putInt(YpSettings.BUBBLING_LIST_POSITION, position);
        ActivityUtil.jump(getActivity(), BubblingInfoActivity.class, bundle, 0, 100);

    }

    @Override
    public void onTypeLayoutItemClick(View view, int position, Location location, String bubblingID, BubblingList bubblingList) {
        // baiDuOverLay = new BaiDuOverLay(getActivity());
        // baiDuOverLay.setBaiDuData(location);
        // baiDuOverLay.showBaiDuOverLay();

        ViewsUtils.preventViewMultipleClick(view, 1000);

        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUBBLING_LIST_ID, bubblingID);
        bundle.putSerializable(YpSettings.BUBBLING_LIST, bubblingList);
        ActivityUtil.jump(getActivity(), BubblingInfoActivity.class, bundle, 0, 100);
    }

    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void onCoveringAblumClick(View view, int userId, int targetId, boolean islock, int position) {

        showUnlockDialog("解锁需要10个苹果，是否付出苹果查看(解锁成功当天内可以任意查看对方私密照片)", "取消使用", "查看", userId, targetId, position);
    }

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

    MyDialog dialog;
    MyDialog buyAppleDialog;

    public void showUnlockDialog(String tvStr, String btn1, String btn2, int userId, int targetId, int position) {
        dialog = new MyDialog(mActivity, R.style.MyDialog, R.layout.view_unlock_dialog, contentView -> {
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
                if (!getActivity().isFinishing()) {
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
                                onRefreshAdapter(targetId);
                            } else if (status == 1) {
                                showBuyDialog("苹果数量不足，请先购买苹果", "取消", "购买苹果", false);
                            }
                        }, throwable -> {
                            Logger.e("相册解锁抛出异常" + throwable.toString());
                            DialogUtil.showDisCoverNetToast(mActivity, ErrorHanding.handleError(throwable));
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

    /**
     * 买东西的dialog
     *
     * @param tvStr
     * @param btn1
     * @param btn2
     * @param isVideo
     */
    public void showBuyDialog(String tvStr, String btn1, String btn2, boolean isVideo) {
        buyAppleDialog = new MyDialog(mActivity, R.style.MyDialog, R.layout.view_unlock_dialog, contentView -> {
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

                    ActivityUtil.jump(mActivity, VipOpenedActivity.class, bundle, 0, 100);
                } else {
                    Bundle appleBundle = new Bundle();

                    appleBundle.putInt(YpSettings.PRODUCT_TYPE, Constant.ProductType_Apple);


                    ActivityUtil.jump(mActivity, AppleListActivity.class, appleBundle, 0, 100);
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
    private String grtDataLocal(int userId) {

        try {
            DiscoverBubblingLocalDto localDto = App.db.findFirst(Selector.from(DiscoverBubblingLocalDto.class).where("userId", " =", userId));
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
                    DiscoverBubblingLocalDto localDto = App.db.findFirst(Selector.from(DiscoverBubblingLocalDto.class).where("userId", " =", userId));

                    if (localDto != null) {
                        localDto.setUserId(LoginUser.getInstance().getUserId());
                        localDto.setData(data);
                        localDto.setTime(System.currentTimeMillis());
                        App.db.update(localDto);
                    } else {
                        localDto = new DiscoverBubblingLocalDto();
                        localDto.setUserId(LoginUser.getInstance().getUserId());
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

        BubblingBubblePraiseService praiseService = new BubblingBubblePraiseService(getActivity());

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

                LogUtils.e("卧槽请求回来了失败" + statusCode);

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了

                    DialogUtil.showDisCoverNetToast(getActivity(), "该冒泡不存在");
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    DialogUtil.showDisCoverNetToast(getActivity(), "您访问的信息存在违规的内容");
                    return;
                }
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(getActivity());
                    return;
                }
                DialogUtil.showDisCoverNetToast(getActivity(), msg);


            }
        });
        praiseService.enqueue();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("discoverbubblingFragment" + "页面被毁掉了");
        RxBus.get().unregister(this);
        unSubscribe();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OnTopEvent")

            }

    )
    public void onTopEvent(OnTopEvent event) {

        int type = event.getEventType();
        if (type == 2) {
//            listView.setSelection(0);
        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("BubblingListEvent")
            }

    )
    public void bubblingListEvent(BubblingListEvent event) {

        int type = event.getEventType();
        if (type == 1) {
            if (bubblingXRefreshView.getVisibility() == View.GONE) {
                start = 0;
                onRefreshData();
            } else {
                bubblingXRefreshView.startRefresh();
            }

        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OnNearTabEvent")

            }

    )
    public void onNearTabEvent(OnNearTabEvent event) {
        if (event.getEventType() == 3 && !isinitData) {
            isinitData = true;
            initData();
            getLocAndInit();

        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OnLock")

            }

    )
    public void onLock(Integer userId) {
        onRefreshAdapter(userId);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("vipDialog")

            }
    )
    public void vipDialog(Object remind) {
        showBuyDialog((String) remind, "取消", "查看VIP", true);
    }


    public void onRefreshAdapter(int userId) {
        Logger.e("userId == " + userId + "的私密相册已经被炸掉了");

        List<BubblingList> list = mDiscoverBubblingAdapter.getData();

        if (list != null && list.size() > 0) {

            int size = list.size();

            //取出该用户所有的冒泡
            for (int i = 0; i < size; i++) {

                if (list.get(i).getUser().getId() == userId) {

                    list.get(i).setUnlockPrivateImage(true);

                }
            }


        }


        mDiscoverBubblingAdapter.unlockAblum(list);


    }


    @Override
    public void checkLookVideo(View view, int position, int userid, int targetId, int videoid, int videoType) {

        if (null == loadingDiaog) {

            loadingDiaog = DialogUtil.LoadingDialog(mActivity, null);

        }

        loadingDiaog.show();

        Subscription subscription = null;
        Logger.e("videoType ===========" + videoType);
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

                                AppUtils.jump(mActivity, VideoActivity.class, bundle);
                            }

                        } else if (status == 1) {

                            RxBus.get().post("vipDialog", videoBean.getMsg());
                        }
                    }, throwable -> {

                        Logger.e("校验视频异常");

                        loadingDiaog.dismiss();

                        ApiResp apiResp = ErrorHanding.handle(throwable);

                        if (apiResp == null) {

                            DialogUtil.showDisCoverNetToast(mActivity);

                        } else {

                            DialogUtil.showDisCoverNetToast(mActivity, apiResp.getMsg());

                        }

                    });

        } else if (videoType == 1) {//认证视频
            loadingDiaog.dismiss();
            Bundle bundle = new Bundle();
            bundle.putInt(YpSettings.USERID, targetId);
            ActivityUtil.jump(getActivity(), VideoDetailGetActivity.class, bundle, 0, 100);
        }

        if (subscription != null) {

            addSubscrebe(subscription);

        }
    }
}
