package cn.chono.yopper.activity.usercenter;

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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.andbase.tractor.utils.DensityUtil;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.LikeHandle.LikeHandleBean;
import cn.chono.yopper.Service.Http.LikeHandle.LikeHandleService;
import cn.chono.yopper.Service.Http.LikeList.LikeListRespBean;
import cn.chono.yopper.Service.Http.LikeListMore.LikeListMoreBean;
import cn.chono.yopper.Service.Http.LikeListMore.LikeListMoreService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.activity.appointment.DatingDetailActivity;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.adapter.LikeAdapter;
import cn.chono.yopper.api.RxResultHelper;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LikeDto;
import cn.chono.yopper.data.LikeListDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.recyclerview.RecyclerItemClickListener;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.OnAdapterIconClickLitener;
import cn.chono.yopper.utils.SchedulersCompat;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 喜欢的人页面
 *
 * @author sam.sun
 */
public class LikeActivity extends MainFrameActivity implements LikeAdapter.OnLikeItemClickLitener, OnAdapterIconClickLitener {

    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView, no_data_view, neterror_view;

    private RecyclerView like_rv;// 地点列表

    private XRefreshView xrefreshView;

    private LikeAdapter mAdapter;

    private TextView like_hint_tv;

    private String nextQuery;
    //haveGeting只有赋值为false的地方,没有为true的地方.
    private boolean haveGeting = false;

    private int userId;

    // 解除喜欢
    private Dialog blockDialog;

    private Dialog loadingDiaog;

    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooters;


    private int W, H;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        userId = LoginUser.getInstance().getUserId();
        initComponent();
        like_hint_tv.setVisibility(View.GONE);

        loadingDiaog = DialogUtil.LoadingDialog(LikeActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }
//        getLikePeopleList(0);

        getLikeList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("喜欢的人"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("喜欢的人"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("喜欢的人");
        this.getBtnGoBack().setVisibility(View.VISIBLE);
        this.getOptionLayout().setVisibility(View.INVISIBLE);

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);
                finish();

            }
        });

        // 内容部分
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = mInflater.inflate(R.layout.act_like, null);

        like_rv = (RecyclerView) contextView.findViewById(R.id.like_rv);

        xrefreshView = (XRefreshView) contextView.findViewById(R.id.like_xrefreshview);

        like_rv.setLayoutManager(new LinearLayoutManager(this));

        like_rv.setItemAnimator(new DefaultItemAnimator());

        setXrefreshListener();

        mAdapter = new LikeAdapter(this);

        mAdapter.setOnAdapterIconClickLitener(this);
        mAdapter.setOnLikeItemClickLitener(this);

        like_rv.setAdapter(mAdapter);

        like_hint_tv = (TextView) contextView.findViewById(R.id.like_hint_tv);


        like_rv.addOnItemTouchListener(new RecyclerItemClickListener(LikeActivity.this, like_rv, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (mAdapter.getDatas() != null && mAdapter.getDatas().size() > 0) {
                    LikeDto dto = (LikeDto) mAdapter.getDatas().get(position);
                    showBlockDialog(dto.getUserInfo().getId(), position);
                }
            }
        }));

        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        Display mDisplay = this.getWindowManager().getDefaultDisplay();
        W = mDisplay.getWidth();
        H = mDisplay.getHeight();
        H = H - DensityUtil.dip2px(this, 45);
    }

    private void handleNoDataError() {

        no_data_view = mInflater.inflate(R.layout.error_no_like_layout, null);

        LinearLayout error_no_like_data_layout = (LinearLayout) no_data_view.findViewById(R.id.error_no_like_data_layout);

        LayoutParams lpm = new LayoutParams(W, H);

        error_no_like_data_layout.setLayoutParams(lpm);

    }


    private void handleNetError() {


        neterror_view = mInflater.inflate(R.layout.error_network_layout, null);

        LinearLayout error_network_layout = (LinearLayout) neterror_view.findViewById(R.id.error_network_layout);

        LayoutParams lpm = new LayoutParams(W, H);

        error_network_layout.setLayoutParams(lpm);

        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) neterror_view.findViewById(R.id.error_network_tv);

        TextView error_network_tv1 = (TextView) neterror_view.findViewById(R.id.error_network_tv1);
        TextView error_network_tv2 = (TextView) neterror_view.findViewById(R.id.error_network_tv2);


        error_network_tv1.setText("数据加载失败了");
        error_network_tv2.setText("请下拉重新刷新");


        error_network_tv.setVisibility(View.GONE);
    }


    public void showBlockDialog(final int id, final int position) {

        // 初始化一个自定义的Dialog
        blockDialog = new MyDialog(LikeActivity.this, R.style.MyDialog, R.layout.select_operate_dialog_layout,
                new MyDialog.DialogEventListener() {

                    @Override
                    public void onInit(View contentView) {

                        TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                        LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                        LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                        LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                        TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                        select_operate_dialog_title_tv.setText("操作");
                        select_operate_dialog_one_tv.setText("取消喜欢");

                        select_operate_dialog_one_layout.setVisibility(View.VISIBLE);

                        select_operate_dialog_two_layout.setVisibility(View.GONE);

                        select_operate_dialog_three_layout.setVisibility(View.GONE);


                        // 点击升级按钮
                        select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ViewsUtils.preventViewMultipleClick(v, 3000);

                                LikeActivity.this.blockDialog.dismiss();

                                doCancelLike(id, false, position);

                            }

                        });
                    }
                });
        blockDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
        blockDialog.show();

    }

    private void setXrefreshListener() {


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);


        mXRefreshViewFooters = new XRefreshViewFooters(this);

        mXRefreshViewFooters.setRecyclerView(like_rv);

        xrefreshView.setCustomFooterView(mXRefreshViewFooters);


        xrefreshView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        xrefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        xrefreshView.setAutoLoadMore(true);


        mXRefreshViewFooters.callWhenNotAutoLoadMore(new SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadMoreLikePeopleList();
                    }
                }, 1000);

            }
        });


        mXRefreshViewFooters.onAutoLoadMoreFail(new SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadMoreLikePeopleList();
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

                        getLikeList();

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        loadMoreLikePeopleList();

                    }
                }, 1000);
            }
        });

    }



    protected CompositeSubscription mCompositeSubscription;

    private void getLikeList() {


        if (haveGeting) {
            mXRefreshViewHeaders.onRefreshFail();
            xrefreshView.stopRefresh();
            return;
        }

        mAdapter.setHeaderView(null, like_rv);

        Subscription subscription = App.getInstance().mHttpApi.getLike()
                .compose(SchedulersCompat.applyIoSchedulers())
                .compose(RxResultHelper.handleResult())
                .subscribe(likedto -> {

                    loadingDiaog.dismiss();

                    // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                    // 如果没有数据，则显示无数据视图，如果有数据则刷新视图
                    // 动画回收
                    haveGeting = false;

                    like_hint_tv.setVisibility(View.VISIBLE);

                    if (likedto != null) {
                        List<LikeDto> list = likedto.getList();

                        if (list != null && list.size() > 0) {

                            nextQuery = likedto.getNextQuery();

                            mAdapter.setData(list);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            handleNoDataError();
                            if (no_data_view != null) {
                                mAdapter.setHeaderView(no_data_view, like_rv);
                                mAdapter.notifyDataSetChanged();
                            }
                            like_hint_tv.setVisibility(View.GONE);
                        }
                    } else {
                        handleNoDataError();
                        if (no_data_view != null) {
                            mAdapter.setHeaderView(no_data_view, like_rv);
                            mAdapter.notifyDataSetChanged();
                        }
                        like_hint_tv.setVisibility(View.GONE);
                    }
                    xrefreshView.stopRefresh();

                }, throwable -> {

                    if(throwable==null){

                        return;
                    }

                    loadingDiaog.dismiss();

                    haveGeting = false;
                    mXRefreshViewHeaders.onRefreshFail();
                    xrefreshView.stopRefresh();


                    if (mAdapter.getDatas() != null && mAdapter.getDatas().size() > 0) {

                        DialogUtil.showDisCoverNetToast(LikeActivity.this);

                    } else {
                        handleNetError();
                        if (neterror_view != null) {
                            mAdapter.setHeaderView(neterror_view, like_rv);
                            mAdapter.notifyDataSetChanged();
                        }
                        like_hint_tv.setVisibility(View.GONE);
                    }

                });

        addSubscrebe(subscription);

    }




    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }



    /**
     * 加载更多喜欢人数据
     */

    private void loadMoreLikePeopleList() {

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


        LikeListMoreBean listMoreBean = new LikeListMoreBean();

        listMoreBean.setNextQuery(nextQuery);

        LikeListMoreService listMoreService = new LikeListMoreService(this);

        listMoreService.parameter(listMoreBean);

        listMoreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                LikeListRespBean listRespBean = (LikeListRespBean) respBean;
                LikeListDto likeListDto = listRespBean.getResp();


                haveGeting = false;

                if (likeListDto != null) {

                    List<LikeDto> list = likeListDto.getList();

                    if (list != null && list.size() > 0) {

                        nextQuery = likeListDto.getNextQuery();
                        List<LikeDto> curlist = mAdapter.getDatas();
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

                // 刷新完成必须调用此方法停止加载
                mXRefreshViewFooters.setLoadcomplete(false);
                xrefreshView.stopLoadMore(false);

                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                haveGeting = false;
                // 提示连接失败
                DialogUtil.showDisCoverNetToast(LikeActivity.this);
            }
        });

        listMoreService.enqueue();

    }


    /**
     * 取消喜欢
     */
    private void doCancelLike(int id, boolean islike, final int position) {

        loadingDiaog = DialogUtil.LoadingDialog(LikeActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }

        LikeHandleBean requestBean = new LikeHandleBean();
        requestBean.setIslike(islike);
        requestBean.setUserId(id);

        LikeHandleService requestService = new LikeHandleService(this);
        requestService.parameter(requestBean);
        requestService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                loadingDiaog.dismiss();
                mAdapter.getDatas().remove(position);
                if (null != mAdapter.getDatas() && mAdapter.getDatas().size() > 0) {

                } else {
                    handleNoDataError();
                    if (no_data_view != null) {
                        mAdapter.setHeaderView(no_data_view, like_rv);
                    }
                    like_hint_tv.setVisibility(View.GONE);
                }

                mAdapter.notifyDataSetChanged();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();
                String msg = respBean.getMsg();
                if (TextUtils.isEmpty(msg)) {
                    DialogUtil.showDisCoverNetToast(LikeActivity.this);
                } else {
                    DialogUtil.showDisCoverNetToast(LikeActivity.this, msg);
                }
            }
        });
        requestService.enqueue();

    }

    @Override
    public void onAdapterIconClick(int position, Object data) {

        LikeDto dto = (LikeDto) data;
        Bundle bundle = new Bundle();
        bundle.putInt(YpSettings.USERID, dto.getUserInfo().getId());
        ActivityUtil.jump(LikeActivity.this, UserInfoActivity.class, bundle, 0, 100);
    }

    @Override
    public void OnTakePartEvent(int position, String actionId) {

        if (CheckUtil.isEmpty(actionId)) {
            return;
        }
        //去活动详情

        Bundle bundle = new Bundle();
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "活动详情");
        bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "activity/dtails?activityId=" + actionId);
        bundle.putString(YpSettings.ACTIVITY_ID, actionId);

        bundle.putInt(YpSettings.SOURCE_YTPE_KEY, 600);

        ActivityUtil.jump(this, SimpleWebViewActivity.class, bundle, 0, 100);

    }

    @Override
    public void OnDatingsEvent(int position, int userId, String datingId) {
        //去邀约详情
        if (CheckUtil.isEmpty(datingId)) {
            return;
        }
        Bundle bundle = new Bundle();

        bundle.putInt(YpSettings.USERID, userId);

        bundle.putString(YpSettings.DATINGS_ID, datingId);

        ActivityUtil.jump(LikeActivity.this, DatingDetailActivity.class, bundle, 0, 100);
    }
}
