package cn.chono.yopper.activity.find;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andbase.tractor.utils.DensityUtil;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.CounselorList.CounselorsBean;
import cn.chono.yopper.Service.Http.CounselorList.CounselorsEntity;
import cn.chono.yopper.Service.Http.CounselorList.CounselorsList;
import cn.chono.yopper.Service.Http.CounselorList.CounselorsListService;
import cn.chono.yopper.Service.Http.CounselorList.CounselorsRespEntity;
import cn.chono.yopper.Service.Http.CounselorsListMore.CounselorsListMoreBean;
import cn.chono.yopper.Service.Http.CounselorsListMore.CounselorsListMoreService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.adapter.TarotOrAstrologyListAdapter;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 塔罗占星列表
 */
public class TarotOrAstrologysListActivity extends MainFrameActivity implements OnAdapterItemClickLitener {

    // 本地缓存数据
    private LayoutInflater mInflater;
    private View no_data_view, neterror_view;


    TarotOrAstrologyListAdapter mTarotOrAstrologyListAdapter;


    private RecyclerView tarotOrAstrology_rv;

    private XRefreshView tarotOrAstrology_xrv;




    private String nextQuery;

    private Dialog mLoadingDiaog;

    XRefreshViewFooters mXRefreshViewFooter;

    int mCounselorType;

    ImageView tarotOrAstrology_iv;

    XRefreshViewHeaders mXRefreshViewHeaders;

    private int W,H;

    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("塔罗占星列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("塔罗占星列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_tarot_astrologys_list);


        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Display mDisplay = this.getWindowManager().getDefaultDisplay();
        W = mDisplay.getWidth();
        H = mDisplay.getHeight();
        H=H - DensityUtil.dip2px(this, 45);


        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(YpSettings.COUNSEL_TYPE))
            mCounselorType = bundle.getInt(YpSettings.COUNSEL_TYPE);


        initView();
        setXrefreshListener();
        initData();



        mLoadingDiaog.show();
        OnRefreshListData(mCounselorType);


    }


    private void handleNoDataError() {

        no_data_view = mInflater.inflate(R.layout.error_no_like_layout, null);

        LinearLayout error_no_like_data_layout = (LinearLayout) no_data_view.findViewById(R.id.error_no_like_data_layout);

        LinearLayout.LayoutParams lpm= new LinearLayout.LayoutParams(W,H);

        error_no_like_data_layout.setLayoutParams(lpm);

    }


    private void handleNetError() {


        neterror_view = mInflater.inflate(R.layout.error_network_layout, null);

        LinearLayout error_network_layout = (LinearLayout) neterror_view.findViewById(R.id.error_network_layout);

        LinearLayout.LayoutParams lpm= new LinearLayout.LayoutParams(W,H);

        error_network_layout.setLayoutParams(lpm);

        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) neterror_view.findViewById(R.id.error_network_tv);

        TextView error_network_tv1 = (TextView) neterror_view.findViewById(R.id.error_network_tv1);
        TextView error_network_tv2 = (TextView) neterror_view.findViewById(R.id.error_network_tv2);


        error_network_tv1.setText("数据加载失败了");
        error_network_tv2.setText("请下拉重新刷新");


        error_network_tv.setVisibility(View.GONE);
    }

    private void initView() {

        // 设置标题栏
        this.getTvTitle().setText("我的订单");

        this.getOptionLayout().setVisibility(View.INVISIBLE);
        this.getBtnGoBack().setVisibility(View.VISIBLE);

        mLoadingDiaog = DialogUtil.LoadingDialog(this);





        tarotOrAstrology_iv= (ImageView) findViewById(R.id.tarotOrAstrology_iv);

        tarotOrAstrology_xrv = (XRefreshView) findViewById(R.id.tarotOrAstrology_xrv);

        tarotOrAstrology_rv = (RecyclerView) findViewById(R.id.tarotOrAstrology_rv);

        tarotOrAstrology_rv.setLayoutManager(new LinearLayoutManager(this));

        tarotOrAstrology_rv.setItemAnimator(new DefaultItemAnimator());

        mTarotOrAstrologyListAdapter = new TarotOrAstrologyListAdapter(this, mCounselorType);

        tarotOrAstrology_rv.setAdapter(mTarotOrAstrologyListAdapter);

        mTarotOrAstrologyListAdapter.setOnAdapterItemClickLitener(this);


        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();
            }
        });

        tarotOrAstrology_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCounselorType == Constant.CounselorType_Tarot) {

                    MobclickAgent.onEvent(TarotOrAstrologysListActivity.this, "btn_Tlist_event_Tarotcards");
                    Bundle bundle = new Bundle();

                    bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "tarot");

                    bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "塔罗占卜");

                    ActivityUtil.jump(TarotOrAstrologysListActivity.this, TarotWebActivity.class, bundle, 0, 100);


                } else if (mCounselorType == Constant.CounselorType_Astrology) {

                    MobclickAgent.onEvent(TarotOrAstrologysListActivity.this, "btn_Alist_event_Astrolabechart");

                    ActivityUtil.jump(TarotOrAstrologysListActivity.this, AstrolabeActivity.class, null, 0, 100);

                } else if (mCounselorType == Constant.CounselorType_Psychological) {//心理咨询师

                }
            }
        });


    }

    private void initData() {


        if (mCounselorType == Constant.CounselorType_Tarot) {
            getTvTitle().setText("塔罗师");
            Glide.with(this).load(R.drawable.tarot_icon).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(tarotOrAstrology_iv);
        } else if (mCounselorType == Constant.CounselorType_Astrology) {
            getTvTitle().setText("占星师");
            Glide.with(this).load(R.drawable.astrology_icon).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(tarotOrAstrology_iv);
        } else if (mCounselorType == Constant.CounselorType_Psychological) {//心理咨询师

        }


    }


    private void setXrefreshListener() {

        mXRefreshViewHeaders=new XRefreshViewHeaders(this);

        tarotOrAstrology_xrv.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshViewFooter = new XRefreshViewFooters(this);

        mXRefreshViewFooter.setRecyclerView(tarotOrAstrology_rv);


        mTarotOrAstrologyListAdapter.setCustomLoadMoreView(mXRefreshViewFooter);

        tarotOrAstrology_xrv.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        tarotOrAstrology_xrv.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        tarotOrAstrology_xrv.setAutoLoadMore(true);


        mXRefreshViewFooter.callWhenNotAutoLoadMore(new SimpleXRefreshListener() {
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


        tarotOrAstrology_xrv.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        OnRefreshListData(mCounselorType);


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




    private void OnRefreshListData(int counselorType) {

        mTarotOrAstrologyListAdapter.setHeaderView(null, tarotOrAstrology_rv);

        CounselorsBean counselorsBean = new CounselorsBean();

        counselorsBean.counselorType = counselorType;


        CounselorsListService counselorsListService = new CounselorsListService(this);


        counselorsListService.parameter(counselorsBean);


        counselorsListService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                mLoadingDiaog.dismiss();

                CounselorsRespEntity counselorsRespEntity = (CounselorsRespEntity) respBean;

                CounselorsEntity counselorsEntity = counselorsRespEntity.resp;



                if (null != counselorsEntity) {

                    nextQuery = counselorsEntity.nextQuery;

                    if (null != counselorsEntity.list && counselorsEntity.list.size() > 0) {

                        mTarotOrAstrologyListAdapter.setData(counselorsEntity.list);




                    } else {

                        handleNoDataError();
                        if (no_data_view != null) {
                            mTarotOrAstrologyListAdapter.setHeaderView(no_data_view, tarotOrAstrology_rv);
                            mTarotOrAstrologyListAdapter.notifyDataSetChanged();
                        }



                    }
                }else {

                    handleNoDataError();
                    if (no_data_view != null) {
                        mTarotOrAstrologyListAdapter.setHeaderView(no_data_view, tarotOrAstrology_rv);
                        mTarotOrAstrologyListAdapter.notifyDataSetChanged();
                    }


                }

                if (TextUtils.isEmpty(nextQuery)) {
                    mXRefreshViewFooter.setLoadcomplete(true);
                } else {
                    mXRefreshViewFooter.setLoadcomplete(false);
                }

                tarotOrAstrology_xrv.stopRefresh();


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mLoadingDiaog.dismiss();
                mXRefreshViewHeaders.onRefreshFail();

                tarotOrAstrology_xrv.stopRefresh();

                List<CounselorsList> list = mTarotOrAstrologyListAdapter.getData();

                if (null == list || list.size() == 0) {
                    handleNetError();
                    if (neterror_view != null) {
                        mTarotOrAstrologyListAdapter.setHeaderView(neterror_view, tarotOrAstrology_rv);
                        mTarotOrAstrologyListAdapter.notifyDataSetChanged();
                    }
                    return;
                }

                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(TarotOrAstrologysListActivity.this);
                    return;
                }
                DialogUtil.showDisCoverNetToast(TarotOrAstrologysListActivity.this, msg);

            }
        });
        counselorsListService.enqueue();


    }

    private void OnLoadMoreListData(String nextQueryUrl) {
        if (TextUtils.isEmpty(nextQueryUrl)) {

            mXRefreshViewFooter.setLoadcomplete(true);
            tarotOrAstrology_xrv.stopLoadMore(false);


            return;
        }


        CounselorsListMoreBean counselorsListMoreBean = new CounselorsListMoreBean();

        counselorsListMoreBean.nextQuery = nextQuery;

        CounselorsListMoreService counselorsListMoreService = new CounselorsListMoreService(this);

        counselorsListMoreService.parameter(counselorsListMoreBean);


        counselorsListMoreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                CounselorsRespEntity counselorsRespEntity = (CounselorsRespEntity) respBean;

                CounselorsEntity counselorsEntity = counselorsRespEntity.resp;

                if (null != counselorsEntity && null != counselorsEntity.list && counselorsEntity.list.size() > 0) {

                    nextQuery = counselorsEntity.nextQuery;
                    mTarotOrAstrologyListAdapter.addData(counselorsEntity.list);
                    mTarotOrAstrologyListAdapter.notifyDataSetChanged();





                }



                if (TextUtils.isEmpty(nextQuery)) {
                    mXRefreshViewFooter.setLoadcomplete(true);
                    tarotOrAstrology_xrv.stopLoadMore(false);

                } else {
                    mXRefreshViewFooter.setLoadcomplete(false);

                    tarotOrAstrology_xrv.stopLoadMore();
                }






            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                LogUtils.e("onFail");

                mXRefreshViewFooter.setLoadcomplete(false);
                tarotOrAstrology_xrv.stopLoadMore(false);


            }
        });

        counselorsListMoreService.enqueue();

    }


    @Override
    public void onAdapterItemClick(int position, Object data) {

        int userId = (int) data;

        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, userId);
        bundle.putInt(YpSettings.COUNSEL_TYPE, mCounselorType);


        ActivityUtil.jump(TarotOrAstrologysListActivity.this, TarotOrAstrologyDetailActivity.class, bundle, 0, 100);

    }


}
