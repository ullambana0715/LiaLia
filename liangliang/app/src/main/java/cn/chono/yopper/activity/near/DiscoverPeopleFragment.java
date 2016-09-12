package cn.chono.yopper.activity.near;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
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
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UsersNearby.UsersNearbyBean;
import cn.chono.yopper.Service.Http.UsersNearby.UsersNearbyRespBean;
import cn.chono.yopper.Service.Http.UsersNearby.UsersNearbyService;
import cn.chono.yopper.Service.Http.UsersNearbyMore.UsersNearbyMoreBean;
import cn.chono.yopper.Service.Http.UsersNearbyMore.UsersNearbyMoreRespBean;
import cn.chono.yopper.Service.Http.UsersNearbyMore.UsersNearbyMoreService;
import cn.chono.yopper.YpBaseFragment;
import cn.chono.yopper.activity.base.IndexActivity;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.activity.find.ClimbingListActivity;
import cn.chono.yopper.adapter.DiscoversPeopleAdapter;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.DiscoverPeopleDto;
import cn.chono.yopper.data.DiscoverPeopleLocalDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.NearPeopleDto;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.event.OnNearPeopleFilterEvent;
import cn.chono.yopper.event.OnNearTabEvent;
import cn.chono.yopper.event.OnTopEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.recyclerview.AgileDividerLookup;
import cn.chono.yopper.recyclerview.DividerItemDecoration;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.ui.UserInfoEditActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.UserInfoUtils;
import cn.chono.yopper.utils.ViewsUtils;

public class DiscoverPeopleFragment extends YpBaseFragment implements DiscoversPeopleAdapter.OnItemClickLitener, OnClickListener {

    private XRefreshView xrefreshView;
    private RecyclerView discoverPeople_rlv;

    private DiscoversPeopleAdapter mDiscoversPeopleAdapter;

    /**
     * 筛选的性别，0--无限 ;1--男;2--女
     */
    private int peopleFilterType = 0;


    private double latitude = 31.240517;
    private double longtitude = 121.478844;


    private boolean haveFilterData = false;
    private boolean haveFilterListData = false;
    private boolean haveGpsTag;

    private String nextQuery;

    private ViewStub discover_people_error_no_data_vs;

    private ViewStub discover_people_error_location_vs;

    private ViewStub discover_people_error_network_vs;

    private ImageView discover_climbing_iv;

    private int start = 0;


    private UserDto userdto;

    private Dialog album_dialog;
    private Dialog status_dialog;

    private List<NearPeopleDto> time_being_list = new ArrayList<>();

    private int time_being_screen;

    private LocInfo myLoc;

    private UsersNearbyService nearbyService;

    private UsersNearbyMoreService moreService;

    private boolean goSetLocationg = false;

    private View contentView;

    XRefreshViewHeaders mXRefreshViewHeaders;

    private boolean isRefreshing;

    private int tab_Id;

    private Dialog loadingDiaog;

    private boolean isinitData = false;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.discover_people_fragment, container, false);
        PushAgent.getInstance(getActivity()).onAppStart();

        //loading
        loadingDiaog = DialogUtil.LoadingDialog(getActivity(), null);

        RxBus.get().register(this);

        initView(contentView);

        setXrefreshViewListener();

        peopleFilterType = DbHelperUtils.getPeopleFilter(LoginUser.getInstance().getUserId());

        time_being_screen = peopleFilterType;

        tab_Id = SharedprefUtil.getInt(getActivity(), YpSettings.DISCOVER_TAB_ID, 0);

        if (tab_Id == 0 && !isinitData) {
            isinitData = true;
            initData();
            getLocAndInit();
        }

//        Glide.with(getActivity()).resumeRequests();

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

            MobclickAgent.onPageStart("附近 (人)"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
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

            discover_people_error_network_vs.setVisibility(View.GONE);

            if (haveFilterData) {
                discover_people_error_location_vs.setVisibility(View.GONE);
                if (haveFilterListData) {
                    xrefreshView.setVisibility(View.VISIBLE);
                    discover_people_error_no_data_vs.setVisibility(View.GONE);
                } else {
                    xrefreshView.setVisibility(View.GONE);
                    handleNoDataError();
                }
                locDialog = DialogUtil.createHintOperateDialog(getActivity(), "定位服务未开启", "开启定位服务以看到附近用户", "取消", "立即开启", locbackCallListener);
                locDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

                if (!getActivity().isFinishing()) {
                    locDialog.show();
                }


            } else {
                handleLocationError();
                xrefreshView.setVisibility(View.GONE);
                discover_people_error_no_data_vs.setVisibility(View.GONE);
            }

        } else {
            discover_people_error_location_vs.setVisibility(View.GONE);
            discover_people_error_network_vs.setVisibility(View.GONE);

            if (haveFilterData) {
                if (haveFilterListData) {
                    xrefreshView.setVisibility(View.VISIBLE);
                    discover_people_error_no_data_vs.setVisibility(View.GONE);
                } else {
                    xrefreshView.setVisibility(View.GONE);
                    handleNoDataError();

                    if (!getActivity().isFinishing()) {
                        loadingDiaog.show();
                    }
                }

            } else {
                xrefreshView.setVisibility(View.GONE);
                discover_people_error_no_data_vs.setVisibility(View.GONE);
                //动画
                if (!getActivity().isFinishing()) {
                    loadingDiaog.show();
                }
            }
            getLocinfo();
        }
    }

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

    /**
     * 定位未开启dialog
     */

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


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("附近 (人)"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(getActivity()); // 统计时长
    }


    private void initView(View contentView) {

        xrefreshView = (XRefreshView) contentView.findViewById(R.id.xrefreshview);

        discoverPeople_rlv = (RecyclerView) contentView.findViewById(R.id.discoverPeople_rlv);

        DividerItemDecoration itemDecoration = new DividerItemDecoration();

        itemDecoration.setDividerLookup(new AgileDividerLookup());

        discoverPeople_rlv.addItemDecoration(itemDecoration);

        discoverPeople_rlv.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mDiscoversPeopleAdapter = new DiscoversPeopleAdapter(getActivity());

        discoverPeople_rlv.setAdapter(mDiscoversPeopleAdapter);

        mDiscoversPeopleAdapter.setOnItemClickLitener(this);


        discover_people_error_location_vs = (ViewStub) contentView.findViewById(R.id.discover_people_error_location_vs);

        discover_people_error_network_vs = (ViewStub) contentView.findViewById(R.id.discover_people_error_network_vs);

        discover_people_error_no_data_vs = (ViewStub) contentView.findViewById(R.id.discover_people_error_no_data_vs);


        discover_climbing_iv = (ImageView) contentView.findViewById(R.id.discover_climbing_iv);

        discover_climbing_iv.setOnClickListener(this);

    }

    private void handleNoDataError() {

        discover_people_error_no_data_vs.setVisibility(View.VISIBLE);

        LinearLayout error_no_data_layout = (LinearLayout) contentView.findViewById(R.id.error_no_data_layout);
        error_no_data_layout.setVisibility(View.VISIBLE);

        TextView error_no_data_tv = (TextView) contentView.findViewById(R.id.error_no_data_tv);

        error_no_data_tv.setText(R.string.error_hini);
    }


    private void handleLocationError() {

        discover_people_error_location_vs.setVisibility(View.VISIBLE);

        LinearLayout error_location_layout = (LinearLayout) contentView.findViewById(R.id.error_location_layout);
        error_location_layout.setVisibility(View.VISIBLE);

        TextView error_location_tv = (TextView) contentView.findViewById(R.id.error_location_tv);

        error_location_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);
                try {
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    getActivity().startActivity(intent);
                }
            }
        });
    }


    private void handleNetError() {

        discover_people_error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) contentView.findViewById(R.id.error_network_layout);
        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) contentView.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);

                discover_people_error_network_vs.setVisibility(View.GONE);
                discover_people_error_location_vs.setVisibility(View.GONE);
                discover_people_error_no_data_vs.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.GONE);
                start = 0;


                if (!getActivity().isFinishing()) {
                    loadingDiaog.show();
                }
                onRefreshData(start);
            }
        });
    }


    private void setXrefreshViewListener() {

        discoverPeople_rlv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Logger.e("滑动状态＝" + newState);

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


        discoverPeople_rlv.setHasFixedSize(true);

        xrefreshView.setPullLoadEnable(true);

        // 设置静默加载模式
        xrefreshView.setSlienceLoadMore();
        // 静默加载模式不能设置footerview


        xrefreshView.setMoveForHorizontal(true);


        mXRefreshViewHeaders = new XRefreshViewHeaders(getActivity());

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);


        // 处理ViewPager冲突
        xrefreshView.setMoveForHorizontal(true);
        // 设置静默加载时提前加载的item个数
        xrefreshView.setPreLoadCount(9);


        xrefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start = 0;
                        onRefreshData(start);


                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().post(new Runnable() {
                    public void run() {

                        onLoadMoreData();

                    }
                });

            }

        });

    }

    private void initData() {

        String dataStr = getPeopleDataFromDB(LoginUser.getInstance().getUserId());
        if (!TextUtils.isEmpty(dataStr)) {// 获取到保存的数据字符串
            DiscoverPeopleDto peropleDto = JsonUtils.fromJson(dataStr, DiscoverPeopleDto.class);

            if (peropleDto != null) {// 对象转换，有数据

                List<NearPeopleDto> list = peropleDto.getList();
                haveFilterData = true;
                if (list != null && list.size() > 0) {// 列表有数据
                    haveFilterListData = true;
                    xrefreshView.setVisibility(View.VISIBLE);
                    discover_people_error_no_data_vs.setVisibility(View.GONE);

                    nextQuery = peropleDto.getNextQuery();
                    mDiscoversPeopleAdapter.setData(list);

                    mDiscoversPeopleAdapter.notifyDataSetChanged();

                } else {

                    haveFilterListData = false;
                    handleNoDataError();
                    xrefreshView.setVisibility(View.GONE);

                }

            } else {// 对象转换异常，没有本地数据
                haveFilterData = false;
                haveFilterListData = false;
                xrefreshView.setVisibility(View.GONE);

                if (!getActivity().isFinishing()) {
                    loadingDiaog.show();
                }
            }

        } else {// 为空就是没有数据
            haveFilterData = false;
            haveFilterListData = false;
            // 没有本地数据
            xrefreshView.setVisibility(View.GONE);

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
                            xrefreshView.setVisibility(View.VISIBLE);
                            discover_people_error_no_data_vs.setVisibility(View.GONE);
                        } else {
                            xrefreshView.setVisibility(View.GONE);
                            handleNoDataError();
                        }
                        //提示未开启定位
//						DialogUtil.showDisCoverNetToast(getActivity(),"获取位置失败");
                    } else {
                        xrefreshView.setVisibility(View.GONE);
                        discover_people_error_no_data_vs.setVisibility(View.GONE);
                        handleLocationError();
                    }
                    onRefreshData(start);

                } else if (msg.what == 1) {// 找到位置

                    latitude = myLoc.getLoc().getLatitude();
                    longtitude = myLoc.getLoc().getLongitude();
                    start = 0;
                    onRefreshData(start);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 上拉加载请求方法
     */
    private void onLoadMoreData() {


        if (TextUtils.isEmpty(nextQuery)) {
            xrefreshView.setLoadComplete(true);
            return;
        }


        UsersNearbyMoreBean nearbyMoreBean = new UsersNearbyMoreBean();
        nearbyMoreBean.setNextQuery(nextQuery);

        moreService = new UsersNearbyMoreService(getActivity());
        moreService.parameter(nearbyMoreBean);
        moreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UsersNearbyMoreRespBean moreRespBean = (UsersNearbyMoreRespBean) respBean;

                DiscoverPeopleDto peropleDto = moreRespBean.getResp();


                if (peropleDto != null) {
                    List<NearPeopleDto> list = peropleDto.getList();

                    nextQuery = peropleDto.getNextQuery();


                    if (list != null && list.size() > 0) {// 列表有数据

                        mDiscoversPeopleAdapter.addData(list);

                        mDiscoversPeopleAdapter.notifyDataSetChanged();

                    }


                    if (TextUtils.isEmpty(nextQuery)) {
                        xrefreshView.setLoadComplete(true);
                        return;
                    }

                    xrefreshView.stopLoadMore();

                    return;

                }
                xrefreshView.stopLoadMore();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                xrefreshView.stopLoadMore();


                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                discover_people_error_location_vs.setVisibility(View.GONE);
                discover_people_error_network_vs.setVisibility(View.GONE);
                discover_people_error_no_data_vs.setVisibility(View.GONE);

                // 提示连接失败
                DialogUtil.showDisCoverNetToast(getActivity());

                //请求失败需要去重置性别筛选条件的文本显示
                RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));
            }
        });
        moreService.enqueue();

        handlePeopleLimitView(LoginUser.getInstance().getUserId(), peopleFilterType);

    }

    /**
     * 下拉加载更多
     *
     * @param start
     */
    private void onRefreshData(final int start) {

        if (isRefreshing) {
            return;
        }

        xrefreshView.setLoadComplete(false);

        isRefreshing = true;

        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            latitude = myLoc.getLoc().getLatitude();
            longtitude = myLoc.getLoc().getLongitude();
        }

        if (latitude == 0 && longtitude == 0) {

            mXRefreshViewHeaders.onRefreshFail();

            xrefreshView.stopRefresh();
            isRefreshing = false;
            if (loadingDiaog != null) {
                loadingDiaog.dismiss();
            }
            return;
        }

        LatLng pt = new LatLng(latitude, longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);


        UsersNearbyBean nearbyBean = new UsersNearbyBean();
        nearbyBean.setLng(pt.longitude);
        nearbyBean.setLat(pt.latitude);
        nearbyBean.setTime(TimeUtil.getCurrentTimeMillis());


        nearbyBean.setStart(start);


        if (peopleFilterType == 3) {
            nearbyBean.setLevel(1);
            nearbyBean.setSex(1);
        } else {
            nearbyBean.setSex(peopleFilterType);
        }

        nearbyService = new UsersNearbyService(getActivity());

        nearbyService.parameter(nearbyBean);

        nearbyService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                isRefreshing = false;
                UsersNearbyRespBean moreRespBean = (UsersNearbyRespBean) respBean;

                DiscoverPeopleDto peropleDto = moreRespBean.getResp();
                String responseStr = JsonUtils.toJson(peropleDto);


                // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                // 如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                if (loadingDiaog != null) {
                    loadingDiaog.dismiss();
                }

                discover_people_error_location_vs.setVisibility(View.GONE);
                discover_people_error_network_vs.setVisibility(View.GONE);


                if (peropleDto != null) {

                    haveFilterData = true;
                    List<NearPeopleDto> list = peropleDto.getList();
                    if (list != null && list.size() > 0) {// 列表有数据
                        haveFilterListData = true;
                        discover_people_error_no_data_vs.setVisibility(View.GONE);
                        xrefreshView.setVisibility(View.VISIBLE);
                        nextQuery = peropleDto.getNextQuery();


                        mDiscoversPeopleAdapter.setData(list);

                        mDiscoversPeopleAdapter.notifyDataSetChanged();


                    } else {
                        haveFilterListData = false;
                        handleNoDataError();
                        xrefreshView.setVisibility(View.GONE);

                    }

                    AsyncTask<Void, Void, Integer> task = saveDataLocal(LoginUser.getInstance().getUserId(), responseStr);
                    task.execute();// 执行保存数据
                } else {
                    haveFilterData = false;
                    haveFilterListData = false;
                    handleNoDataError();
                    xrefreshView.setVisibility(View.GONE);

                }


                xrefreshView.stopRefresh();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                isRefreshing = false;

                mXRefreshViewHeaders.onRefreshFail();
                xrefreshView.stopRefresh();

                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                if (loadingDiaog != null) {
                    loadingDiaog.dismiss();
                }
                RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));

                discover_people_error_location_vs.setVisibility(View.GONE);

                if (haveFilterData) {
                    if (haveFilterListData) {
                        xrefreshView.setVisibility(View.VISIBLE);
                        discover_people_error_no_data_vs.setVisibility(View.GONE);
                    } else {
                        xrefreshView.setVisibility(View.GONE);
                        handleNoDataError();
                    }

                    discover_people_error_network_vs.setVisibility(View.GONE);
                    // 提示连接失败
                    DialogUtil.showDisCoverNetToast(getActivity());
                } else {
                    handleNetError();
                    xrefreshView.setVisibility(View.GONE);
                    discover_people_error_no_data_vs.setVisibility(View.GONE);
                }

            }
        });
        nearbyService.enqueue();
        handlePeopleLimitView(LoginUser.getInstance().getUserId(), peopleFilterType);

    }


    @Override
    public void onItemClick(View view, int position, int userID) {
        Bundle userbundle = new Bundle();
        userbundle.putInt(YpSettings.USERID, userID);
        ActivityUtil.jump(getActivity(), UserInfoActivity.class, userbundle, 0, 100);

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
                    DiscoverPeopleLocalDto localDto = App.db.findFirst(Selector.from(DiscoverPeopleLocalDto.class).where("userId", " =", userId));

                    if (localDto != null) {
                        localDto.setUserId(LoginUser.getInstance().getUserId());
                        localDto.setData(data);
                        localDto.setTime(System.currentTimeMillis());
                        App.db.update(localDto);
                    } else {
                        localDto = new DiscoverPeopleLocalDto();
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
     * 获取缓存数据
     *
     * @param userId
     * @return
     */

    private String getPeopleDataFromDB(int userId) {

        try {
            DiscoverPeopleLocalDto localDto = App.db.findFirst(Selector.from(DiscoverPeopleLocalDto.class).where("userId", " =", userId));
            if (localDto == null) {
                return null;
            }

            return localDto.getData();

        } catch (DbException e) {
            e.printStackTrace();

        }

        return null;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.discover_climbing_iv://爬行榜

                MobclickAgent.onEvent(getActivity(), "btn_find_event_climblist");
                ActivityUtil.jump(getActivity(), ClimbingListActivity.class, null, 0, 100);

                break;

            default:
                break;
        }

    }


    /**
     * 根据附近人刷选条件 判断是否显示限制视图
     *
     * @param userid
     */
    private void handlePeopleLimitView(int userid, int filterType) {
        time_being_list = mDiscoversPeopleAdapter.getData();

        if (3 == filterType) {
            UserInfo userInfo = DbHelperUtils.getUserInfo(userid);

            if (userInfo != null) {
                userdto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
                if (userdto != null) {
                    int sex = userdto.getProfile().getSex();

                    List<String> album = UserInfoUtils.userPhotoToAlbum(userdto);
                    int status = userdto.getProfile().getStatus();
                    if (sex == 1) {//男性


                    } else {//女性

                        if (null != album && album.size() < 4) {//相册不足4张


                            album_dialog = DialogUtil.createHintOperateDialog(getActivity(), "", "至少上传4张个人照片才能继续查看", "取消", "立即上传", new AlbumDialogBackCallListener());
                            album_dialog.setCancelable(true);
                            album_dialog.setCanceledOnTouchOutside(false);
                            album_dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                        peopleFilterType = time_being_screen;
                                        RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));
                                        mDiscoversPeopleAdapter.setData(time_being_list);
                                        mDiscoversPeopleAdapter.notifyDataSetChanged();
                                        album_dialog.dismiss();
                                    }

                                    return false;
                                }
                            });

                            album_dialog.show();


                        } else if (((status >> 0) & 1) == 0) {//头像没审核通过

                            status_dialog = DialogUtil.createHintOperateDialog(getActivity(), "", "通过头像审核才能继续查看", "查看帮助", "确定", new StatusDialogBackCallListener());
                            status_dialog.setCancelable(true);
                            status_dialog.setCanceledOnTouchOutside(false);
                            status_dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                        peopleFilterType = time_being_screen;
                                        RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));
                                        mDiscoversPeopleAdapter.setData(time_being_list);
                                        mDiscoversPeopleAdapter.notifyDataSetChanged();
                                        status_dialog.dismiss();
                                    }

                                    return false;
                                }
                            });
                            status_dialog.show();

                        }


                    }

                }
            }
        }


    }


    /**
     * 优质男没有上传4张相册图片  的提示回调
     */
    private class AlbumDialogBackCallListener implements BackCallListener {

        @Override
        public void onEnsure(View view, Object... obj) {

            if (null != nearbyService) {

                nearbyService.cancel();
            }
            if (null != moreService) {

                moreService.cancel();
            }

            peopleFilterType = time_being_screen;

            RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));

            mDiscoversPeopleAdapter.setData(time_being_list);

            mDiscoversPeopleAdapter.notifyDataSetChanged();

            Bundle userbundle = new Bundle();

            userbundle.putInt(YpSettings.USERID, LoginUser.getInstance().getUserId());

            ActivityUtil.jump(getActivity(), UserInfoEditActivity.class, userbundle, 0, 100);

            album_dialog.dismiss();
        }

        @Override
        public void onCancel(View view, Object... obj) {

            if (null != nearbyService) {

                nearbyService.cancel();

            }
            if (null != moreService) {

                moreService.cancel();

            }
            peopleFilterType = time_being_screen;

            RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));

            mDiscoversPeopleAdapter.setData(time_being_list);

            mDiscoversPeopleAdapter.notifyDataSetChanged();

            album_dialog.dismiss();
        }
    }

    /**
     * 优质男没有通过 头像审核弹出 的提示回调
     */
    private class StatusDialogBackCallListener implements BackCallListener {

        @Override
        public void onEnsure(View view, Object... obj) {
            if (null != nearbyService) {
                nearbyService.cancel();
            }
            if (null != moreService) {
                moreService.cancel();
            }


            peopleFilterType = time_being_screen;

            RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));

            mDiscoversPeopleAdapter.setData(time_being_list);

            mDiscoversPeopleAdapter.notifyDataSetChanged();

            status_dialog.dismiss();

        }

        @Override
        public void onCancel(View view, Object... obj) {

            if (null != nearbyService) {

                nearbyService.cancel();
            }
            if (null != moreService) {

                moreService.cancel();
            }
            peopleFilterType = time_being_screen;

            RxBus.get().post("OnNearPeopleFilterEvent", new OnNearPeopleFilterEvent(2, time_being_screen));

            mDiscoversPeopleAdapter.setData(time_being_list);

            mDiscoversPeopleAdapter.notifyDataSetChanged();
            //跳转到web 查看帮助

            Bundle bundle = new Bundle();

            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Standard/AvatarAudit");

            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "头像审核规范");

            bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

            ActivityUtil.jump(getActivity(), SimpleWebViewActivity.class, bundle, 0, 100);

            status_dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister(this);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OnTopEvent")

            }
    )
    public void onTopEvent(OnTopEvent event) {

//        LogUtils.e("来了乐乐乐安达假两件");
        if (event.getEventType() == 0) {
//        discoverPeople_rlv.sets(0);
        }
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OnNearTabEvent")

            }
    )
    public void onNearTabEvent(OnNearTabEvent event) {

        if (event.getEventType() == 1 && !isinitData) {
            isinitData = true;
            initData();
            getLocAndInit();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OnNearPeopleFilterEvent")

            }
    )
    public void onNearPeopleFilterEvent(OnNearPeopleFilterEvent event) {

        int filterProcess = event.getFilterProcess();

        if (filterProcess == 1) {
            // 筛选的性别，0--无限 ;1--男;2--女

            time_being_screen = peopleFilterType;

            peopleFilterType = event.getPeopleFilterType();

            start = 0;

            if (xrefreshView.getVisibility() == View.GONE) {

                onRefreshData(start);

            } else {

                xrefreshView.stopRefresh();

                xrefreshView.startRefresh();

            }
        }

    }

}
