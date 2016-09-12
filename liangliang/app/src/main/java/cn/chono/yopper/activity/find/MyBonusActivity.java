package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.GetPrize.GetPrizeReqBean;
import cn.chono.yopper.Service.Http.GetPrize.GetPrizeRespBean;
import cn.chono.yopper.Service.Http.GetPrize.GetPrizeService;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListMoreReqBean;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListMoreServices;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListReqBean;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListRespBean;
import cn.chono.yopper.Service.Http.MyBonusList.MyBonusListServices;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.address.AddressManagerActivity;
import cn.chono.yopper.adapter.BonusAdapter;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.recyclerview.Divider;
import cn.chono.yopper.recyclerview.DividerItemDecoration;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;

/**
 * Created by yangjinyu on 16/3/11.
 */
public class MyBonusActivity extends MainFrameActivity implements BonusAdapter.OnItemListener {

    private XRefreshView xrefreshView;

    private RecyclerView listView;

    private BonusAdapter mAdapter;

    private List<MyBonusListRespBean.MyBonusListRespContent.BonusItem> listData;

    private int _start = 0;

    private String _nextQuery;

    private LinearLayout error_network_layout;

    private LinearLayout error_no_data_layout;

    private AutoLinearLayout my_bonus_error_layout;

    private TextView error_tv;

    private TextView error_no_data_tv;

    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooters;

    private Dialog loadingDiaog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.my_bonus_activity);

        PushAgent.getInstance(this).onAppStart();

        getTvTitle().setText("领奖");

        getBtnGoBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        int loginSex = DbHelperUtils.getDbUserSex(LoginUser.getInstance().getUserId());

        if (loginSex == 1) {
            gettvOption().setVisibility(View.GONE);
        } else {
            gettvOption().setVisibility(View.VISIBLE);
            gettvOption().setText("兑换");
            gettvOption().setTextColor(getResources().getColor(R.color.color_ff7462));
            gettvOption().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(MyBonusActivity.this, "btn_find_event_exchangebonus");
                    ActivityUtil.jumpForResult(MyBonusActivity.this, ExchangeBonusActivity.class, null, 6600, 0, 100);
                }
            });
        }


        intView();
        setxRefreshView();
        loadingDiaog = DialogUtil.LoadingDialog(this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }
    }


    @Override
    public void onResume() {

        super.onResume();

        MobclickAgent.onPageStart("领奖"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        onRefreshData(_start);

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("领奖"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    private void intView() {

        my_bonus_error_layout = (AutoLinearLayout) findViewById(R.id.my_bonus_error_layout);

        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);

        error_tv = (TextView) findViewById(R.id.error_network_tv);

        error_no_data_layout = (LinearLayout) findViewById(R.id.my_bonys_error_no_data_layout);

        error_no_data_tv = (TextView) findViewById(R.id.error_no_data_tv);

        xrefreshView = (XRefreshView) findViewById(R.id.my_bonus_xfefresh);

        listView = (RecyclerView) findViewById(R.id.my_bonus_recycler);

        listData = new ArrayList<>();

        mAdapter = new BonusAdapter(this);

        DividerItemDecoration localDividerItemDecoration = new DividerItemDecoration();

        localDividerItemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            public Divider getHorizontalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(Color.alpha(getResources().getColor(R.color.color_eeeeee))).size(12).build();
            }

            public Divider getVerticalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(Color.alpha(Color.alpha(getResources().getColor(R.color.color_eeeeee)))).size(12).build();
            }
        });

        listView.addItemDecoration(localDividerItemDecoration);

        listView.setLayoutManager(new LinearLayoutManager(this));

        listView.setAdapter(mAdapter);

        mAdapter.setOnItemListener(this);

        error_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_network_layout.setVisibility(View.GONE);
                my_bonus_error_layout.setVisibility(View.GONE);
                onRefreshData(_start);
            }
        });

    }


    private void setxRefreshView() {

        listView.setHasFixedSize(true);

        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        mXRefreshViewFooters = new XRefreshViewFooters(this);

        mXRefreshViewFooters.setRecyclerView(listView);

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
                    @Override
                    public void run() {

                        onLoadMoreData(_nextQuery);
                    }
                }, 1000);
            }
        });


        this.xrefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        _start = 0;
                        onRefreshData(_start);
                    }
                }, 1000);
            }

            public void onLoadMore(boolean paramAnonymousBoolean) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        onLoadMoreData(_nextQuery);
                    }
                }, 1000);
            }
        });
    }


    private void onRefreshData(int start) {
        this.xrefreshView.setLoadComplete(false);

        //设置请求参数
        MyBonusListReqBean myBonusListReqBean = new MyBonusListReqBean();
        myBonusListReqBean.setStart(start);

        MyBonusListServices myBonusListServices = new MyBonusListServices(this);
        myBonusListServices.parameter(myBonusListReqBean);
        myBonusListServices.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();

                MyBonusListRespBean myBonusListRespBean = (MyBonusListRespBean) respBean;

                MyBonusListRespBean.MyBonusListRespContent resp = myBonusListRespBean.getResp();

                if (resp != null) {
                    listData = resp.getList();

                    _nextQuery = resp.getNextQuery();

                    if (listData != null && listData.size() > 0) {
                        mAdapter.setListData(listData);
                    }
                    mAdapter.notifyDataSetChanged();

                    if (listData.size() == 0) {
                        error_no_data_layout.setVisibility(View.VISIBLE);
                    } else {
                        error_no_data_layout.setVisibility(View.GONE);
                    }
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

                if (listData.size() == 0) {
                    my_bonus_error_layout.setVisibility(View.VISIBLE);
                    error_network_layout.setVisibility(View.VISIBLE);
                    return;
                }

                String str = respBean.getMsg();
                if (!TextUtils.isEmpty(str)) {
                    DialogUtil.showDisCoverNetToast(MyBonusActivity.this, str);
                    return;
                }
                DialogUtil.showDisCoverNetToast(MyBonusActivity.this);

            }
        });

        myBonusListServices.enqueue();
    }

    private void onLoadMoreData(final String nextQuery) {

        if (TextUtils.isEmpty(nextQuery)) {
            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshView.stopLoadMore(false);
            DialogUtil.showDisCoverNetToast(MyBonusActivity.this, "已是最后一页");
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

                MyBonusListRespBean myBonusListRespBean = (MyBonusListRespBean) respBean;

                MyBonusListRespBean.MyBonusListRespContent resp = myBonusListRespBean.getResp();
                if (resp != null) {

                    _nextQuery = resp.getNextQuery();

                    if (resp.getList() != null && resp.getList().size() > 0) {

                        mAdapter.addData(resp.getList());
                        mAdapter.notifyDataSetChanged();
                    }


                }

                if (TextUtils.isEmpty(_nextQuery)) {
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

                String str = respBean.getMsg();
                if (!TextUtils.isEmpty(str)) {
                    DialogUtil.showDisCoverNetToast(MyBonusActivity.this, str);
                    return;
                }
                DialogUtil.showDisCoverNetToast(MyBonusActivity.this);

            }
        });
        moreServices.enqueue();
    }

    /**
     * 领奖
     */
    private void getBonus(String prizeId) {
        GetPrizeReqBean reqBean = new GetPrizeReqBean();
        reqBean.setUserPrizeId(prizeId);

        GetPrizeService getPrizeService = new GetPrizeService(this);

        if (reqBean == null) {
            return;
        }

        getPrizeService.parameter(reqBean);
        getPrizeService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                GetPrizeRespBean prizeRespBean = (GetPrizeRespBean) respBean;
                GetPrizeRespBean.GetPrizeRespBeanContent resp = prizeRespBean.getResp();

                if (null == resp) return;

                String resultStr = resp.getMsg();

                if (TextUtils.isEmpty(resultStr)) {
                    DialogUtil.showDisCoverNetToast(MyBonusActivity.this, "领取成功");
                    _start = 0;
                    onRefreshData(_start);
                } else {
                    DialogUtil.showDisCoverNetToast(MyBonusActivity.this, resp.getMsg());
                }
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String msg = respBean.getMsg();

                if (!TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(MyBonusActivity.this, msg);
                    return;
                }
                DialogUtil.showDisCoverNetToast(MyBonusActivity.this);
            }
        });
        getPrizeService.enqueue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6600) {
            _start = 0;
            onRefreshData(_start);
        }
    }

    @Override
    public void onReceive(View view, int position, MyBonusListRespBean.MyBonusListRespContent.BonusItem itemData) {

        Bundle localBundle = null;
        if (itemData.getPrize().getType() == 2) {//抽奖券
            if (itemData.getStatus() == 1) {
                return;
            }
            localBundle = new Bundle();
            localBundle.putString("from_flag", "mybonus");
            localBundle.putString("userprize_id", itemData.getUserPrizeId());
            ActivityUtil.jumpForResult(this, DrawActivity.class, localBundle, 6600, 0, 100);

        } else if (itemData.getPrize().getType() == 3) {//苹果
            if (itemData.getStatus() == 1) {
                return;
            }
            getBonus(itemData.getUserPrizeId());

        } else if (itemData.getPrize().getType() == 1) {

            if (itemData.getStatus() == 0) {//可领取
                localBundle = new Bundle();
                localBundle.putString("from_flag", "mybonus");
                localBundle.putString("bouns_id", itemData.getUserPrizeId());
                ActivityUtil.jumpForResult(this, AddressManagerActivity.class, localBundle, 6600, 0, 100);

            } else if (itemData.getStatus() == 1) {//已领取
                DialogUtil.showDisCoverNetToast(MyBonusActivity.this, "已领取");

            } else if (itemData.getStatus() == 2) {//已过期
                DialogUtil.showDisCoverNetToast(MyBonusActivity.this, "已过期");

            } else if (itemData.getStatus() == 3) {//已兑换
                DialogUtil.showDisCoverNetToast(MyBonusActivity.this, "已兑换");

            }

        }
    }
}
