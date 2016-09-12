package cn.chono.yopper.activity.usercenter;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.hwangjr.rxbus.RxBus;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.VisitorsList.VisitorsListBean;
import cn.chono.yopper.Service.Http.VisitorsList.VisitorsListRespBean;
import cn.chono.yopper.Service.Http.VisitorsList.VisitorsListService;
import cn.chono.yopper.Service.Http.VisitorsMoreList.VisitorsMoreListBean;
import cn.chono.yopper.Service.Http.VisitorsMoreList.VisitorsMoreListRespBean;
import cn.chono.yopper.Service.Http.VisitorsMoreList.VisitorsMoreListService;
import cn.chono.yopper.adapter.UserVisitorsAdapter;
import cn.chono.yopper.adapter.UserVisitorsAdapter.OnItemClickLitener;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.VisitorsDto;
import cn.chono.yopper.data.VisitorsListDto;
import cn.chono.yopper.data.Visits;
import cn.chono.yopper.event.SyncVideoStateEvent;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;


/**
 * 访客列表
 *
 * @author sam.sun
 */
public class VisitorsActivity extends MainFrameActivity implements OnItemClickLitener {


    private RecyclerView user_visitors_recyclerview;// 地点列表

    private XRefreshView xrefreshView;

    private UserVisitorsAdapter mAdapter;

    private ViewStub visitors_error_network_vs;
    private ViewStub visitors_error_no_data_vs;

    private boolean haveGeting = false;

    private String nextQuery;

    private int userid;

    private Dialog loadingDiaog;

    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.user_visitors_activity);
        PushAgent.getInstance(this).onAppStart();

        Bundle bunble = this.getIntent().getExtras();
        if (bunble != null) {
            userid = bunble.getInt(YpSettings.USERID);
        }
        initComponent();

        loadingDiaog = DialogUtil.LoadingDialog(VisitorsActivity.this, null);

        if (!isFinishing()) {
            loadingDiaog.show();
        }

        xrefreshView.setVisibility(View.GONE);

        visitors_error_network_vs.setVisibility(View.GONE);

        visitors_error_no_data_vs.setVisibility(View.GONE);

        getVisitorsList(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("访客页面"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("访客页面"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("谁看过我");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });

        xrefreshView = (XRefreshView) findViewById(R.id.user_visitors_xrefreshview);


        user_visitors_recyclerview = (RecyclerView) findViewById(R.id.user_visitors_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        user_visitors_recyclerview.setLayoutManager(layoutManager);
        mAdapter = new UserVisitorsAdapter(this);
        user_visitors_recyclerview.setAdapter(mAdapter);

        setXrefreshListener();

        mAdapter.setOnItemClickLitener(this);

        visitors_error_network_vs = (ViewStub) findViewById(R.id.visitors_error_network_vs);

        visitors_error_no_data_vs = (ViewStub) findViewById(R.id.visitors_error_no_data_vs);


    }


    private void handleNetWorkError() {

        visitors_error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) this.findViewById(R.id.error_network_layout);
        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) this.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewsUtils.preventViewMultipleClick(v, 1000);
                visitors_error_network_vs.setVisibility(View.GONE);
                visitors_error_no_data_vs.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.GONE);

                loadingDiaog = DialogUtil.LoadingDialog(VisitorsActivity.this, null);

                if (!isFinishing()) {
                    loadingDiaog.show();
                }

                getVisitorsList(0);
            }
        });
    }


    private void handleNoData() {

        visitors_error_no_data_vs.setVisibility(View.VISIBLE);

        LinearLayout error_no_data_layout = (LinearLayout) this.findViewById(R.id.error_no_data_layout);
        error_no_data_layout.setVisibility(View.VISIBLE);

        TextView error_no_data_tv = (TextView) this.findViewById(R.id.error_no_data_tv);

        error_no_data_tv.setText("暂时没有人访问你");
    }


    private void setXrefreshListener() {


        mXRefreshViewHeaders=new XRefreshViewHeaders(this);

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);


        mXRefreshViewFooters = new XRefreshViewFooters(this);

        mXRefreshViewFooters.setRecyclerView(user_visitors_recyclerview);

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

                        loadMoreGetVisitorsList();
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

                        loadMoreGetVisitorsList();
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

                        getVisitorsList(0);

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        loadMoreGetVisitorsList();

                    }
                }, 1000);
            }

        });


    }


    /**
     * 获取约会列表
     */

    private void getVisitorsList(final int start) {


        if (haveGeting) {
            mXRefreshViewHeaders.onRefreshFail();
            xrefreshView.stopRefresh();
            return;
        }

        VisitorsListBean listBean = new VisitorsListBean();
        listBean.setUserid(userid);
        listBean.setStart(start);

        VisitorsListService listService = new VisitorsListService(this);
        listService.parameter(listBean);
        listService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                VisitorsListRespBean listRespBean = (VisitorsListRespBean) respBean;
                VisitorsListDto listDto = listRespBean.getResp();
                savaLookme();

                //定位视图隐藏  网络加载失败视图隐藏-小圆圈视图隐藏
                //如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                haveGeting = false;

                visitors_error_network_vs.setVisibility(View.GONE);

                List<VisitorsDto> list = new ArrayList<VisitorsDto>();


                if (listDto != null) {

                    list = listDto.getList();

                    if (list != null && list.size() > 0) {
                        visitors_error_no_data_vs.setVisibility(View.GONE);
                        xrefreshView.setVisibility(View.VISIBLE);
                        nextQuery = listDto.getNextQuery();
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        handleNoData();
                        xrefreshView.setVisibility(View.GONE);
                    }
                } else {
                    handleNoData();
                    xrefreshView.setVisibility(View.GONE);
                }

                xrefreshView.stopRefresh();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();

                mXRefreshViewHeaders.onRefreshFail();
                xrefreshView.stopRefresh();

                //定位视图隐藏  无数据视图隐藏-小圆圈视图隐藏-
                //如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                // 动画回收
                // 动画回收
                haveGeting = false;
                handleNetWorkError();
                xrefreshView.setVisibility(View.GONE);
                visitors_error_no_data_vs.setVisibility(View.GONE);
            }
        });
        listService.enqueue();

    }


    /**
     * 获取约会列表
     */

    private void loadMoreGetVisitorsList() {

        if (haveGeting) {
            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshView.stopLoadMore(false);
            return;
        }

        if (CheckUtil.isEmpty(nextQuery)) {

            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshView.stopLoadMore(false);
            return;
        }


        VisitorsMoreListBean moelistBean = new VisitorsMoreListBean();
        moelistBean.setNextQuery(nextQuery);

        VisitorsMoreListService listService = new VisitorsMoreListService(this);
        listService.parameter(moelistBean);
        listService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                VisitorsMoreListRespBean listRespBean = (VisitorsMoreListRespBean) respBean;
                VisitorsListDto listDto = listRespBean.getResp();
                haveGeting = false;

                List<VisitorsDto> list = new ArrayList<VisitorsDto>();

                if (listDto != null) {

                    list = listDto.getList();

                    if (list != null && list.size() > 0) {

                        nextQuery = listDto.getNextQuery();
                        List<VisitorsDto> curlist = mAdapter.getData();
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


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                mXRefreshViewFooters.setLoadcomplete(false);
                xrefreshView.stopLoadMore(false);

                //定位视图隐藏  无数据视图隐藏-小圆圈视图隐藏-
                //如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                haveGeting = false;
                visitors_error_network_vs.setVisibility(View.GONE);
                visitors_error_no_data_vs.setVisibility(View.GONE);

                //提示连接失败
                DialogUtil.showDisCoverNetToast(VisitorsActivity.this);
            }
        });
        listService.enqueue();

    }

    private void savaLookme() {


        Visits dto;
        try {
            dto = App.getInstance().db.findFirst(Selector.from(Visits.class).where("id", " =", userid));
            if (dto != null) {
                dto.setNewadded(0);
                App.getInstance().db.update(dto);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

        RxBus.get().post("SyncVideoStateEvent", new SyncVideoStateEvent());

    }


    @Override
    public void onItemClick(View view, int position, int userID) {
        Bundle bundle = new Bundle();
        bundle.putInt(YpSettings.USERID, userID);
        ActivityUtil.jump(VisitorsActivity.this, UserInfoActivity.class, bundle, 0, 100);
    }

}
