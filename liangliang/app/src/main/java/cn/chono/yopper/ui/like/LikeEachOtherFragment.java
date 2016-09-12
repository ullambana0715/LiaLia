package cn.chono.yopper.ui.like;

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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andbase.tractor.utils.DensityUtil;
import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.adapter.ILikeAdapter;
import cn.chono.yopper.base.BaseFragment;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.entity.likeBean.ILike;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.model.like.ILikeContract;
import cn.chono.yopper.model.like.ILikePresenter;
import cn.chono.yopper.recyclerview.RecyclerItemClickListener;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.utils.ContextUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SharedprefUtil;

/**
 * Created by jianghua on 2016/7/15.
 */
public class LikeEachOtherFragment extends BaseFragment<ILikePresenter> implements ILikeContract.ILikeView {

    @BindView(R.id.ilike_rv)
    RecyclerView ilikeRv;
    @BindView(R.id.ilike_xrefreshview)
    XRefreshView ilikeXrefreshview;
    @BindView(R.id.remind_tv)
    TextView remindTv;

    private LayoutInflater mInflater;
    private View no_data_view, neterror_view;

    private ILikeAdapter mLikeEachAdapter;
    private XRefreshViewFooters mXRefreshViewFooters;
    private XRefreshViewHeaders mXRefreshViewHeaders;

    private int W, H;
    private boolean isRefresh = false;

    private int position = 0;

    @Override
    protected ILikePresenter getPresenter() {
        return new ILikePresenter(mContext, this);
    }

    @Override
    protected int getLayoutId() {
        //加入fragment布局的layout
        return R.layout.frm_ilike_layout;
    }

    @Override
    protected void initView() {
        RxBus.get().register(this);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        no_data_view = mInflater.inflate(R.layout.error_no_like_layout, null);
        neterror_view = mInflater.inflate(R.layout.error_network_layout, null);
        remindTv.setText("互相喜欢的人聊天不受HOT限制");
        remindTv.setVisibility(View.VISIBLE);

        ilikeRv.setItemAnimator(new DefaultItemAnimator());
        ilikeRv.setLayoutManager(new LinearLayoutManager(mContext));

        mLikeEachAdapter = new ILikeAdapter(mContext, YpSettings.LIKE_TYPE_LIKEEACH);
        ilikeRv.setAdapter(mLikeEachAdapter);

        setXrefreshViewListener();

        Display mDisplay = mContext.getWindowManager().getDefaultDisplay();
        W = mDisplay.getWidth();
        H = mDisplay.getHeight();
        H = H - DensityUtil.dip2px(mContext, 45);
    }

    @Override
    protected void initDataAndLoadData() {

    }

    @Override
    public void setData(List<ILike> data) {

        mLikeEachAdapter.setHeaderView(null,ilikeRv);

        Logger.e("刷新数据咯 啊哈哈  likeEach");

        isRefresh = true;
        mLikeEachAdapter.setData(data);
    }

    @Override
    public void addData(List<ILike> data) {
        mLikeEachAdapter.addData(data);
        isRefresh = true;
    }

    @Override
    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            DialogUtil.showDisCoverNetToast(mContext);
            return;
        }
        DialogUtil.showDisCoverNetToast(mContext, msg);
    }

    @Override
    public void handNetError() {
        mXRefreshViewHeaders.onRefreshFail();
        ilikeXrefreshview.stopRefresh();
        handleNetError();
        if (neterror_view != null) {

            ilikeRv.removeAllViews();

            ilikeRv.setAdapter(mLikeEachAdapter);

            mLikeEachAdapter.setHeaderView(null,ilikeRv);

            mLikeEachAdapter.setHeaderView(neterror_view, ilikeRv);

            mLikeEachAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handNoDataError() {

        RxBus.get().post("cancelAttion", 2);

        handleNoDataError();
        if (no_data_view != null) {


            ilikeRv.removeAllViews();

            ilikeRv.setAdapter(mLikeEachAdapter);


            mLikeEachAdapter.setHeaderView(null,ilikeRv);

            mLikeEachAdapter.setHeaderView(no_data_view, ilikeRv);

            mLikeEachAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefreshFinish() {

        SharedprefUtil.saveBoolean(ContextUtil.getContext(), LoginUser.getInstance().getUserId() + "likeEachOther", false);

        RxBus.get().post("likeEachOther", new CommonEvent());


        ilikeXrefreshview.stopRefresh();
    }

    @Override
    public void onRefreshError(String msg) {

        mXRefreshViewHeaders.onRefreshFail();

        ilikeXrefreshview.stopRefresh();

        if (TextUtils.isEmpty(msg)) {

            DialogUtil.showDisCoverNetToast(mContext);

        } else {

            DialogUtil.showDisCoverNetToast(mContext, msg);

        }
    }

    @Override
    public void canLoadMore(boolean flag) {
        Logger.e("是否可以加载更多：：：" + flag);
        if (flag) {
            mXRefreshViewFooters.setLoadcomplete(false);
            ilikeXrefreshview.stopLoadMore();
        } else {
            mXRefreshViewFooters.setLoadcomplete(true);
            ilikeXrefreshview.stopLoadMore(false);
        }
    }

    @Override
    public void onLoadMoreError(String msg) {
        mXRefreshViewFooters.setLoadcomplete(false);
        ilikeXrefreshview.stopLoadMore(false);
        if (TextUtils.isEmpty(msg)) {
            DialogUtil.showDisCoverNetToast(mContext);
        } else {
            DialogUtil.showDisCoverNetToast(mContext, msg);
        }
    }

    @Override
    public void onGoneRemind() {
//        if (mLikeEachAdapter.getData().size() != 0) {
            no_data_view.setVisibility(View.GONE);
            neterror_view.setVisibility(View.GONE);
//        }
    }

    //头像点击
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("fragment_type_2")
            }
    )
    public void intData(Object type) {
        //处理item点击事件
        if ((int) type == 2) {
            if (null == mLikeEachAdapter.getData() || mLikeEachAdapter.getData().size() == 0) {
                ilikeXrefreshview.startRefresh();
            }
        }
    }

    //头像点击
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachHeadClick")
            }
    )
    public void onHeadClick(ILike.UserInfoBean il) {
        Logger.e("LikeEachOtherFragment onHeadClick");
        //处理item点击事件
        mPresenter.onAdapterIconClick(il,YpSettings.LIKE_TYPE_LIKEEACH);
    }

    //活动
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachActClick")
            }
    )
    public void onActClick(String actId) {
        mPresenter.OnTakePartEvent(actId);
    }

    //约会
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachDatingClick")
            }
    )
    public void onDatingClick(ILike il) {
        Logger.e("LikeEachFragment  onDatingClick == " + il.toString());
        mPresenter.OnDatingsEvent(il.getUserInfo().getId(), il.getDating().getDatingId());
    }

    //长按
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachlongClick")
            }
    )
    public void onItemLongClick(Integer position) {
        int userid = mLikeEachAdapter.getData().get(position).getUserInfo().getId();
        mPresenter.onItemLongClick(userid, position, YpSettings.LIKE_TYPE_LIKEEACH);
    }

    //取消喜欢
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachcancelSuccess")
            }
    )
    public void onCancelSuccess(Object position) {
        mLikeEachAdapter.getData().remove((int)position);
        mLikeEachAdapter.notifyDataSetChanged();
    }

    //解锁
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachunlock")
            }
    )
    public void onUnLock(CommonEvent commonEvent) {
        Logger.e("解锁互相喜欢的人 onUnLock " + (int) commonEvent.getEvent());
        position = commonEvent.getPostion();
        mPresenter.onUnLockUser((int) commonEvent.getEvent(), YpSettings.LIKE_TYPE_LIKEEACH);
    }

    //解锁成功
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("likeEachlockSuccess")
            }
    )
    public void lockSuccess(Object type) {
        //处理item点击事件
        if ((boolean) type) {
            mLikeEachAdapter.getData().get(position).setIsUnlock(true);
            mLikeEachAdapter.notifyDataSetChanged();
        }
    }


    //好友关系变动
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("LikeEachEvent")
            }
    )
    public void LikeEachEvent(Object type) {

        isRefresh = false;

        ilikeXrefreshview.setLoadComplete(false);

        mPresenter.getData(YpSettings.LIKE_TYPE_LIKEEACH, isRefresh);

    }


    private void setXrefreshViewListener() {

        ilikeRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //newState 目前滚动状态 0：未滚动
                if (newState == 0) {
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        ilikeRv.setHasFixedSize(true);

        mXRefreshViewFooters = new XRefreshViewFooters(mContext);

        mXRefreshViewFooters.setRecyclerView(ilikeRv);

        mXRefreshViewHeaders = new XRefreshViewHeaders(mContext);

        mLikeEachAdapter.setCustomLoadMoreView(mXRefreshViewFooters);

        ilikeXrefreshview.setPullLoadEnable(true);

        ilikeXrefreshview.setCustomHeaderView(mXRefreshViewHeaders);

        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        ilikeXrefreshview.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        ilikeXrefreshview.setAutoLoadMore(true);

        mXRefreshViewFooters.callWhenNotAutoLoadMore(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.getMoreData(YpSettings.LIKE_TYPE_LIKEEACH);
                    }
                }, 1000);

            }
        });

        mXRefreshViewFooters.onAutoLoadMoreFail(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPresenter.getMoreData(YpSettings.LIKE_TYPE_LIKEEACH);
                    }
                }, 1000);

            }
        });

        ilikeXrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mLikeEachAdapter.setHeaderView(null, ilikeRv);
                        mPresenter.getData(YpSettings.LIKE_TYPE_LIKEEACH, isRefresh);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPresenter.getMoreData(YpSettings.LIKE_TYPE_LIKEEACH);
                    }
                }, 1000);

            }

        });

        ilikeRv.addOnItemTouchListener(new RecyclerItemClickListener(mContext, ilikeRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (mLikeEachAdapter.getData() != null && mLikeEachAdapter.getData().size() > 0) {
                    ILike iLike = mLikeEachAdapter.getData().get(position);
                    mPresenter.onItemLongClick(iLike.getUserInfo().getId(), position, YpSettings.LIKE_TYPE_LIKEEACH);
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        mPresenter.detachView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void handleNoDataError() {

        LinearLayout error_no_like_data_layout = (LinearLayout) no_data_view.findViewById(R.id.error_no_like_data_layout);

        LinearLayout.LayoutParams lpm = new LinearLayout.LayoutParams(W, H);

        error_no_like_data_layout.setLayoutParams(lpm);

        TextView remind = (TextView) error_no_like_data_layout.findViewById(R.id.remind_like_tv);

        remind.setText("还没有互相喜欢的人");
    }

    private void handleNetError() {

        LinearLayout error_network_layout = (LinearLayout) neterror_view.findViewById(R.id.error_network_layout);

        LinearLayout.LayoutParams lpm = new LinearLayout.LayoutParams(W, H);

        error_network_layout.setLayoutParams(lpm);

        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) neterror_view.findViewById(R.id.error_network_tv);

        TextView error_network_tv1 = (TextView) neterror_view.findViewById(R.id.error_network_tv1);
        TextView error_network_tv2 = (TextView) neterror_view.findViewById(R.id.error_network_tv2);

        error_network_tv1.setText("数据加载失败了");
        error_network_tv2.setText("请下拉重新刷新");

        error_network_tv.setVisibility(View.GONE);
    }

}
