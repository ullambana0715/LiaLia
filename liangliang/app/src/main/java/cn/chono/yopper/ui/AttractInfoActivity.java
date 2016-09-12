package cn.chono.yopper.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.chono.yopper.R;
import cn.chono.yopper.adapter.AttractListAdapter;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.entity.charm.ReceiveGiftInfoEntity;
import cn.chono.yopper.presenter.AttractInfoContract;
import cn.chono.yopper.presenter.AttractInfoPresenter;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;

/**
 * Created by sunquan on 16/8/4.
 */
public class AttractInfoActivity extends BaseActivity<AttractInfoPresenter> implements AttractInfoContract.View {


    AttractListAdapter mAttractListAdapter;

    @BindView(R.id.no_data_tv)
    TextView noDataTv;


    private int userID;

    private int charmNum;

    private int account;


    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;

    @BindView(R.id.atrrack_info_charm_num_tv)
    TextView atrrackInfoCharmNumTv;

    @BindView(R.id.atrrack_info_account_tv)
    TextView atrrackInfoAccountTv;

    @BindView(R.id.attract_info_gift_rv)
    RecyclerView attractInfoGiftRv;

    @BindView(R.id.attract_info_xrefreshview)
    XRefreshView attractInfoXrefreshview;

    @BindView(R.id.atrrack_info_ll)
    LinearLayout atrrackInfoLl;

    @BindView(R.id.error_network_vs)
    ViewStub errorNetworkVs;

    XRefreshViewFooters mXRefreshViewFooters;

    XRefreshViewHeaders mXRefreshViewHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);


        baseTitleTv.setText("魅力值介绍");

        attractInfoGiftRv.setItemAnimator(new DefaultItemAnimator());

        attractInfoGiftRv.setLayoutManager(new LinearLayoutManager(this));

        mAttractListAdapter = new AttractListAdapter(this);

        attractInfoGiftRv.setAdapter(mAttractListAdapter);

        setXrefreshListener();
    }

    @Override
    protected int getLayout() {
        return R.layout.act_attrack_info;
    }

    @Override
    protected AttractInfoPresenter getPresenter() {
        return new AttractInfoPresenter(mContext, this);
    }

    @Override
    protected void initVariables() {

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            userID = bundle.getInt(YpSettings.USERID);
        }

    }

    @Override
    protected void initView() {

//        baseTitleTv.setText("魅力值介绍");
//
//        attractInfoGiftRv.setItemAnimator(new DefaultItemAnimator());
//
//        attractInfoGiftRv.setLayoutManager(new LinearLayoutManager(this));
//
//        mAttractListAdapter = new AttractListAdapter(this);
//
//        attractInfoGiftRv.setAdapter(mAttractListAdapter);


    }

    @Override
    protected void initDataAndLoadData() {

        mPresenter.getAttractInfo(0);


    }


    @OnClick({R.id.base_back_layout, R.id.atrrack_info_withdraw_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_back_layout:
                finish();
                break;
            case R.id.atrrack_info_withdraw_tv:


                Bundle bundle = new Bundle();

                bundle.putInt(YpSettings.WithDraw_Account, account);

                bundle.putInt(YpSettings.Charm_Num, charmNum);

                bundle.putInt(YpSettings.USERID, userID);

                ActivityUtil.jump(AttractInfoActivity.this, WithdrawActivity.class, bundle, 0, 100);
                break;
        }
    }

    @Override
    public void setCharmNumView(int charm) {
        charmNum = charm;

        atrrackInfoCharmNumTv.setText(charm + "");
    }

    @Override
    public void setAccountView(int accountNum) {

        account = accountNum;


        Logger.e("---accountNum------" + accountNum);

        double f = accountNum / 100.00;


        DecimalFormat df = new DecimalFormat("#.#");

        String st = df.format(f);


        atrrackInfoAccountTv.setText(st + "元");


    }

    @Override
    public void showError() {

        errorNetworkVs.setVisibility(View.VISIBLE);

        atrrackInfoLl.setVisibility(View.GONE);

        LinearLayout error_network_layout = (LinearLayout) this.findViewById(R.id.error_network_layout);

        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) this.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(v -> {

            errorNetworkVs.setVisibility(View.GONE);

            atrrackInfoLl.setVisibility(View.GONE);

            mPresenter.getAttractInfo(0);
        });

    }


    @Override
    public void updateGiftListView(List<ReceiveGiftInfoEntity> list) {
        errorNetworkVs.setVisibility(View.GONE);
        atrrackInfoLl.setVisibility(View.VISIBLE);
        mAttractListAdapter.setList(list);
        mAttractListAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreGiftListView(List<ReceiveGiftInfoEntity> list) {

        mAttractListAdapter.addList(list);

        mAttractListAdapter.notifyDataSetChanged();

    }

    @Override
    public void showErrorHint(String msg) {

        DialogUtil.showDisCoverNetToast(this, msg);
    }

    @Override
    public void showNoData() {
        noDataTv.setVisibility(View.VISIBLE);

        attractInfoGiftRv.setVisibility(View.GONE);
    }


    public void setXrefreshListener() {

        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        attractInfoXrefreshview.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshViewFooters = new XRefreshViewFooters(this);

        mXRefreshViewFooters.setRecyclerView(attractInfoGiftRv);


        mAttractListAdapter.setCustomLoadMoreView(mXRefreshViewFooters);

        attractInfoXrefreshview.setPullRefreshEnable(false);

        attractInfoXrefreshview.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        attractInfoXrefreshview.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        attractInfoXrefreshview.setAutoLoadMore(true);

        mXRefreshViewFooters.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mPresenter.loadMoreGift();
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

                        mPresenter.loadMoreGift();
                    }
                }, 1000);
            }
        });


        attractInfoXrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {


            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        Logger.e("lllllllllllll");

                        mPresenter.loadMoreGift();

                    }
                }, 1000);
            }
        });

    }

    @Override
    public void stopRefresh() {

        attractInfoXrefreshview.stopRefresh();

    }

    @Override
    public void setLoadcomplete(boolean b) {

        mXRefreshViewFooters.setLoadcomplete(b);


    }

    @Override
    public void stopLoadMore(boolean b) {
        attractInfoXrefreshview.stopLoadMore(b);
    }

    @Override
    public void stopLoadMore() {
        attractInfoXrefreshview.stopLoadMore();
    }

}
