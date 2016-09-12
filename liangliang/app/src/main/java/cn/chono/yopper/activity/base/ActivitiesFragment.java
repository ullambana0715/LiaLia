package cn.chono.yopper.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ActivitiesList.ActivityReq;
import cn.chono.yopper.Service.Http.ActivitiesList.ActivityResp;
import cn.chono.yopper.Service.Http.ActivitiesList.ActivityService;
import cn.chono.yopper.Service.Http.ActivitiesMoreList.ActivityMoreReq;
import cn.chono.yopper.Service.Http.ActivitiesMoreList.ActivityMoreResp;
import cn.chono.yopper.Service.Http.ActivitiesMoreList.ActivityMoreService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.ActivityAdapter;
import cn.chono.yopper.data.IndexActivities;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.JsonUtils;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * Created by jianghua on 2016/6/13.
 */
public class ActivitiesFragment extends Fragment implements ActivityAdapter.OnItemClickListener {
    private Activity mActivity;
    private View contentView;


    private TextView titleTv;

    private XRefreshView xrefreshviewView;
    private XRefreshViewFooters mXRefreshViewFooters;
    private XRefreshViewHeaders mXRefreshViewHeaders;
    private RecyclerView listView;

    private List<IndexActivities> listData = new ArrayList<>();
    private ActivityAdapter mActivityAdapter;

    private String _nextQuery;
    View network_view;
    View no_data_view;

    boolean isFirst = true;
    int userId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.from(mActivity).inflate(R.layout.fragment_activities_layout, container, false);
        network_view = LayoutInflater.from(mActivity).inflate(R.layout.error_network_layout, container, false);
        no_data_view = LayoutInflater.from(mActivity).inflate(R.layout.error_no_data_layout, container, false);

        init();
        setXrefreshViewListener();

        userId = LoginUser.getInstance().getUserId();
        ActivityResp.IndexActivitiesResp dto = DbHelperUtils.getIndexActivities(userId);
        if (null != dto) {
            listData = dto.getList();
            _nextQuery = dto.getNextQuery();
            if (null != listData && listData.size() > 0) {
                mActivityAdapter.setData(listData);
                mActivityAdapter.notifyDataSetChanged();
            }
        }

        xrefreshviewView.startRefresh();
        return contentView;
    }

    private void init() {
        titleTv = (TextView) contentView.findViewById(R.id.title_tv);

        xrefreshviewView = (XRefreshView) contentView.findViewById(R.id.xrefreshview_View);
        listView = (RecyclerView) contentView.findViewById(R.id.listView);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setLayoutManager(new LinearLayoutManager(mActivity));

        mActivityAdapter = new ActivityAdapter(mActivity);
        mActivityAdapter.setOnItemClickListener(this);

        listView.setAdapter(mActivityAdapter);
    }

    private void setXrefreshViewListener() {

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //newState 目前滚动状态 0：未滚动
                if (newState == 0) {
                    Glide.with(mActivity).resumeRequests();
                } else {
                    Glide.with(mActivity).pauseRequests();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        listView.setHasFixedSize(true);


        mXRefreshViewFooters = new XRefreshViewFooters(mActivity);


        mXRefreshViewFooters.setRecyclerView(listView);

        mXRefreshViewHeaders = new XRefreshViewHeaders(mActivity);

        mActivityAdapter.setCustomLoadMoreView(mXRefreshViewFooters);

        xrefreshviewView.setPullLoadEnable(true);


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

    private void handleNoDataError() {
        LinearLayout error_no_data_layout = (LinearLayout) no_data_view.findViewById(R.id.error_no_data_layout);
        error_no_data_layout.setVisibility(View.VISIBLE);

        TextView error_no_data_tv = (TextView) no_data_view.findViewById(R.id.error_no_data_tv);

        ImageView error_no_data_img = (ImageView) no_data_view.findViewById(R.id.error_no_data_img);

        error_no_data_tv.setText("暂时没有活动数据");
        error_no_data_img.setImageResource(R.drawable.ic_error_no_data);
    }

    private void handleNetError() {
        LinearLayout error_network_layout = (LinearLayout) network_view.findViewById(R.id.error_network_layout);

        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv1 = (TextView) network_view.findViewById(R.id.error_network_tv1);
        TextView error_network_tv2 = (TextView) network_view.findViewById(R.id.error_network_tv2);
        TextView error_network_tv = (TextView) network_view.findViewById(R.id.error_network_tv);

        error_network_tv1.setText("数据加载失败了");
        error_network_tv2.setText("请下拉重新刷新");
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


    private void onRefreshData() {

        if (!isFirst) {
            xrefreshviewView.setLoadComplete(false);
        } else {
            isFirst = false;
        }
        mActivityAdapter.setHeaderView(null, listView);

        //设置请求参数
        ActivityReq reqBean = new ActivityReq();
        reqBean.setStart(0);

        ActivityService service = new ActivityService(mActivity);
        service.parameter(reqBean);
        service.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                ActivityResp activityResp = (ActivityResp) respBean;
                if (null != activityResp.getResp()) {

                    if (null != activityResp.getResp().getList() && activityResp.getResp().getList().size() > 0) {
                        _nextQuery = activityResp.getResp().getNextQuery();
                        listData = activityResp.getResp().getList();
                        mActivityAdapter.setData(listData);
                        mActivityAdapter.notifyDataSetChanged();
                        String str = JsonUtils.toJson(activityResp);
                        DbHelperUtils.saveIndexActivities(userId, str);
                    } else {
                        if (listData.size() > 0) {
                            DialogUtil.showDisCoverNetToast(mActivity, "没有数据更新");
                        } else {
                            handleNoDataError();
                            mActivityAdapter.setHeaderView(no_data_view, listView);
                            xrefreshviewView.setVisibility(View.VISIBLE);
                        }
                    }

                }

                if (TextUtils.isEmpty(_nextQuery)) {
                    mXRefreshViewFooters.setLoadcomplete(true);
                } else {
                    mXRefreshViewFooters.setLoadcomplete(false);
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
                        DialogUtil.showDisCoverNetToast(mActivity);
                    } else {
                        DialogUtil.showDisCoverNetToast(mActivity, msg);
                    }
                } else {
                    handleNetError();
                    mActivityAdapter.setHeaderView(network_view, listView);
                }
                xrefreshviewView.setVisibility(View.VISIBLE);

            }
        });

        service.enqueue();
    }

    private void onLoadMoreData(final String nextQuery) {
        if (TextUtils.isEmpty(nextQuery)) {
            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshviewView.stopLoadMore(false);
            return;
        }

        ActivityMoreReq reqBean = new ActivityMoreReq();
        reqBean.setNextQuery(nextQuery);

        ActivityMoreService moreServices = new ActivityMoreService(mActivity);
        moreServices.parameter(reqBean);

        moreServices.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                ActivityMoreResp moreResp = (ActivityMoreResp) respBean;
                if (null != moreResp.getResp()) {
                    if (null != moreResp.getResp().getList() && moreResp.getResp().getList().size() > 0) {
                        _nextQuery = moreResp.getResp().getNextQuery();
                        listData = moreResp.getResp().getList();
                        mActivityAdapter.addAll(listData);
                        mActivityAdapter.notifyDataSetChanged();
                    }

                    if (TextUtils.isEmpty(_nextQuery)) {
                        mXRefreshViewFooters.setLoadcomplete(true);
                        xrefreshviewView.stopLoadMore(false);

                    } else {
                        mXRefreshViewFooters.setLoadcomplete(false);
                        xrefreshviewView.stopLoadMore();
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
                    DialogUtil.showDisCoverNetToast(mActivity);
                } else {
                    DialogUtil.showDisCoverNetToast(mActivity, msg);
                }
            }
        });
        moreServices.enqueue();
    }

    @Override
    public void onItemClick(int position, IndexActivities activityDto) {
        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "活动详情");
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "activity/dtails?activityId=" + activityDto.getActivityId());
        bundle.putString(YpSettings.ACTIVITY_ID, activityDto.getActivityId());
        bundle.putInt(YpSettings.ACTIVITY_FEE, activityDto.getFee());

        bundle.putInt(YpSettings.SOURCE_YTPE_KEY, 600);
        bundle.putInt(YpSettings.ACTIVITY_STATUS, activityDto.getActivityStatus());
        bundle.putInt(YpSettings.SIGHUP_STATUS, activityDto.getJoinStatus());
        ActivityUtil.jump(mActivity, SimpleWebViewActivity.class, bundle, 0, 100);
    }
}
