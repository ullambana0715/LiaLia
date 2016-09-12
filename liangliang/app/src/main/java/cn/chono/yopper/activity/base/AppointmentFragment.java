package cn.chono.yopper.activity.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingsList.DatingListBean;
import cn.chono.yopper.Service.Http.DatingsList.DatingListRespBean;
import cn.chono.yopper.Service.Http.DatingsList.DatingListService;
import cn.chono.yopper.Service.Http.DatingsListMore.DatingListMoreBean;
import cn.chono.yopper.Service.Http.DatingsListMore.DatingListMoreService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;

import cn.chono.yopper.base.App;
import cn.chono.yopper.YpBaseFragment;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.appointment.DatingDetailActivity;
import cn.chono.yopper.adapter.AppointListAdapter;
import cn.chono.yopper.data.AppointFilterDto;
import cn.chono.yopper.data.AppointListDto;
import cn.chono.yopper.data.AppointmentDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.event.DatingsFilterEvent;
import cn.chono.yopper.event.DatingsRefreshEvent;
import cn.chono.yopper.event.OnNearTabEvent;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.SharedprefUtil;

public class AppointmentFragment extends YpBaseFragment implements OnClickListener, AppointListAdapter.OnItemClickLitener {


    private RelativeLayout dating_type_all_layout;
    private LinearLayout dating_all_type_select_layout;

    private RelativeLayout dating_type_married_layout;
    private LinearLayout dating_married_select_layout;

    private RelativeLayout dating_type_travel_layout;
    private LinearLayout dating_travel_select_layout;

    private RelativeLayout dating_type_eat_layout;
    private LinearLayout dating_eat_select_layout;

    private RelativeLayout dating_type_movie_layout;
    private LinearLayout dating_movie_select_layout;

    private RelativeLayout dating_type_ktv_layout;
    private LinearLayout dating_ktv_select_layout;

    private RelativeLayout dating_type_fitness_layout;
    private LinearLayout dating_fitness_select_layout;

    private RelativeLayout dating_type_dog_layout;
    private LinearLayout dating_dog_select_layout;

    private RelativeLayout dating_type_others_layout;
    private LinearLayout dating_others_select_layout;

    private int appointType;
    private int sexType;
    private int sortType;
    private String firstArea = "";
    private String secondArea = "";

    private boolean isall = false;

    private View convertView;

    private RecyclerView mRecyclerView;

    private AppointListAdapter mAdapter;

    private XRefreshView xrefreshView;

    private int userId;

    private boolean haveFilterData = false;

    private boolean haveFilterListData = false;


    private double lat = 0;

    private double lng = 0;


    private String nextQuery;

    private boolean haveGpsTag;

    private TextView error_no_data_title;
    private TextView error_no_data_tv;

    private ViewStub datings_error_network_vs;

    private ViewStub datings_error_location_vs;

    private LinearLayout error_no_data_layout;

    private boolean haveGeting = false;

    private LocInfo myLoc;

    private boolean goSetLocationg = false;

    private int loginUserSex;

    private Dialog loadingDiaog;

    XRefreshViewHeaders mXRefreshViewHeaders;

    private boolean isinitData = false;

    private int tab_Id;

    XRefreshViewFooters mXRefreshViewFooters;

    public AppointmentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        convertView = inflater.inflate(R.layout.appointment_fragment_layout, container, false);

        userId = LoginUser.getInstance().getUserId();

        loginUserSex = DbHelperUtils.getDbUserSex(userId);

        loadingDiaog = DialogUtil.LoadingDialog(getActivity(), null);

        initView(convertView);
        RxBus.get().register(this);

        tab_Id = SharedprefUtil.getInt(getActivity(), YpSettings.DISCOVER_TAB_ID, 0);

        if (tab_Id == 1 && !isinitData) {
            isinitData = true;
            initData();
            getLocAndInit();
        }
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (goSetLocationg) {
            getLocAndInit();
        }
    }


    private void getLocAndInit() {
        // 获得位置
        haveGpsTag = Loc.isGpsAvailable();
        if (!haveGpsTag) {
            //未开启定位权限
            datings_error_network_vs.setVisibility(View.GONE);


            if (haveFilterData) {
                datings_error_location_vs.setVisibility(View.GONE);
                if (haveFilterListData) {
                    xrefreshView.setVisibility(View.VISIBLE);
                    error_no_data_layout.setVisibility(View.GONE);
                } else {
                    xrefreshView.setVisibility(View.GONE);
                    error_no_data_layout.setVisibility(View.VISIBLE);

                }
                //提示未开启定位
                //showDialog();
                locDialog = DialogUtil.createHintOperateDialog(getActivity(), "定位服务未开启", "开启定位服务以看到附近用户", "取消", "立即开启", locationbackCallListener);
                locDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                if (!getActivity().isFinishing()) {
                    locDialog.show();
                }


            } else {
                handleLocationError();
                xrefreshView.setVisibility(View.GONE);
                error_no_data_layout.setVisibility(View.GONE);
            }

        } else {
            datings_error_location_vs.setVisibility(View.GONE);
            datings_error_network_vs.setVisibility(View.GONE);

            if (haveFilterData) {
                if (haveFilterListData) {
                    xrefreshView.setVisibility(View.VISIBLE);
                    error_no_data_layout.setVisibility(View.GONE);
                } else {
                    xrefreshView.setVisibility(View.GONE);
                    error_no_data_layout.setVisibility(View.VISIBLE);
                    if (!getActivity().isFinishing()) {
                        loadingDiaog.show();
                    }
                }

            } else {
                xrefreshView.setVisibility(View.GONE);
                error_no_data_layout.setVisibility(View.GONE);
                //loading
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

    @Override
    public void onPause() {
        super.onPause();
        Loc.sendLocControlMessage(false);
    }

    private void initView(View view) {


        dating_type_all_layout = (RelativeLayout) view.findViewById(R.id.dating_type_all_layout);
        dating_type_all_layout.setOnClickListener(this);
        dating_all_type_select_layout = (LinearLayout) view.findViewById(R.id.dating_all_type_select_layout);


        dating_type_married_layout = (RelativeLayout) view.findViewById(R.id.dating_type_married_layout);
        dating_type_married_layout.setOnClickListener(this);
        dating_married_select_layout = (LinearLayout) view.findViewById(R.id.dating_married_select_layout);


        dating_type_travel_layout = (RelativeLayout) view.findViewById(R.id.dating_type_travel_layout);
        dating_type_travel_layout.setOnClickListener(this);
        dating_travel_select_layout = (LinearLayout) view.findViewById(R.id.dating_travel_select_layout);


        dating_type_eat_layout = (RelativeLayout) view.findViewById(R.id.dating_type_eat_layout);
        dating_type_eat_layout.setOnClickListener(this);
        dating_eat_select_layout = (LinearLayout) view.findViewById(R.id.dating_eat_select_layout);


        dating_type_movie_layout = (RelativeLayout) view.findViewById(R.id.dating_type_movie_layout);
        dating_type_movie_layout.setOnClickListener(this);
        dating_movie_select_layout = (LinearLayout) view.findViewById(R.id.dating_movie_select_layout);


        dating_type_ktv_layout = (RelativeLayout) view.findViewById(R.id.dating_type_ktv_layout);
        dating_type_ktv_layout.setOnClickListener(this);
        dating_ktv_select_layout = (LinearLayout) view.findViewById(R.id.dating_ktv_select_layout);


        dating_type_fitness_layout = (RelativeLayout) view.findViewById(R.id.dating_type_fitness_layout);
        dating_type_fitness_layout.setOnClickListener(this);
        dating_fitness_select_layout = (LinearLayout) view.findViewById(R.id.dating_fitness_select_layout);


        dating_type_dog_layout = (RelativeLayout) view.findViewById(R.id.dating_type_dog_layout);
        dating_type_dog_layout.setOnClickListener(this);
        dating_dog_select_layout = (LinearLayout) view.findViewById(R.id.dating_dog_select_layout);

        dating_type_others_layout = (RelativeLayout) view.findViewById(R.id.dating_type_others_layout);
        dating_type_others_layout.setOnClickListener(this);
        dating_others_select_layout = (LinearLayout) view.findViewById(R.id.dating_others_select_layout);


        datings_error_network_vs = (ViewStub) convertView.findViewById(R.id.datings_error_network_vs);

        datings_error_location_vs = (ViewStub) convertView.findViewById(R.id.datings_error_location_vs);


        error_no_data_tv = (TextView) view.findViewById(R.id.error_no_data_tv);
        error_no_data_title = (TextView) view.findViewById(R.id.error_no_data_title);

        error_no_data_layout = (LinearLayout) view.findViewById(R.id.error_no_data_layout);

        error_no_data_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isall) {
                    appointType = 0;
                    sexType = 0;
                    sortType = 0;
                    firstArea = "";
                    secondArea = "";
                    datingTypeSelect(appointType);
                    getAppointList();
                }
            }
        });

        xrefreshView = (XRefreshView) view.findViewById(R.id.appoint_xrefreshview);
        xrefreshView.setAutoLoadMore(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.appoint_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AppointListAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(this);
        xrefreshView.setVisibility(View.GONE);

        setXrefreshListener();

    }


    private void handleLocationError() {

        datings_error_location_vs.setVisibility(View.VISIBLE);

        LinearLayout error_location_layout = (LinearLayout) convertView.findViewById(R.id.error_location_layout);
        error_location_layout.setVisibility(View.VISIBLE);

        TextView error_location_tv = (TextView) convertView.findViewById(R.id.error_location_tv);

        error_location_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        datings_error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) convertView.findViewById(R.id.error_network_layout);
        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) convertView.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAppointList();
            }
        });
    }


    protected void initData() {
        getAppointFilter();

        String filterStr = DbHelperUtils.getAppointV3FilterList(userId);
        if (!CheckUtil.isEmpty(filterStr)) {

            AppointListDto nearAppointListDto = new AppointListDto();
            nearAppointListDto = JsonUtils.fromJson(filterStr, AppointListDto.class);

            if (nearAppointListDto != null) {
                List<AppointmentDto> list = new ArrayList<AppointmentDto>();
                list = nearAppointListDto.getList();
                haveFilterData = true;


                if (list != null && list.size() > 0) {
                    xrefreshView.setVisibility(View.VISIBLE);
                    error_no_data_layout.setVisibility(View.GONE);
                    haveFilterListData = true;
                    nextQuery = nearAppointListDto.getNextQuery();
                    mAdapter.setData(nearAppointListDto.getList());
                    mAdapter.notifyDataSetChanged();
                } else {

                    haveFilterListData = false;
                    error_no_data_layout.setVisibility(View.VISIBLE);
                    xrefreshView.setVisibility(View.GONE);
                }

            } else {
                //没有本地数据
                xrefreshView.setVisibility(View.GONE);
                //loading
                if (!getActivity().isFinishing()) {
                    loadingDiaog.show();
                }
                haveFilterData = false;
                haveFilterListData = false;

            }

        } else {
            //没有本地数据
            xrefreshView.setVisibility(View.GONE);
            //loading
            if (!getActivity().isFinishing()) {
                loadingDiaog.show();
            }
            haveFilterData = false;
            haveFilterListData = false;
        }

        datingTypeSelect(appointType);

        if (xrefreshView.getVisibility() == View.GONE) {
            getAppointList();
        } else {
            xrefreshView.startRefresh();
        }

//        getAppointList(location);
    }


    private void setXrefreshListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        mXRefreshViewHeaders = new XRefreshViewHeaders(getActivity());

        mXRefreshViewFooters = new XRefreshViewFooters(getActivity());

        mXRefreshViewFooters.setRecyclerView(mRecyclerView);

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);

        mAdapter.setCustomLoadMoreView(mXRefreshViewFooters);


        xrefreshView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xrefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        xrefreshView.setAutoLoadMore(true);


        mXRefreshViewFooters.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        loadMoregetNearAppointList();
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

                        loadMoregetNearAppointList();
                    }
                }, 1000);
            }
        });


        xrefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getAppointList();

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        loadMoregetNearAppointList();

                    }
                }, 1000);
            }

        });


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.dating_type_all_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_NO_LIMIT);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_married_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_MARRIED);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_travel_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_TRAVEL);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_eat_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_EAT);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_movie_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_MOVIE);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_ktv_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_KTV);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_fitness_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_FITNESS);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_dog_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_DOG);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            case R.id.dating_type_others_layout:
                datingTypeSelect(Constant.APPOINT_TYPE_OTHERS);
                if (xrefreshView.getVisibility() == View.GONE) {
                    getAppointList();
                } else {
                    xrefreshView.startRefresh();
                }
                break;

            default:
                break;
        }
    }


    private void datingTypeSelect(int datingtype) {


        switch (datingtype) {
            case Constant.APPOINT_TYPE_NO_LIMIT:

                appointType = Constant.APPOINT_TYPE_NO_LIMIT;

                dating_all_type_select_layout.setVisibility(View.VISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;

            case Constant.APPOINT_TYPE_MARRIED:

                appointType = Constant.APPOINT_TYPE_MARRIED;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.VISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;

            case Constant.APPOINT_TYPE_TRAVEL:

                appointType = Constant.APPOINT_TYPE_TRAVEL;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.VISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;

            case Constant.APPOINT_TYPE_MOVIE:

                appointType = Constant.APPOINT_TYPE_MOVIE;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.VISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;

            case Constant.APPOINT_TYPE_EAT:

                appointType = Constant.APPOINT_TYPE_EAT;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.VISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;

            case Constant.APPOINT_TYPE_FITNESS:

                appointType = Constant.APPOINT_TYPE_FITNESS;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.VISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;

            case Constant.APPOINT_TYPE_DOG:

                appointType = Constant.APPOINT_TYPE_DOG;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.VISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;

            case Constant.APPOINT_TYPE_OTHERS:

                appointType = Constant.APPOINT_TYPE_OTHERS;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.INVISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.VISIBLE);

                break;

            case Constant.APPOINT_TYPE_KTV:

                appointType = Constant.APPOINT_TYPE_KTV;

                dating_all_type_select_layout.setVisibility(View.INVISIBLE);

                dating_married_select_layout.setVisibility(View.INVISIBLE);

                dating_travel_select_layout.setVisibility(View.INVISIBLE);

                dating_eat_select_layout.setVisibility(View.INVISIBLE);

                dating_movie_select_layout.setVisibility(View.INVISIBLE);

                dating_ktv_select_layout.setVisibility(View.VISIBLE);

                dating_fitness_select_layout.setVisibility(View.INVISIBLE);

                dating_dog_select_layout.setVisibility(View.INVISIBLE);

                dating_others_select_layout.setVisibility(View.INVISIBLE);

                break;
        }

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("DatingsFilterEvent")

            }

    )
    public void datingsFilterEvent(DatingsFilterEvent event) {

        appointType = event.getAppointtype();
        sexType = event.getSextype();
        sortType = event.getSorttype();

        firstArea = event.getFirstArea_str();
        secondArea = event.getSecondArea_str();


        if (xrefreshView.getVisibility() == View.GONE) {

            getAppointList();

        } else {
            xrefreshView.startRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister(this);
    }


    /**
     * 获取缓存筛选条件
     */


    private void getAppointFilter() {

        AppointFilterDto dto = DbHelperUtils.getAppointFilter(userId);
        if (dto != null) {
            appointType = dto.getDatingType();
            sexType = dto.getSexType();
            sortType = dto.getSortType();
            firstArea = dto.getFirstArea();
            secondArea = dto.getSecondArea();

        } else {
            appointType = 0;

            if (loginUserSex == 1) {
                sexType = 2;
            } else {
                sexType = 1;
            }

            sortType = 0;

            LocInfo myLoc = Loc.getLoc();
            if (myLoc != null && !CheckUtil.isEmpty(myLoc.getCity())) {
                firstArea = myLoc.getProvince();
                secondArea = myLoc.getCity();
            }

        }
    }


    private Handler LocHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {

                if (msg.what == 0) {//

                    loadingDiaog.dismiss();

                    if (haveFilterData) {
                        if (haveFilterListData) {
                            xrefreshView.setVisibility(View.VISIBLE);
                            error_no_data_layout.setVisibility(View.GONE);
                        } else {
                            xrefreshView.setVisibility(View.GONE);
                            error_no_data_layout.setVisibility(View.VISIBLE);
                        }
                        //提示未开启定位
//						DialogUtil.showDisCoverNetToast(getActivity(),"获取位置失败");
                    } else {
                        xrefreshView.setVisibility(View.GONE);
                        error_no_data_layout.setVisibility(View.GONE);
                        handleLocationError();
                    }

                } else if (msg.what == 1) {// 找到位置

                    lat = myLoc.getLoc().getLatitude();
                    lng = myLoc.getLoc().getLongitude();
                    getAppointList();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 定位未开启dialog
     */

    private Dialog locDialog;


    private BackCallListener locationbackCallListener = new BackCallListener() {
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

    /**
     * 获取约会列表
     */
    private void getAppointList() {


        if (haveGeting) {
            mXRefreshViewHeaders.onRefreshFail();
            xrefreshView.stopRefresh();
            return;
        }

        haveGeting = true;


        LocInfo myLoc = Loc.getLoc();
        if (myLoc != null && myLoc.getLoc() != null) {
            lat = myLoc.getLoc().getLatitude();
            lng = myLoc.getLoc().getLongitude();
        }


        LatLng pt = new LatLng(lat, lng);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        DatingListBean nearestBean = new DatingListBean();

        nearestBean.setLat(pt.latitude);
        nearestBean.setLng(pt.longitude);
        nearestBean.setSex(sexType);
        nearestBean.setSort(sortType);
        nearestBean.setType(appointType);
        nearestBean.setFirstArea(firstArea);
        nearestBean.setSecondArea(secondArea);

        if (sexType == 0 && sortType == 0 && appointType == Constant.APPOINT_TYPE_NO_LIMIT && CheckUtil.isEmpty(firstArea) && CheckUtil.isEmpty(secondArea)) {
            isall = true;
        } else {
            isall = false;
        }


        DatingListService nearestService = new DatingListService(getActivity());
        nearestService.parameter(nearestBean);
        nearestService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                //定位视图隐藏  网络加载失败视图隐藏-小圆圈视图隐藏
                //如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                DbHelperUtils.saveAppointFilter(appointType, sexType, sortType, firstArea, secondArea);

                haveGeting = false;
                loadingDiaog.dismiss();

                datings_error_location_vs.setVisibility(View.GONE);
                datings_error_network_vs.setVisibility(View.GONE);


                DatingListRespBean nearestRespBean = (DatingListRespBean) respBean;
                AppointListDto nearAppointListDto = nearestRespBean.getResp();
                String resp_str = JsonUtils.toJson(nearAppointListDto);

                List<AppointmentDto> list = new ArrayList<AppointmentDto>();


                if (nearAppointListDto != null) {
                    DbHelperUtils.saveAppointV3FilterList(userId, resp_str);

                    haveFilterData = true;

                    list = nearAppointListDto.getList();

                    if (list != null && list.size() > 0) {
                        error_no_data_layout.setVisibility(View.GONE);
                        xrefreshView.setVisibility(View.VISIBLE);
                        nextQuery = nearAppointListDto.getNextQuery();
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                        haveFilterListData = true;
                    } else {
                        error_no_data_layout.setVisibility(View.VISIBLE);
                        xrefreshView.setVisibility(View.GONE);
                        haveFilterListData = false;
                        if (isall) {
                            error_no_data_title.setText("暂时没有约会");
                            error_no_data_tv.setText("你有机会成为第一个发布约会的人");
                        } else {
                            error_no_data_title.setText("暂时没有发现符合条件的约会");
                            error_no_data_tv.setText("点击查看所有约会>");
                        }
                    }

                } else {
                    haveFilterData = false;
                    haveFilterListData = false;
                    xrefreshView.setVisibility(View.GONE);
                    error_no_data_layout.setVisibility(View.VISIBLE);
                    if (isall) {
                        error_no_data_title.setText("暂时没有约会");
                        error_no_data_tv.setText("你有机会成为第一个发布约会的人");
                    } else {
                        error_no_data_title.setText("暂时没有发现符合条件的约会");
                        error_no_data_tv.setText("点击查看所有约会>");
                    }

                }
                xrefreshView.stopRefresh();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                //定位视图隐藏  无数据视图隐藏-小圆圈视图隐藏-
                //如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                // 动画回收
                // 动画回收
                haveGeting = false;
                loadingDiaog.dismiss();

                datings_error_location_vs.setVisibility(View.GONE);


                if (haveFilterData) {
                    if (haveFilterListData) {
                        xrefreshView.setVisibility(View.VISIBLE);
                        error_no_data_layout.setVisibility(View.GONE);
                    } else {
                        xrefreshView.setVisibility(View.GONE);
                        error_no_data_layout.setVisibility(View.VISIBLE);
                    }

                    datings_error_network_vs.setVisibility(View.GONE);
                    //提示连接失败
                    DialogUtil.showDisCoverNetToast(getActivity());
                } else {
                    handleNetError();
                    xrefreshView.setVisibility(View.GONE);
                    error_no_data_layout.setVisibility(View.GONE);
                }

                mXRefreshViewHeaders.onRefreshFail();
                xrefreshView.stopRefresh();
            }
        });
        nearestService.enqueue();

    }


    /**
     * 获取约会列表
     */

    private void loadMoregetNearAppointList() {

        if (haveGeting) {
            //刷新完成必须调用此方法停止加载
            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshView.stopLoadMore(false);

            return;
        }

        if (CheckUtil.isEmpty(nextQuery)) {

            //刷新完成必须调用此方法停止加载
            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshView.setLoadComplete(false);
            return;
        }

        haveGeting = true;

        DatingListMoreBean nearestsBean = new DatingListMoreBean();
        nearestsBean.setNextQuery(nextQuery);
        DatingListMoreService nearestsService = new DatingListMoreService(getActivity());
        nearestsService.parameter(nearestsBean);
        nearestsService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                haveGeting = false;
                DatingListRespBean daingsNearestsRespBean = (DatingListRespBean) respBean;

                AppointListDto nearAppointListDto = daingsNearestsRespBean.getResp();

                List<AppointmentDto> list = new ArrayList<AppointmentDto>();

                if (nearAppointListDto != null) {

                    list = nearAppointListDto.getList();

                    if (list != null && list.size() > 0) {

                        nextQuery = nearAppointListDto.getNextQuery();
                        List<AppointmentDto> curlist = mAdapter.getDatas();
                        curlist.addAll(list);
                        mAdapter.setData(curlist);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                if (TextUtils.isEmpty(nextQuery)) {
                    mXRefreshViewFooters.setLoadcomplete(true);
                    xrefreshView.stopLoadMore(false);
                } else {
                    mXRefreshViewFooters.setLoadcomplete(false);
                    xrefreshView.stopLoadMore();
                }


                //刷新完成必须调用此方法停止加载
                xrefreshView.stopLoadMore();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                //定位视图隐藏  无数据视图隐藏-小圆圈视图隐藏-
                //如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                haveGeting = false;

                datings_error_location_vs.setVisibility(View.GONE);
                datings_error_network_vs.setVisibility(View.GONE);
                error_no_data_layout.setVisibility(View.GONE);

                //提示连接失败
                DialogUtil.showDisCoverNetToast(getActivity());

                //刷新完成必须调用此方法停止加载
                mXRefreshViewFooters.setLoadcomplete(true);
                xrefreshView.stopLoadMore(false);
            }
        });
        nearestsService.enqueue();

    }


    @Override
    public void onItemClick(View view, int position) {

        Bundle bundle = new Bundle();
        AppointmentDto dto = mAdapter.getDatas().get(position);
        bundle.putString(YpSettings.DATINGS_ID, dto.getDatingId());
        bundle.putInt(YpSettings.USERID, dto.getOwner().getUserId());
        bundle.putString(YpSettings.FROM_PAGE, "AppointmentFragment");
        bundle.putInt(YpSettings.DATINGS_TYPE, dto.getActivityType());

        ActivityUtil.jump(getActivity(), DatingDetailActivity.class, bundle, 0, 100);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("DatingsRefreshEvent")

            }

    )
    public void datingsRefreshEvent(DatingsRefreshEvent event) {

        int type = event.getEventType();
        if (type == 1) {
            if (xrefreshView.getVisibility() == View.GONE) {
                getAppointList();
            } else {
                xrefreshView.startRefresh();
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

        if (event.getEventType() == 2 && !isinitData) {
            isinitData = true;
            initData();
            getLocAndInit();

        }
    }

}
