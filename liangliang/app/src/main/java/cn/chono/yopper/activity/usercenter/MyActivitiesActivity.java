package cn.chono.yopper.activity.usercenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.MyActivitiesList.ActivityRespBean;
import cn.chono.yopper.Service.Http.MyActivitiesList.MyActivityReq;
import cn.chono.yopper.Service.Http.MyActivitiesList.MyActivityResp;
import cn.chono.yopper.Service.Http.MyActivitiesList.MyActivityService;
import cn.chono.yopper.Service.Http.MyActivitiesMoreList.MyActivityMoreResp;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListMoreReqBean;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListMoreServices;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.base.App;
import cn.chono.yopper.adapter.MyActivityAdapter;
import cn.chono.yopper.data.Activities;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * Created by jianghua on 2016/6/12.
 */
public class MyActivitiesActivity extends MainFrameActivity implements MyActivityAdapter.OnItemClickListener {

    private TextView nofouondTv;

    private XRefreshView xrefreshviewView;
    private RecyclerView listView;
    private XRefreshViewFooters mXRefreshViewFooters;
    private XRefreshViewHeaders mXRefreshViewHeaders;
    private MyActivityAdapter mMyActivityAdapter;

    private List<Activities> listData = new ArrayList<>();

    private String _nextQuery;

    int userId;

    View network_view;
    View no_data_view;
    boolean isFirst = true;
    int W;
    int H;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        W = wm.getDefaultDisplay().getWidth();
        H = wm.getDefaultDisplay().getHeight();

        setContentLayout(R.layout.activity_myactivities_layout);
        getGoBackLayout().setVisibility(View.VISIBLE);
        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getTvTitle().setText("我的活动");

        init();
        setXrefreshViewListener();

        userId = LoginUser.getInstance().getUserId();
        ActivityRespBean dto = DbHelperUtils.getMyActivities(userId);
        if (null != dto) {
            listData = dto.getList();
            _nextQuery = dto.getNextQuery();
            if (null != listData && listData.size() > 0) {
                mMyActivityAdapter.setData(listData);
                mMyActivityAdapter.notifyDataSetChanged();
            }
        }
        xrefreshviewView.startRefresh();
    }

    private void init() {
        LayoutInflater inflater = getLayoutInflater();
        network_view = inflater.inflate(R.layout.error_network_layout, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(W, H);
        network_view.setLayoutParams(layoutParams);
        no_data_view = inflater.inflate(R.layout.error_no_data_layout, null);
        no_data_view.setLayoutParams(layoutParams);

        nofouondTv = (TextView) findViewById(R.id.activities_nofound_tv);
        nofouondTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.jump(MyActivitiesActivity.this, MyActivitiesNoFoundActivity.class, null, 0, 100);
            }
        });

        xrefreshviewView = (XRefreshView) findViewById(R.id.xrefreshview_View);
        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setLayoutManager(new LinearLayoutManager(this));

        mMyActivityAdapter = new MyActivityAdapter(MyActivitiesActivity.this);
        mMyActivityAdapter.setOnItemClickListener(this);
        listView.setAdapter(mMyActivityAdapter);
    }


    private void setXrefreshViewListener() {

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //newState 目前滚动状态 0：未滚动
                if (newState == 0) {
                    Glide.with(MyActivitiesActivity.this).resumeRequests();
                } else {
                    Glide.with(MyActivitiesActivity.this).pauseRequests();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        listView.setHasFixedSize(true);


        mXRefreshViewFooters = new XRefreshViewFooters(MyActivitiesActivity.this);

        mXRefreshViewFooters.setRecyclerView(listView);


        mXRefreshViewHeaders = new XRefreshViewHeaders(MyActivitiesActivity.this);

        mMyActivityAdapter.setCustomLoadMoreView(mXRefreshViewFooters);

        xrefreshviewView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xrefreshviewView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        xrefreshviewView.setAutoLoadMore(true);
        xrefreshviewView.setCustomHeaderView(mXRefreshViewHeaders);


        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xrefreshviewView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        xrefreshviewView.setAutoLoadMore(true);

        mXRefreshViewFooters.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoadMoreData(_nextQuery);
                    }
                }, 1000);

            }
        });

        mXRefreshViewFooters.onAutoLoadMoreFail(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onLoadMoreData(_nextQuery);

                    }
                }, 1000);

            }
        });

        xrefreshviewView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefreshData();

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onLoadMoreData(_nextQuery);

                    }
                }, 1000);

            }

        });
    }

    private void onRefreshData() {
        if (!isFirst) {
            xrefreshviewView.setLoadComplete(false);
        } else {
            isFirst = false;
        }
        mMyActivityAdapter.setHeaderView(null, listView);

        //设置请求参数
        MyActivityReq reqBean = new MyActivityReq();
        reqBean.setStart(0);

        MyActivityService service = new MyActivityService(this);
        service.parameter(reqBean);
        service.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                MyActivityResp activityResp = (MyActivityResp) respBean;
                if (null != activityResp) {
                    if (null != activityResp.getResp().getList() && activityResp.getResp().getList().size() > 0) {
                        _nextQuery = activityResp.getResp().getNextQuery();
                        listData = activityResp.getResp().getList();
                        mMyActivityAdapter.setData(listData);
                        mMyActivityAdapter.notifyDataSetChanged();
                        String jsonStr = JsonUtils.toJson(activityResp);
                        DbHelperUtils.saveMyActiviteis(userId, jsonStr);
                    } else {
                        if (listData.size() > 0) {
                            DialogUtil.showDisCoverNetToast(MyActivitiesActivity.this, "没有数据更新");
                        } else {
                            handleNoDataError();
                            no_data_view.findViewById(R.id.error_no_data_layout).setVisibility(View.VISIBLE);
                            mMyActivityAdapter.setHeaderView(no_data_view, listView);
                            xrefreshviewView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (!TextUtils.isEmpty(_nextQuery)) {
                    mXRefreshViewFooters.setLoadcomplete(false);
                } else {
                    mXRefreshViewFooters.setLoadcomplete(true);
                }

                xrefreshviewView.stopRefresh();
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mXRefreshViewHeaders.onRefreshFail();
                xrefreshviewView.stopRefresh();

                if (listData.size() > 0) {
                    String msg = respBean.getMsg();
                    if (TextUtils.isEmpty(msg)) {
                        DialogUtil.showDisCoverNetToast(MyActivitiesActivity.this);
                    } else {
                        DialogUtil.showDisCoverNetToast(MyActivitiesActivity.this, msg);
                    }
                } else {
                    handleNetError();
                    mMyActivityAdapter.setHeaderView(network_view, listView);
                }
                xrefreshviewView.setVisibility(View.VISIBLE);
            }
        });

        service.enqueue();
    }

    private void handleNoDataError() {

        no_data_view.findViewById(R.id.error_no_data_layout).setVisibility(View.VISIBLE);

        TextView error_no_data_tv = (TextView) no_data_view.findViewById(R.id.error_no_data_tv);

        error_no_data_tv.setGravity(Gravity.CENTER);

        ImageView error_no_data_img = (ImageView) no_data_view.findViewById(R.id.error_no_data_img);

        error_no_data_tv.setText("您现在还没有参加任何活动,\n赶快去报名吧");

        error_no_data_img.setImageResource(R.drawable.ic_error_no_data);
    }

    private void handleNetError() {

        network_view.findViewById(R.id.error_network_layout).setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) network_view.findViewById(R.id.error_network_tv);
        error_network_tv.setVisibility(View.GONE);
        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);
                xrefreshviewView.setVisibility(View.GONE);
                onRefreshData();
            }
        });
    }

    private void onLoadMoreData(final String nextQuery) {
        if (TextUtils.isEmpty(nextQuery)) {
            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshviewView.stopLoadMore(false);
            return;
        }

        MyBonusListMoreReqBean reqBean = new MyBonusListMoreReqBean();
        reqBean.setNextQuery(nextQuery);

        MyBonusListMoreServices moreServices = new MyBonusListMoreServices(this);
        moreServices.parameter(reqBean);

        moreServices.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                MyActivityMoreResp moreResp = (MyActivityMoreResp) respBean;

                if (null != moreResp) {
                    if (null != moreResp.getResp().getList() && moreResp.getResp().getList().size() > 0) {
                        _nextQuery = moreResp.getResp().getNextQuery();
                        listData = moreResp.getResp().getList();
                        mMyActivityAdapter.addAll(listData);
                        mMyActivityAdapter.notifyDataSetChanged();
                    }

                    if (!TextUtils.isEmpty(_nextQuery)) {
                        mXRefreshViewFooters.setLoadcomplete(false);
                        xrefreshviewView.stopLoadMore();

                    } else {
                        mXRefreshViewFooters.setLoadcomplete(true);
                        xrefreshviewView.stopLoadMore(false);
                    }
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mXRefreshViewFooters.setLoadcomplete(false);
                xrefreshviewView.stopLoadMore(false);

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(MyActivitiesActivity.this);
                } else {
                    DialogUtil.showDisCoverNetToast(MyActivitiesActivity.this, msg);
                }

            }
        });
        moreServices.enqueue();
    }

    @Override
    public void onItemClick(int position, Activities activities) {
        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "活动详情");
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "activity/dtails?activityId=" + activities.getActivityId());
        bundle.putString(YpSettings.ACTIVITY_ID, activities.getActivityId());
        bundle.putInt(YpSettings.ACTIVITY_FEE, activities.getFee());

        bundle.putInt(YpSettings.SOURCE_YTPE_KEY, 600);
//        bundle.putInt(YpSettings.ACTIVITY_STATUS, activities.getActivityStatus());
        bundle.putInt(YpSettings.SIGHUP_STATUS, activities.getJoinStatus());
        ActivityUtil.jump(this, SimpleWebViewActivity.class, bundle, 0, 100);
    }
}
