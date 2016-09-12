package cn.chono.yopper.activity.near;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.DatingAddress.DatingAddressBean;
import cn.chono.yopper.Service.Http.DatingAddress.DatingAddressRespBean;
import cn.chono.yopper.Service.Http.DatingAddress.DatingAddressService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.BubblingAddressAdapter;
import cn.chono.yopper.adapter.BubblingAddressAdapter.OnItemClickLitener;
import cn.chono.yopper.data.NearLoc;
import cn.chono.yopper.data.NearPlaceDto;
import cn.chono.yopper.data.NearPlaceListDto;
import cn.chono.yopper.location.Loc;
import cn.chono.yopper.location.LocInfo;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;

/**
 * 选择约会地点
 *
 * @author sam.sun
 */
public class BubblingAddressActivity extends MainFrameActivity implements
        OnItemClickLitener {

    private RecyclerView select_address_recyclerview;// 地点列表

    private XRefreshView xrefreshView;

    private TextView data_reload_tv;

    private BubblingAddressAdapter mAdapter;

    // 坐标位置
    public double _latitude = 0.0;
    public double _longtitude = 0.0;
    /**
     * 开始位置
     */
    private int _start = 0;

    private int _total = 0;

    /**
     * 加载的条数
     */
    private int number = 20;

    // private int type;

    private int result_code;

    private TextView error_location_tv;


    private LinearLayout error_location_layout;


    private boolean haveGpsTag;

    private boolean isgetLoc = false;


    private LocInfo myLoc;

    XRefreshViewHeaders mXRefreshViewHeaders;


    XRefreshViewFooters mXRefreshViewFooter;

    private Dialog loadingDiaog;


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发布冒泡地址页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
        Loc.sendLocControlMessage(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("发布冒泡地址页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        boolean isBubblingAddress = SharedprefUtil.getBoolean(this,
                YpSettings.BUBBLING_ADDRESS_STR, false);

        if (!isBubblingAddress) {// false

            SharedprefUtil.saveBoolean(this, YpSettings.BUBBLING_ADDRESS_STR,
                    true);
            // 获得位置
            haveGpsTag = Loc.isGpsAvailable();
            if (!haveGpsTag) {
                // 未开启定位权限

                error_location_layout.setVisibility(View.VISIBLE);
                xrefreshView.setVisibility(View.GONE);

            } else {
                error_location_layout.setVisibility(View.GONE);

                xrefreshView.setVisibility(View.GONE);
                loadingDiaog = DialogUtil.LoadingDialog(this, null);
                if (!isFinishing()) {
                    loadingDiaog.show();
                }

                Loc.sendLocControlMessage(true);

                getLocinfo();
            }
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

    private Handler LocHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {
                if (msg.what == 0) {
                    loadingDiaog.dismiss();
                    xrefreshView.setVisibility(View.GONE);
                    error_location_layout.setVisibility(View.VISIBLE);

                } else if (msg.what == 1) {
                    // 找到位置
                    isgetLoc = true;

                    _latitude = myLoc.getLoc().getLatitude();
                    _longtitude = myLoc.getLoc().getLongitude();

                    LogUtils.e("--_latitude---" + _latitude);
                    LogUtils.e("--_longtitude---" + _longtitude);
                    LogUtils.e("--myLoc---" + myLoc.getInfo());
                    LogUtils.e("--myLoc---" + myLoc.getAddrStr());
                    LogUtils.e("--myLoc---" + myLoc.getDistrict());

                    _start = 0;
                    onRefreshData(_start);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.bubbling_address_activity);
        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            // type = bundle.getInt(YpSettings.APPOINTMENT_INTENT_YTPE, 51);
            result_code = bundle.getInt(YpSettings.INTENT_RESULT_CODE);
        }

        initView();
        setXrefreshListener();

    }

    /**
     * 初始化
     */
    private void initView() {

        xrefreshView = (XRefreshView) findViewById(R.id.select_address_xrefreshview);

        select_address_recyclerview = (RecyclerView) findViewById(R.id.select_address_recyclerview);
        select_address_recyclerview.setLayoutManager(new LinearLayoutManager(
                this));
        select_address_recyclerview.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new BubblingAddressAdapter(this, null);
        select_address_recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(this);

        // 设置标题栏
        getTvTitle().setText("我在哪儿");
        TextView tvBack = gettvBack();

        tvBack.setText("取消");
        tvBack.setVisibility(View.VISIBLE);
        getBtnGoBack().setVisibility(View.GONE);

        getOptionLayout().setVisibility(View.INVISIBLE);

        getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();

            }
        });

        error_location_tv = (TextView) findViewById(R.id.error_location_tv);


        error_location_layout = (LinearLayout) findViewById(R.id.error_location_layout);

        error_location_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);

                SharedprefUtil.saveBoolean(BubblingAddressActivity.this,
                        YpSettings.BUBBLING_ADDRESS_STR, false);

                try {
                    Intent intent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(
                            android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
//		ActivityUtil.overridePendingTransition(BubblingAddressActivity.this, 0,
//				R.anim.out_to_bottom);
    }

    private void setXrefreshListener() {


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshViewFooter = new XRefreshViewFooters(this);

        mXRefreshViewFooter.setRecyclerView(select_address_recyclerview);


        mAdapter.setCustomLoadMoreView(mXRefreshViewFooter);

        xrefreshView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xrefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        xrefreshView.setAutoLoadMore(true);


        mXRefreshViewFooter.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        onLoadMoreData(_start);
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

                        onLoadMoreData(_start);

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


                        onLoadMoreData(_start);


                    }
                }, 1000);
            }

        });

    }

    private void onRefreshData(final int start) {


        if (_latitude == 0 && _longtitude == 0) {
            mXRefreshViewHeaders.onRefreshFail();
            xrefreshView.stopRefresh();
            return;
        }

        LatLng pt = new LatLng(_latitude, _longtitude);
        pt = Loc.getBaiduGpsFromGcj(pt.latitude, pt.longitude);

        String time = TimeUtil.getDateFormatString(System.currentTimeMillis());


        DatingAddressBean addressBean = new DatingAddressBean();
        addressBean.setLng(pt.longitude);
        addressBean.setLat(pt.latitude);
        addressBean.setType("");
        addressBean.setR(5);
        addressBean.setDatingTime(time);
        addressBean.setSort("");
        addressBean.setStart(start);
        addressBean.setRows(20);

        DatingAddressService addressService = new DatingAddressService(this);
        addressService.parameter(addressBean);
        addressService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DatingAddressRespBean addressRespBean = (DatingAddressRespBean) respBean;

                NearPlaceListDto nearPlaceListdto = addressRespBean.getResp();


                xrefreshView.stopRefresh();
                loadingDiaog.dismiss();
                xrefreshView.setVisibility(View.VISIBLE);

                List<NearPlaceDto> placeList = setNearPlaceDto();
                List<NearPlaceDto> list = new ArrayList<NearPlaceDto>();


                if (nearPlaceListdto != null) {// 有数据
                    _total = nearPlaceListdto.getTotal();
                    _start = _start + nearPlaceListdto.getRows();
                    list = nearPlaceListdto.getList();

                }

                if (placeList != null && placeList.size() > 0) {
                    if (list != null && list.size() > 0) {
                        placeList.addAll(list);
                    }
                } else {
                    placeList = list;
                }

                mAdapter.setData(placeList);
                mAdapter.notifyDataSetChanged();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                mXRefreshViewHeaders.onRefreshFail();
                xrefreshView.stopRefresh();

                loadingDiaog.dismiss();
                List<NearPlaceDto> placeList = setNearPlaceDto();
                xrefreshView.setVisibility(View.VISIBLE);
                error_location_layout.setVisibility(View.GONE);
                if (placeList != null && placeList.size() > 0) {
                    mAdapter.setData(placeList);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });

        addressService.enqueue();

    }

    private List<NearPlaceDto> setNearPlaceDto() {

        LocInfo myLoc = Loc.getLoc();
        String naeme = "";
        String address = "";
        if (myLoc != null && myLoc.getLoc() != null) {
            LogUtils.e("--myLoc.getLoc().getInfo()---" + myLoc.getInfo());
            naeme = myLoc.getDistrict() + "·" + myLoc.getStreet();
            address = myLoc.getAddrStr();
        }
        if (CheckUtil.isEmpty(naeme)) {
            return null;
        }

        if (CheckUtil.isEmpty(address)) {
            return null;
        }

        List<NearPlaceDto> list = new ArrayList<NearPlaceDto>();
        NearPlaceDto dto = new NearPlaceDto();
        NearLoc loc = new NearLoc();
        loc.setId(0);
        loc.setName(naeme);
        loc.setAddress(address);
        loc.setLat(0.0);
        loc.setLng(0.0);
        dto.setLoc(loc);
        list.add(dto);
        return list;
    }

    private void onLoadMoreData(final int start) {

        if (_total <= start) {


            mXRefreshViewFooter.setLoadcomplete(true);
            xrefreshView.setLoadComplete(false);

            return;
        }


        String time = TimeUtil.getDateFormatString(System.currentTimeMillis());

        DatingAddressBean addressBean = new DatingAddressBean();
        addressBean.setLng(_longtitude);
        addressBean.setLat(_latitude);
        addressBean.setType("");
        addressBean.setR(5);
        addressBean.setDatingTime(time);
        addressBean.setSort("");
        addressBean.setStart(start);
        addressBean.setRows(20);

        DatingAddressService addressService = new DatingAddressService(this);
        addressService.parameter(addressBean);
        addressService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DatingAddressRespBean addressRespBean = (DatingAddressRespBean) respBean;

                NearPlaceListDto nearPlaceListdto = addressRespBean.getResp();


                if (nearPlaceListdto != null) {

                    List<NearPlaceDto> list = nearPlaceListdto
                            .getList();
                    _start = _start + nearPlaceListdto.getRows();
                    _total = nearPlaceListdto.getTotal();
                    if (list != null && list.size() > 0) {

                        List<NearPlaceDto> curlist = mAdapter.getDatas();
                        curlist.addAll(list);
                        mAdapter.setData(curlist);
                        mAdapter.notifyDataSetChanged();

                    }
                } else {
                    // 没有网络的场合，去提示页
                    DialogUtil.showDisCoverNetToast(BubblingAddressActivity.this);
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

                mXRefreshViewFooter.setLoadcomplete(false);
                xrefreshView.stopLoadMore(false);


                // 没有网络的场合，去提示页
                DialogUtil.showDisCoverNetToast(BubblingAddressActivity.this);

            }
        });

        addressService.enqueue();


    }

    @Override
    public void onItemClick(View view, int position, NearPlaceDto nearPlaceDto) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(YpSettings.ADDRESS_LIST_DTO, nearPlaceDto);
        ActivityUtil.jump(BubblingAddressActivity.this, PublishBubblingActivity.class, bundle, 0, 100);

    }

}
