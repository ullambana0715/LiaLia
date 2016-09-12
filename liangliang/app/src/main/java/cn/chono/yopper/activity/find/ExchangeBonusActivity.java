package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.GainPFruit.AvailableEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitBean;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitRespEntity;
import cn.chono.yopper.Service.Http.GainPFruit.GainPFruitService;
import cn.chono.yopper.Service.Http.MyExchanagePrize.ExpiryDataBean;
import cn.chono.yopper.Service.Http.MyExchanagePrize.ExpiryDataRespBean;
import cn.chono.yopper.Service.Http.MyExchanagePrize.ExpiryDataService;
import cn.chono.yopper.Service.Http.MyExchanagePrize.ExpiryMoreDataBean;
import cn.chono.yopper.Service.Http.MyExchanagePrize.ExpiryMoreDataService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.base.App;
import cn.chono.yopper.activity.address.AddressManagerActivity;
import cn.chono.yopper.adapter.ExchanagePrizeAdapter;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.recyclerview.Divider;
import cn.chono.yopper.recyclerview.DividerItemDecoration;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;

public class ExchangeBonusActivity extends MainFrameActivity implements ExchanagePrizeAdapter.OnItemClickListener {
    private String _nextQuery;
    private int _start = 0;
    private ExchanagePrizeAdapter adapter;
    private boolean canChanage = true;
    private RecyclerView mRecyclerView;
    private List<ExpiryDataRespBean.ExchanageResp.PrizeData> listData;
    private TextView mAppleView;
    private TextView mAppleCost_tv;
    private int pguo_count = 0;
    private int apple_cost = 0;
    private XRefreshView xRefreshView;

    private AutoLinearLayout exchanage_bonus_error_layout;

    private LinearLayout error_network_layout;

    private TextView error_network_tv;

    private LinearLayout error_no_data_layout;
    private RelativeLayout apple_count_layout;

    private TextView error_no_data_tv;

    XRefreshViewHeaders mXRefreshViewHeaders;
    XRefreshViewFooters mXRefreshViewFooters;

    private Dialog loadingDiaog;

    private void initView() {

        exchanage_bonus_error_layout = (AutoLinearLayout) findViewById(R.id.exchanage_bonus_error_layout);
        apple_count_layout = (RelativeLayout) findViewById(R.id.apple_draw);
        mAppleCost_tv = (TextView) findViewById(R.id.bonus_exchanage_btn);
        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);

        error_network_tv = (TextView) findViewById(R.id.error_network_tv);

        error_no_data_layout = (LinearLayout) findViewById(R.id.exchanage_no_data_layout);

        error_no_data_tv = (TextView) findViewById(R.id.error_no_data_tv);

        this.xRefreshView = ((XRefreshView) findViewById(R.id.exchanage_bonus_xresh_view));

        this.mRecyclerView = ((RecyclerView) findViewById(R.id.exchanage_bonus_rv));

        DividerItemDecoration localDividerItemDecoration = new DividerItemDecoration();

        localDividerItemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            public Divider getHorizontalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(Color.alpha(getResources().getColor(R.color.color_eeeeee))).size(2).build();
            }

            public Divider getVerticalDivider(int paramAnonymousInt) {
                return new Divider.Builder().color(Color.alpha(Color.alpha(getResources().getColor(R.color.color_eeeeee)))).size(2).build();
            }
        });

        mRecyclerView.addItemDecoration(localDividerItemDecoration);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickLitener(this);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchanage_bonus_error_layout.setVisibility(View.GONE);
                error_network_layout.setVisibility(View.GONE);
                onRefreshData(_start);
            }
        });

        error_no_data_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_no_data_layout.setVisibility(View.GONE);
                onRefreshData(_start);
            }
        });

        apple_count_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle localBundle = new Bundle();
                localBundle.putString("from_flag", "mybonus");
                localBundle.putString("userprize_id", "2");
                localBundle.putInt("applecost", apple_cost);
                localBundle.putInt("applecount", pguo_count);
                localBundle.putInt(DrawActivity.ENTRANCE, DrawActivity.FROM_MYACCOUNT);

                ActivityUtil.jump(ExchangeBonusActivity.this, DrawActivity.class, localBundle, 0, 0);
            }
        });

        setxRefreshView();
        if (!isFinishing()) {
            loadingDiaog.show();
        }
        onRefreshData(_start);
    }


    private void onLoadMoreData(String paramString) {
        if (TextUtils.isEmpty(paramString)) {

            mXRefreshViewFooters.setLoadcomplete(true);
            this.xRefreshView.setLoadComplete(false);
            DialogUtil.showDisCoverNetToast(this, "已是最后一页");
            return;
        }

        ExpiryMoreDataBean localExpiryMoreDataBean = new ExpiryMoreDataBean();

        localExpiryMoreDataBean.setNextQuery(paramString);

        ExpiryMoreDataService localExpiryMoreDataService = new ExpiryMoreDataService(this);

        localExpiryMoreDataService.parameter(localExpiryMoreDataBean);

        localExpiryMoreDataService.callBack(new OnCallBackSuccessListener() {
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                ExpiryDataRespBean localExpiryDataRespBean = (ExpiryDataRespBean) respBean;

                ExpiryDataRespBean.ExchanageResp resp = localExpiryDataRespBean.getResp();

                if (resp != null) {
                    _nextQuery = resp.getNextQuery();

                    if (resp.getList() != null && resp.getList().size() > 0) {

                        adapter.addData(listData, canChanage);
                        adapter.notifyDataSetChanged();
                    }


                }
                if (TextUtils.isEmpty(_nextQuery)) {
                    mXRefreshViewFooters.setLoadcomplete(true);
                    xRefreshView.stopLoadMore(false);

                } else {
                    mXRefreshViewFooters.setLoadcomplete(false);
                    xRefreshView.stopLoadMore();
                }
            }
        }, new OnCallBackFailListener() {
            public void onFail(RespBean respBean) {
                super.onFail(respBean);


                mXRefreshViewFooters.setLoadcomplete(false);
                xRefreshView.stopLoadMore(false);

                String str = respBean.getMsg();

                if (!TextUtils.isEmpty(str)) {
                    DialogUtil.showDisCoverNetToast(ExchangeBonusActivity.this, str);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ExchangeBonusActivity.this);
            }
        });
        localExpiryMoreDataService.enqueue();
    }

    private void onRefreshData(int paramInt) {


        ExpiryDataBean localExpiryDataBean = new ExpiryDataBean();

        localExpiryDataBean.setStart(paramInt);

        ExpiryDataService localExpiryDataService = new ExpiryDataService(this);

        localExpiryDataService.parameter(localExpiryDataBean);

        localExpiryDataService.callBack(new OnCallBackSuccessListener() {

            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();

                ExpiryDataRespBean localExpiryDataRespBean = (ExpiryDataRespBean) respBean;

                ExpiryDataRespBean.ExchanageResp resp = localExpiryDataRespBean.getResp();

                if (resp != null) {
                    listData = resp.getList();

                    _nextQuery = resp.getNextQuery();

                    canChanage = resp.isCanExcange();

                    if (listData != null && listData.size() > 0) {
                        adapter.setDatalist(listData, canChanage);
                    }
                    adapter.notifyDataSetChanged();

                    if (listData.size() == 0) {
                        error_no_data_layout.setVisibility(View.VISIBLE);
                    } else {
                        error_no_data_layout.setVisibility(View.GONE);
                    }
                    mAppleCost_tv.setText(resp.getAppleDrawPrice() + "苹果/次");
                    apple_cost = resp.getAppleDrawPrice();
                }

                xRefreshView.stopRefresh();
            }
        }, new OnCallBackFailListener() {
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                loadingDiaog.dismiss();


                mXRefreshViewHeaders.onRefreshFail();

                xRefreshView.stopRefresh();

                if (listData.size() == 0) {
                    exchanage_bonus_error_layout.setVisibility(View.VISIBLE);
                    error_network_layout.setVisibility(View.VISIBLE);
                }

                String str = respBean.getMsg();
                if (!TextUtils.isEmpty(str)) {
                    DialogUtil.showDisCoverNetToast(ExchangeBonusActivity.this, str);
                    return;
                }
                DialogUtil.showDisCoverNetToast(ExchangeBonusActivity.this);

            }
        });
        localExpiryDataService.enqueue();
    }

    private void setxRefreshView() {


        this.mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        this.mXRefreshViewFooters = new XRefreshViewFooters(this);

        this.mXRefreshViewFooters.setRecyclerView(mRecyclerView);

        this.xRefreshView.setCustomHeaderView(mXRefreshViewHeaders);

        adapter.setCustomLoadMoreView(mXRefreshViewFooters);

        xRefreshView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xRefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        xRefreshView.setAutoLoadMore(true);


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


        this.xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6600) {

            _start = 0;
            onRefreshData(_start);
        }
    }


    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        PushAgent.getInstance(this).onAppStart();

        setContentLayout(R.layout.exchange_bonus_activity);

        mAppleView = ((TextView) findViewById(R.id.my_apple_no));

        listData = new ArrayList();

        adapter = new ExchanagePrizeAdapter(this);

        getTvTitle().setText("兑奖");

        getBtnGoBack().setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                setResult(1);
                finish();

            }
        });

        loadingDiaog = DialogUtil.LoadingDialog(this, null);

        initView();
    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("兑奖"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        get_p_fruit_point();

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("兑奖"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }


    /**
     * 获取用户信息
     */
    //获取P果数量
    private void get_p_fruit_point() {

        GainPFruitBean fruitBean = new GainPFruitBean();
        fruitBean.setUserId(LoginUser.getInstance().getUserId());

        GainPFruitService pFruitService = new GainPFruitService(this);

        pFruitService.parameter(fruitBean);
        pFruitService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                GainPFruitRespEntity fruitRespBean = (GainPFruitRespEntity) respBean;
                AvailableEntity available = fruitRespBean.resp;
                pguo_count = available.availableBalance;
                mAppleView.setText(String.valueOf(pguo_count));
            }
        }, new OnCallBackFailListener());

        pFruitService.enqueue();
    }

    @Override
    public void onItemClikc(View paramView, int paramInt, ExpiryDataRespBean.ExchanageResp.PrizeData paramPrizeData) {

        if (!canChanage) {
            DialogUtil.showDisCoverNetToast(this, "本周已兑换过奖品");
            return;
        }

        if (paramPrizeData.getPrize().getState() == 1) {
            DialogUtil.showDisCoverNetToast(this, "已领取");
            return;
        }

        if (paramPrizeData.getPrize().getState() == 2) {
            DialogUtil.showDisCoverNetToast(this, "已过期");
            return;
        }

        if (paramPrizeData.getPrize().getState() == 3) {
            DialogUtil.showDisCoverNetToast(this, "本周已兑换过奖品");
            return;
        }

        if (paramPrizeData.getPrize().getAppleCount() > pguo_count) {
            DialogUtil.showDisCoverNetToast(this, "苹果余额不足");
            return;
        }

        Bundle localBundle = new Bundle();

        localBundle.putString("from_flag", "exchanagebonus");
        localBundle.putString("bouns_id", paramPrizeData.getPrize().getPrizeId());

        ActivityUtil.jumpForResult(this, AddressManagerActivity.class, localBundle, 6600, 0, 100);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
