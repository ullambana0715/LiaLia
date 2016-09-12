package cn.chono.yopper.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.chono.yopper.R;
import cn.chono.yopper.adapter.WithDrawRecordsAdapter;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.entity.WithDrawItemEntity;
import cn.chono.yopper.presenter.WithDrawRecordsContract;
import cn.chono.yopper.presenter.WithDrawRecordsPresenter;

/**
 * Created by sunquan on 16/8/4.
 */
public class WithDrawRecordsActivity extends BaseActivity<WithDrawRecordsPresenter> implements WithDrawRecordsContract.View {

    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;

    @BindView(R.id.withdraw_record_rv)
    RecyclerView withdrawRecordRv;


    @BindView(R.id.error_no_data_vs)
    ViewStub errorNoDataVs;

    @BindView(R.id.error_network_vs)
    ViewStub errorNetworkVs;

    WithDrawRecordsAdapter mWithDrawRecordsAdapter;

    private int userID;

    @Override
    protected int getLayout() {
        return R.layout.act_withdraw_records;
    }

    @Override
    protected WithDrawRecordsPresenter getPresenter() {
        return new WithDrawRecordsPresenter(mContext, this);
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

        baseTitleTv.setText("提现记录");

        withdrawRecordRv.setItemAnimator(new DefaultItemAnimator());
        withdrawRecordRv.setLayoutManager(new LinearLayoutManager(this));
        mWithDrawRecordsAdapter = new WithDrawRecordsAdapter(this);
        withdrawRecordRv.setAdapter(mWithDrawRecordsAdapter);

    }

    @Override
    protected void initDataAndLoadData() {

        mPresenter.getWithDrawRecords(userID, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.base_back_layout)
    public void onClick() {
        finish();
    }

    @Override
    public void updateWithDrawRecordsView(List<WithDrawItemEntity> list) {
        errorNoDataVs.setVisibility(View.GONE);
        errorNetworkVs.setVisibility(View.GONE);
        withdrawRecordRv.setVisibility(View.VISIBLE);
        mWithDrawRecordsAdapter.setData(list);
        mWithDrawRecordsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoData() {
        errorNoDataVs.setVisibility(View.VISIBLE);
        errorNetworkVs.setVisibility(View.GONE);
        withdrawRecordRv.setVisibility(View.GONE);

        LinearLayout error_no_data_layout = (LinearLayout) this.findViewById(R.id.error_no_data_layout);
        error_no_data_layout.setVisibility(View.VISIBLE);
        TextView error_no_data_tv = (TextView) this.findViewById(R.id.error_no_data_tv);
        error_no_data_tv.setText("暂无提现记录");
    }

    @Override
    public void showError() {

        errorNoDataVs.setVisibility(View.GONE);
        errorNetworkVs.setVisibility(View.VISIBLE);
        withdrawRecordRv.setVisibility(View.GONE);

        LinearLayout error_network_layout = (LinearLayout) this.findViewById(R.id.error_network_layout);
        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) this.findViewById(R.id.error_network_tv);
        error_network_tv.setOnClickListener(v ->{
            errorNoDataVs.setVisibility(View.GONE);
            errorNetworkVs.setVisibility(View.GONE);
            withdrawRecordRv.setVisibility(View.GONE);
            mPresenter.getWithDrawRecords(userID, 0);
        });
    }
}
