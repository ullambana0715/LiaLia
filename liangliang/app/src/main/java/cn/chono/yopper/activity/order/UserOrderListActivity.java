package cn.chono.yopper.activity.order;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderCancel.OrderCancelBean;
import cn.chono.yopper.Service.Http.OrderCancel.OrderCancelEntity;
import cn.chono.yopper.Service.Http.OrderCancel.OrderCancelRespEntity;
import cn.chono.yopper.Service.Http.OrderCancel.OrderCancelService;
import cn.chono.yopper.Service.Http.OrderDetail.OrderDetailDto;
import cn.chono.yopper.Service.Http.OrderLimits.OrderLimitsBean;
import cn.chono.yopper.Service.Http.OrderLimits.OrderLimitsEntity;
import cn.chono.yopper.Service.Http.OrderLimits.OrderLimitsRespEntity;
import cn.chono.yopper.Service.Http.OrderLimits.OrderLimitsService;
import cn.chono.yopper.Service.Http.OrderList.OrderListBean;
import cn.chono.yopper.Service.Http.OrderList.OrderListMoreBean;
import cn.chono.yopper.Service.Http.OrderList.OrderListMoreService;
import cn.chono.yopper.Service.Http.OrderList.OrderListRespBean;
import cn.chono.yopper.Service.Http.OrderList.OrderListService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.chat.ChatCounselActivity;
import cn.chono.yopper.activity.find.TarotOrAstrologyDetailActivity;
import cn.chono.yopper.adapter.UserOrderListAdapter;
import cn.chono.yopper.data.CounselOrderStatusTable;
import cn.chono.yopper.data.OrderListDto;
import cn.chono.yopper.event.OrderListEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.OnAdapterIconClickLitener;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;

/**
 * 我的订单列表
 */
public class UserOrderListActivity extends MainFrameActivity implements UserOrderListAdapter.OnOrderItemClickLitener, OnAdapterIconClickLitener {


    private UserOrderListAdapter mUserOrderListAdapter;


    private RecyclerView user_order_rv;

    private XRefreshView user_order_xrv;


    private ViewStub user_order_no_data_vs;

    TextView error_no_data_tv;

    private String nextQuery;

    private Dialog mLoadingDiaog, cancel_dlg, feedback_dlg;

    XRefreshViewFooters mXRefreshViewFooter;

    XRefreshViewHeaders mXRefreshViewHeaders;

    int mOrderType = 0;

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("我的订单"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        user_order_xrv.setPullLoadEnable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("我的订单"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_order_list);
        PushAgent.getInstance(this).onAppStart();


        RxBus.get().register(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.ORDER_TYPE))

            mOrderType = bundle.getInt(YpSettings.ORDER_TYPE);


        initView();
        setXrefreshListener();

        user_order_xrv.setVisibility(View.GONE);

        mLoadingDiaog.show();
        OnRefreshUserOrderData(mOrderType);


    }


    private void NoData(int Visibility) {
        if (Visibility == View.GONE) {
            user_order_no_data_vs.setVisibility(View.GONE);
        } else {
            user_order_no_data_vs.setVisibility(View.VISIBLE);

            error_no_data_tv = (TextView) findViewById(R.id.error_no_data_tv);
            error_no_data_tv.setText("暂无数据");
        }

    }

    private void initView() {

        // 设置标题栏
        this.getTvTitle().setText("我的订单");

        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getBtnGoBack().setVisibility(View.VISIBLE);

        mLoadingDiaog = DialogUtil.LoadingDialog(this);


        user_order_no_data_vs = (ViewStub) findViewById(R.id.user_order_no_data_vs);


        user_order_xrv = (XRefreshView) findViewById(R.id.user_order_xrv);

        user_order_rv = (RecyclerView) findViewById(R.id.user_order_rv);

        user_order_rv.setLayoutManager(new LinearLayoutManager(this));

        user_order_rv.setItemAnimator(new DefaultItemAnimator());

        mUserOrderListAdapter = new UserOrderListAdapter(this);

        user_order_rv.setAdapter(mUserOrderListAdapter);

        mUserOrderListAdapter.setOnOrderItemClickLitener(this);

        mUserOrderListAdapter.setOnAdapterIconClickLitener(this);


        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });


    }


    private void setXrefreshListener() {


        mXRefreshViewHeaders=new XRefreshViewHeaders(this);

        user_order_xrv.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshViewFooter = new XRefreshViewFooters(this);

        mXRefreshViewFooter.setRecyclerView(user_order_rv);

        mUserOrderListAdapter.setCustomLoadMoreView(mXRefreshViewFooter);

        user_order_xrv.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        user_order_xrv.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        user_order_xrv.setAutoLoadMore(true);


        mXRefreshViewFooter.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        OnLoadMoreUserOrderData(nextQuery);
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

                        OnLoadMoreUserOrderData(nextQuery);
                    }
                }, 1000);
            }
        });


        user_order_xrv.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        OnRefreshUserOrderData(mOrderType);


                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        OnLoadMoreUserOrderData(nextQuery);
                    }
                }, 1000);
            }

        });


    }


    /**
     * 获取用户约会数据
     * IsHistory 是否历史约会
     * Start
     * Rows
     */

    private void OnRefreshUserOrderData(int orderType) {


        OrderListService dataService = new OrderListService(this);
        OrderListBean orderListBean = new OrderListBean();
        orderListBean.setOrderType(orderType);

        dataService.parameter(orderListBean);

        dataService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                mLoadingDiaog.dismiss();

                OrderListRespBean orderListRespBean = (OrderListRespBean) respBean;

                OrderListDto orderListDto = orderListRespBean.getResp();

                if (null != orderListDto) {

                    nextQuery = orderListDto.getNextQuery();

                    if (null != orderListDto.getList() && orderListDto.getList().size() > 0) {

                        mUserOrderListAdapter.setData(orderListDto.getList());
                        mUserOrderListAdapter.notifyDataSetChanged();

                        user_order_xrv.setVisibility(View.VISIBLE);
                        NoData(View.GONE);

                    } else {

                        user_order_xrv.setVisibility(View.GONE);
                        NoData(View.VISIBLE);


                    }
                }

                if (TextUtils.isEmpty(nextQuery)) {
                    mXRefreshViewFooter.setLoadcomplete(true);
                } else {
                    mXRefreshViewFooter.setLoadcomplete(false);
                }

                user_order_xrv.stopRefresh();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mLoadingDiaog.dismiss();

                mXRefreshViewHeaders.onRefreshFail();

                user_order_xrv.stopRefresh();

                List<OrderDetailDto> list = mUserOrderListAdapter.getDatas();

                if (null == list || list.size() <= 0) {
                    user_order_xrv.setVisibility(View.GONE);
                    NoData(View.VISIBLE);
                    return;
                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(UserOrderListActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(UserOrderListActivity.this, msg);

            }
        });
        dataService.enqueue();


    }

    private void OnLoadMoreUserOrderData(String nextQueryUrl) {
        if (TextUtils.isEmpty(nextQueryUrl)) {

            mXRefreshViewFooter.setLoadcomplete(true);
            user_order_xrv.stopLoadMore(false);


            return;
        }

        OrderListMoreBean orderListMoreBean = new OrderListMoreBean();
        orderListMoreBean.setNextQuery(nextQueryUrl);

        OrderListMoreService orderListMoreService = new OrderListMoreService(this);
        orderListMoreService.parameter(orderListMoreBean);

        orderListMoreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                OrderListRespBean orderListRespBean = (OrderListRespBean) respBean;
                OrderListDto orderListDto = orderListRespBean.getResp();

                if (null != orderListDto && null != orderListDto.getList() && orderListDto.getList().size() > 0) {
                    nextQuery = orderListDto.getNextQuery();
                    mUserOrderListAdapter.addData(orderListDto.getList());
                    mUserOrderListAdapter.notifyDataSetChanged();


                }


                if (TextUtils.isEmpty(nextQuery)) {
                    mXRefreshViewFooter.setLoadcomplete(true);
                    user_order_xrv.stopLoadMore(false);

                } else {
                    mXRefreshViewFooter.setLoadcomplete(false);
                    user_order_xrv.stopLoadMore();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                LogUtils.e("onFail");

                mXRefreshViewFooter.setLoadcomplete(false);
                user_order_xrv.stopLoadMore(false);


            }
        });

        orderListMoreService.enqueue();

    }

    /**
     * 投诉订单限制
     *
     * @param orderId
     */
    private void getOrderLimits(final String orderId) {

        OrderLimitsBean orderLimitsBean = new OrderLimitsBean();

        orderLimitsBean.id = orderId;

        OrderLimitsService orderLimitsService = new OrderLimitsService(this);

        orderLimitsService.parameter(orderLimitsBean);

        orderLimitsService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                OrderLimitsRespEntity orderLimitsRespEntity = (OrderLimitsRespEntity) respBean;

                OrderLimitsEntity orderLimitsEntity = orderLimitsRespEntity.resp;

                if (orderLimitsEntity == null) {
                    return;
                }

                if (orderLimitsEntity.result == 0) {

                    Bundle bundle = new Bundle();

                    bundle.putString(YpSettings.ORDER_ID, orderId);


                    ActivityUtil.jump(UserOrderListActivity.this, UserOrderFeedBackActivity.class, bundle, 0, 100);

                    return;
                }
                DialogUtil.showDisCoverNetToast(UserOrderListActivity.this, orderLimitsEntity.msg);
            }
        });

        orderLimitsService.enqueue();


    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    private void cancelOrder(String orderId) {

        final OrderCancelBean orderCancelBean = new OrderCancelBean();

        orderCancelBean.id = orderId;


        OrderCancelService orderCancelService = new OrderCancelService(this);


        orderCancelService.parameter(orderCancelBean);


        orderCancelService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                OrderCancelRespEntity orderCancelRespEntity = (OrderCancelRespEntity) respBean;

                OrderCancelEntity orderCancelEntity = orderCancelRespEntity.resp;


                if (orderCancelEntity == null) return;


                if (orderCancelEntity.resp) {

                    DialogUtil.showDisCoverNetToast(UserOrderListActivity.this, "取消成功");

                    user_order_xrv.startRefresh();

                    return;

                }

                DialogUtil.showDisCoverNetToast(UserOrderListActivity.this, "取消失败");

            }
        });

        orderCancelService.enqueue();
    }


    @Override
    public void OnCancelEvent(int position, final String orderId) {//取消订单


        cancel_dlg = DialogUtil.createHintOperateDialog(UserOrderListActivity.this, "", "确定要取消此订单吗？", "取消", "确定", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                cancel_dlg.dismiss();

            }

            @Override
            public void onEnsure(View view, Object... obj) {

                cancel_dlg.dismiss();

                cancelOrder(orderId);

            }
        });
        cancel_dlg.show();


    }

    @Override
    public void OnComplaintsEvent(int position, String orderId) { //投诉


        getOrderLimits(orderId);


    }

    @Override
    public void OnFeedbackEvent(int position, final String orderId, String feedbackResult) {//反馈结果

        feedback_dlg = DialogUtil.createHintOperateDialog(UserOrderListActivity.this, "", feedbackResult, "好的", "再次反馈", new BackCallListener() {
            @Override
            public void onCancel(View view, Object... obj) {

                feedback_dlg.dismiss();

            }

            @Override
            public void onEnsure(View view, Object... obj) {

                feedback_dlg.dismiss();

                getOrderLimits(orderId);


            }
        });
        feedback_dlg.show();


    }

    @Override
    public void OnPaymentEvent(int position, String orderId) {//付款

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.ORDER_ID, orderId);

        bundle.putInt(YpSettings.FROM_PAGE, 2);

        ActivityUtil.jump(UserOrderListActivity.this, UserOrderPayActivity.class, bundle, 0, 100);

    }

    @Override
    public void OnAdvisoryEvent(int position, String orderId, int counselId, int orderStatus, int counselType) {//咨询


        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.ORDER_ID, orderId);

        bundle.putInt(YpSettings.USERID, counselId);

        bundle.putInt(YpSettings.COUNSEL_TYPE, counselType);

        if (orderStatus == 5) {
            bundle.putInt(YpSettings.COUNSEL_STATUS, CounselOrderStatusTable.request_end);
        }

        bundle.putInt(YpSettings.FROM_PAGE, 1);

        ActivityUtil.jump(UserOrderListActivity.this, ChatCounselActivity.class, bundle, 0, 100);

    }

    @Override
    public void OnEvaluationEvent(int position, String orderId) {//评价

        Bundle bundle = new Bundle();

        bundle.putString(YpSettings.ORDER_ID, orderId);

        ActivityUtil.jump(UserOrderListActivity.this, UserOrderEvaluationActivity.class, bundle, 0, 100);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("OrderListEvent")

            }
    )
    public void orderListEvent(OrderListEvent event) {

        user_order_xrv.startRefresh();

    }


    @Override
    public void onAdapterIconClick(int position, Object data) {

        OrderDetailDto dto = (OrderDetailDto) data;

        if (dto == null) {

            return;
        }


        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, dto.getUser().getUserId());
        bundle.putInt(YpSettings.COUNSEL_TYPE, dto.getCounselType());


        ActivityUtil.jump(UserOrderListActivity.this, TarotOrAstrologyDetailActivity.class, bundle, 0, 100);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister(this);


    }
}
