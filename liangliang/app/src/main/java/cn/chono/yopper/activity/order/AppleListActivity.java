package cn.chono.yopper.activity.order;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderProduct.OrderProductBean;
import cn.chono.yopper.Service.Http.OrderProduct.OrderProductEntity;
import cn.chono.yopper.Service.Http.OrderProduct.OrderProductRespEntity;
import cn.chono.yopper.Service.Http.OrderProduct.OrderProductService;
import cn.chono.yopper.Service.Http.Products.AppleInfoEntity;
import cn.chono.yopper.Service.Http.Products.ProductsBean;
import cn.chono.yopper.Service.Http.Products.ProductsDataEntity;
import cn.chono.yopper.Service.Http.Products.ProductsEntity;
import cn.chono.yopper.Service.Http.Products.ProductsListEntity;
import cn.chono.yopper.Service.Http.Products.ProductsRespEntity;
import cn.chono.yopper.Service.Http.Products.ProductsService;
import cn.chono.yopper.Service.Http.ProductsMore.ProductsMoreBean;
import cn.chono.yopper.Service.Http.ProductsMore.ProductsMoreService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.AppleListAdapter;
import cn.chono.yopper.event.AppleListEvent;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;

/**
 * 购买苹果列表
 * Created by cc on 16/5/5.
 */
public class AppleListActivity extends MainFrameActivity implements OnAdapterItemClickLitener {

    XRefreshView appleList_xrv;

    RecyclerView appleList_rv;

    AppleListAdapter mAppleListAdapter;

    XRefreshViewFooters mXRefreshViewFooter;

    XRefreshViewHeaders mXRefreshViewHeaders;

    int productType = 0;

    String nextQuery;

    int count;// 商品数量

    int giveCount; // 赠送数量

    ViewStub appleList_sv;

    TextView error_no_data_tv;

    Dialog mLoadingDiaog;

    boolean isOrderItemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_apple_list);

        PushAgent.getInstance(this).onAppStart();


        RxBus.get().register(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.PRODUCT_TYPE))
            productType = bundle.getInt(YpSettings.PRODUCT_TYPE);


        initView();

        initXRefreshView();

        mLoadingDiaog.show();


        getRefreshAppleList(productType);


    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("购买苹果列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("购买苹果列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    private void NoData(int Visibility) {
        if (Visibility == View.GONE) {
            appleList_sv.setVisibility(View.GONE);
        } else {
            appleList_sv.setVisibility(View.VISIBLE);

            error_no_data_tv = (TextView) findViewById(R.id.error_no_data_tv);
            error_no_data_tv.setText("暂无数据");
        }

    }

    private void initView() {

        mLoadingDiaog = DialogUtil.LoadingDialog(this);

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getTvTitle().setText(getResources().getString(R.string.pay_apple));


        appleList_sv = (ViewStub) findViewById(R.id.appleList_sv);


        appleList_xrv = (XRefreshView) findViewById(R.id.appleList_xrv);

        appleList_rv = (RecyclerView) findViewById(R.id.appleList_rv);

        appleList_rv.setLayoutManager(new LinearLayoutManager(this));

        mAppleListAdapter = new AppleListAdapter(this, this);

        appleList_rv.setAdapter(mAppleListAdapter);

        appleList_rv.setHasFixedSize(true);

    }

    private void initXRefreshView() {

        mXRefreshViewFooter = new XRefreshViewFooters(this);

        mXRefreshViewFooter.setRecyclerView(appleList_rv);

        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        mAppleListAdapter.setCustomLoadMoreView(mXRefreshViewFooter);


        appleList_xrv.setCustomHeaderView(mXRefreshViewHeaders);


        appleList_xrv.setPullLoadEnable(true);

        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        appleList_xrv.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        appleList_xrv.setAutoLoadMore(true);


        mXRefreshViewFooter.onAutoLoadMoreFail(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getLoadMoreAppleList();

                    }
                }, 1000);

            }
        });


        appleList_xrv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getRefreshAppleList(productType);


                    }
                }, 1000);

            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getLoadMoreAppleList();

                    }
                }, 1000);
            }
        });


    }


    private void getRefreshAppleList(int productType) {


        ProductsBean productsBean = new ProductsBean();

        productsBean.productType = productType;

        ProductsService productsService = new ProductsService(this);

        productsService.parameter(productsBean);

        productsService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                mLoadingDiaog.dismiss();

                ProductsRespEntity productsRespEntity = (ProductsRespEntity) respBean;

                ProductsDataEntity productsDataEntity = productsRespEntity.resp;

                if (productsDataEntity == null) {

                    if (mAppleListAdapter.getProductsListEntities() == null || mAppleListAdapter.getProductsListEntities().size() == 0) {
                        NoData(View.VISIBLE);
                        appleList_xrv.setVisibility(View.GONE);
                    }


                    appleList_xrv.stopRefresh();
                    return;
                }
                AppleInfoEntity appleInfoEntity = productsDataEntity.appleInfo;

                if (appleInfoEntity != null) {

                    mAppleListAdapter.setAppleInfoEntity(appleInfoEntity);

                }


                ProductsEntity productsEntity = productsDataEntity.products;
                if (productsEntity != null) {

                    nextQuery = productsEntity.nextQuery;

                    mAppleListAdapter.setPrivacyList(productsEntity.list);
                }

                if (mAppleListAdapter.getProductsListEntities() == null || mAppleListAdapter.getProductsListEntities().size() == 0) {
                    NoData(View.VISIBLE);
                    appleList_xrv.setVisibility(View.GONE);
                } else {
                    NoData(View.GONE);
                    appleList_xrv.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(nextQuery)) {

                    mXRefreshViewFooter.setLoadcomplete(true);


                } else {
                    mXRefreshViewFooter.setLoadcomplete(false);
                }

                appleList_xrv.stopRefresh();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mLoadingDiaog.dismiss();

                if (mAppleListAdapter.getProductsListEntities() == null || mAppleListAdapter.getProductsListEntities().size() == 0) {
                    NoData(View.VISIBLE);
                    appleList_xrv.setVisibility(View.GONE);
                }

                mXRefreshViewHeaders.onRefreshFail();

                appleList_xrv.stopRefresh();
            }
        });
        productsService.enqueue();

    }

    private void getLoadMoreAppleList() {

        if (TextUtils.isEmpty(nextQuery)) {
            mXRefreshViewFooter.setLoadcomplete(true);
            appleList_xrv.stopLoadMore(false);
            return;
        }


        ProductsMoreBean productsMoreBean = new ProductsMoreBean();

        productsMoreBean.nextQuery = nextQuery;

        ProductsMoreService productsMoreService = new ProductsMoreService(this);

        productsMoreService.parameter(productsMoreBean);

        productsMoreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                ProductsRespEntity productsRespEntity = (ProductsRespEntity) respBean;

                ProductsDataEntity productsDataEntity = productsRespEntity.resp;

                if (productsDataEntity == null) {
                    appleList_xrv.stopLoadMore(false);
                    return;
                }


                ProductsEntity productsEntity = productsDataEntity.products;
                if (productsEntity == null) {

                    appleList_xrv.stopLoadMore(false);
                    return;


                }

                nextQuery = productsEntity.nextQuery;

                mAppleListAdapter.addPrivacyList(productsEntity.list);

                if (TextUtils.isEmpty(nextQuery)) {
                    mXRefreshViewFooter.setLoadcomplete(true);
                    appleList_xrv.stopLoadMore(false);
                    return;
                }
                mXRefreshViewFooter.setLoadcomplete(false);
                appleList_xrv.stopLoadMore();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                appleList_xrv.stopLoadMore(false);
            }
        });

        productsMoreService.enqueue();

    }


    @Override
    public void onAdapterItemClick(int position, Object data) {


        if (isOrderItemClick) {
            return;
        }
        isOrderItemClick = true;
        ProductsListEntity productsListEntity = (ProductsListEntity) data;

        count = productsListEntity.count;
        giveCount = productsListEntity.giveCount;

        postOrderProduct(productType, productsListEntity.productId, count + giveCount, (productsListEntity.price / 100));


    }

    private void postOrderProduct(int orderType, String productId, final int apple_num, final long cost) {

        if (orderType == Constant.ProductType_Apple) {

            orderType = Constant.Orders_ProductType_Apple;

        } else if (orderType == Constant.ProductType_Star) {

            orderType = Constant.Orders_ProductType_Star;
        }

        OrderProductBean orderProductBean = new OrderProductBean();

        orderProductBean.orderType = orderType;

        orderProductBean.productId = productId;

        OrderProductService orderProductService = new OrderProductService(this);


        orderProductService.parameter(orderProductBean);

        orderProductService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                isOrderItemClick = false;

                OrderProductRespEntity orderProductRespEntity = (OrderProductRespEntity) respBean;


                OrderProductEntity orderProductEntity = orderProductRespEntity.resp;

                if (orderProductEntity == null) {


                    return;
                }

                if (orderProductEntity.result) {

                    Bundle bundle = new Bundle();


                    bundle.putString(YpSettings.ORDER_ID, orderProductEntity.orderId);

                    bundle.putString(YpSettings.ProductName, apple_num+"苹果");

                    bundle.putLong(YpSettings.PAY_COST, cost);

                    bundle.putInt(YpSettings.PAY_TYPE, 1);

                    ActivityUtil.jump(AppleListActivity.this, UserAppleOrderPayActivity.class, bundle, 0, 100);

                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                isOrderItemClick = false;
            }
        });

        orderProductService.enqueue();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("AppleListEvent")

            }
    )
    public void appleListEvent(AppleListEvent event) {

        AppleInfoEntity appleInfoEntity = mAppleListAdapter.getAppleInfoEntity();

        appleInfoEntity.availableBalance = appleInfoEntity.availableBalance + count + giveCount;

        mAppleListAdapter.setAppleInfoEntity(appleInfoEntity);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
