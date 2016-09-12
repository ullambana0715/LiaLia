package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationDataEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationListBean;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationListRespEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationsEntity;
import cn.chono.yopper.Service.Http.EvaluationList.EvaluationsListService;
import cn.chono.yopper.Service.Http.EvaluationsListMore.EvaluationsListMoreBean;
import cn.chono.yopper.Service.Http.EvaluationsListMore.EvaluationsListMoreService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.adapter.TarotOrAstrologyEvaluationAdapter;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;

/**
 * 塔罗占星评价列表
 * Created by cc on 16/4/28.
 */
public class TarotOrAstrologyEvaluationListActivity extends MainFrameActivity implements OnAdapterItemClickLitener {


    XRefreshView tarotOrAstrologyEvaluation_xrv;

    RecyclerView arotOrAstrologyEvaluation_rv;


    XRefreshViewFooters mXRefreshViewFooter;

    XRefreshViewHeaders mXRefreshViewHeaders;


    TarotOrAstrologyEvaluationAdapter mTarotOrAstrologyEvaluationAdapter;


    int mUserId;

    int mCounselorType;

    String nextQuery;

    Dialog mLoadingDiaog;

    ViewStub tarotOrAstrology_vs;

    TextView error_no_data_tv;

    boolean isData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_tarot_astrology_evaluation);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.USERID))
            mUserId = bundle.getInt(YpSettings.USERID);

        if (bundle.containsKey(YpSettings.COUNSEL_TYPE))
            mCounselorType = bundle.getInt(YpSettings.COUNSEL_TYPE);


        initView();
        initXRefreshView();
        tarotOrAstrologyEvaluation_xrv.setVisibility(View.GONE);
        mLoadingDiaog.show();
        OnRefreshListData(mUserId);
    }


    private void NoData(int Visibility) {
        if (Visibility == View.GONE) {
            tarotOrAstrology_vs.setVisibility(View.GONE);
        } else {
            tarotOrAstrology_vs.setVisibility(View.VISIBLE);

            error_no_data_tv = (TextView) findViewById(R.id.error_no_data_tv);
            error_no_data_tv.setText("暂无数据");
        }

    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("塔罗占星评价列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("塔罗占星评价列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    private void initView() {

        mLoadingDiaog = DialogUtil.LoadingDialog(this);

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getTvTitle().setText("用户评价");

        tarotOrAstrology_vs = (ViewStub) findViewById(R.id.tarotOrAstrology_vs);


        tarotOrAstrologyEvaluation_xrv = (XRefreshView) findViewById(R.id.tarotOrAstrologyEvaluation_xrv);

        arotOrAstrologyEvaluation_rv = (RecyclerView) findViewById(R.id.arotOrAstrologyEvaluation_rv);


        arotOrAstrologyEvaluation_rv.setLayoutManager(new LinearLayoutManager(this));

        arotOrAstrologyEvaluation_rv.setItemAnimator(new DefaultItemAnimator());

        mTarotOrAstrologyEvaluationAdapter = new TarotOrAstrologyEvaluationAdapter(this);

        mTarotOrAstrologyEvaluationAdapter.setOnAdapterItemClickLitener(this);

        arotOrAstrologyEvaluation_rv.setAdapter(mTarotOrAstrologyEvaluationAdapter);


    }


    private void initXRefreshView() {




        mXRefreshViewFooter = new XRefreshViewFooters(this);

        mXRefreshViewFooter.setRecyclerView(arotOrAstrologyEvaluation_rv);

        mXRefreshViewHeaders=new XRefreshViewHeaders(this);

        tarotOrAstrologyEvaluation_xrv.setCustomHeaderView(mXRefreshViewHeaders);

        mTarotOrAstrologyEvaluationAdapter.setCustomLoadMoreView(mXRefreshViewFooter);

        tarotOrAstrologyEvaluation_xrv.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        tarotOrAstrologyEvaluation_xrv.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        tarotOrAstrologyEvaluation_xrv.setAutoLoadMore(true);


        mXRefreshViewFooter.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        OnLoadMoreListData(nextQuery);
                    }
                }, 1000);

            }
        });

        mXRefreshViewFooter.onAutoLoadMoreFail(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        OnLoadMoreListData(nextQuery);
                    }
                }, 1000);
            }
        });


        tarotOrAstrologyEvaluation_xrv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        OnRefreshListData(mUserId);


                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        OnLoadMoreListData(nextQuery);
                    }
                }, 1000);
            }

        });


    }


    private void OnRefreshListData(int userId) {

        EvaluationListBean evaluationListBean = new EvaluationListBean();

        evaluationListBean.receiveUserId = userId;

        EvaluationsListService evaluationsListService = new EvaluationsListService(this);


        evaluationsListService.parameter(evaluationListBean);


        evaluationsListService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                mLoadingDiaog.dismiss();

                EvaluationListRespEntity evaluationListRespEntity = (EvaluationListRespEntity) respBean;

                EvaluationDataEntity evaluationDataEntity = evaluationListRespEntity.resp;

                if (null == evaluationDataEntity) {

                    tarotOrAstrologyEvaluation_xrv.setVisibility(View.GONE);
                    NoData(View.VISIBLE);

                    mXRefreshViewHeaders.onRefreshFail();

                    tarotOrAstrologyEvaluation_xrv.stopRefresh();

                    return;

                }

                tarotOrAstrologyEvaluation_xrv.setVisibility(View.VISIBLE);
                NoData(View.GONE);

                mTarotOrAstrologyEvaluationAdapter.setDataAggregation(evaluationDataEntity.evaluationsAggregation);

                if (evaluationDataEntity.evaluationsAggregation !=null && evaluationDataEntity.evaluationsAggregation.total !=0){
                    StringBuilder sb=new StringBuilder("用户评价");

                    sb.append("(");
                    sb.append(evaluationDataEntity.evaluationsAggregation.total);
                    sb.append(")");

                    getTvTitle().setText(sb.toString());
                }




                EvaluationsEntity evaluationsEntity = evaluationDataEntity.evaluations;

                if (evaluationsEntity != null && evaluationsEntity.list != null && evaluationsEntity.list.size() > 0) {


                    nextQuery = evaluationsEntity.nextQuery;

                    mTarotOrAstrologyEvaluationAdapter.setData(evaluationsEntity.list);


                    tarotOrAstrologyEvaluation_xrv.setVisibility(View.VISIBLE);
                    NoData(View.GONE);




                    if (TextUtils.isEmpty(nextQuery)) {
                        mXRefreshViewFooter.setLoadcomplete(true);
                    } else {
                        mXRefreshViewFooter.setLoadcomplete(false);
                    }

                }


                tarotOrAstrologyEvaluation_xrv.stopRefresh();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mLoadingDiaog.dismiss();


                if (!isData) {
                    NoData(View.VISIBLE);
                    tarotOrAstrologyEvaluation_xrv.setVisibility(View.GONE);
                }

                mXRefreshViewHeaders.onRefreshFail();

                tarotOrAstrologyEvaluation_xrv.stopRefresh();


            }
        });
        evaluationsListService.enqueue();


    }


    private void OnLoadMoreListData(String nextQueryUrl) {
        if (TextUtils.isEmpty(nextQueryUrl)) {

            mXRefreshViewFooter.setLoadcomplete(true);
            tarotOrAstrologyEvaluation_xrv.stopLoadMore(false);


            return;
        }


        EvaluationsListMoreBean evaluationsListMoreBean = new EvaluationsListMoreBean();

        evaluationsListMoreBean.nextQuery = nextQuery;


        EvaluationsListMoreService evaluationsListMoreService = new EvaluationsListMoreService(this);

        evaluationsListMoreService.parameter(evaluationsListMoreBean);


        evaluationsListMoreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                EvaluationListRespEntity evaluationListRespEntity = (EvaluationListRespEntity) respBean;

                EvaluationDataEntity evaluationDataEntity = evaluationListRespEntity.resp;


                if (evaluationDataEntity == null) {

                    tarotOrAstrologyEvaluation_xrv.stopLoadMore(false);
                    return;
                }


                EvaluationsEntity evaluationsEntity = evaluationDataEntity.evaluations;

                if (evaluationsEntity != null && evaluationsEntity.list != null && evaluationsEntity.list.size() > 0) {

                    nextQuery = evaluationsEntity.nextQuery;
                    mTarotOrAstrologyEvaluationAdapter.addData(evaluationsEntity.list);
                    tarotOrAstrologyEvaluation_xrv.stopLoadMore();


                    if (TextUtils.isEmpty(nextQuery)) {
                        mXRefreshViewFooter.setLoadcomplete(true);

                    } else {
                        mXRefreshViewFooter.setLoadcomplete(false);
                    }
                    return;

                }


                tarotOrAstrologyEvaluation_xrv.stopLoadMore(false);


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                LogUtils.e("onFail");

                mXRefreshViewFooter.setLoadcomplete(false);
                tarotOrAstrologyEvaluation_xrv.stopLoadMore(false);


            }
        });

        evaluationsListMoreService.enqueue();

    }


    @Override
    public void onAdapterItemClick(int position, Object data) {

        int userId = (int) data;

        Bundle userbundle = new Bundle();
        userbundle.putInt(YpSettings.USERID, userId);
        ActivityUtil.jump(this, UserInfoActivity.class, userbundle, 0, 100);

    }
}
