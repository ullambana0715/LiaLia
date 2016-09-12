package cn.chono.yopper.activity.near;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BubblingBubbleLikes.BubblingBubbleLikesBean;
import cn.chono.yopper.Service.Http.BubblingBubbleLikes.BubblingBubbleLikesRespBean;
import cn.chono.yopper.Service.Http.BubblingBubbleLikes.BubblingBubbleLikesService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.adapter.BubblingPraiseListAdapter;
import cn.chono.yopper.adapter.BubblingPraiseListAdapter.OnItemPraiseClickLitener;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingPraiseDto;
import cn.chono.yopper.data.BubblingPraiseDto.BubblingPraise;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;

/**
 * 点赞人列表
 */
public class BubblingPraiseListActivity extends MainFrameActivity implements
        OnItemPraiseClickLitener, OnClickListener {

    private XRefreshView xrefreshView;
    private ListView praise_list;

    private BubblingPraiseListAdapter adapter;

    private String bubblingId;

    private int _start = 0;

    private int _total = 0;

    private Animation mRotateAnimation;

    private TextView error_network_tv;
    private TextView error_no_data_remove_tv;


    private LinearLayout error_network_layout;// 网络
    private LinearLayout error_no_data_remove_layout;// 已经删除
    private LinearLayout error_message_layout;// 错误

    private boolean haveFilterListData = false;


    XRefreshViewHeaders mXRefreshViewHeaders;


    XRefreshViewFooters mXRefreshViewFooters;

    private Dialog loadingDiaog;


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("点赞人列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("点赞人列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

        if (!haveFilterListData) {
            loadingDiaog = DialogUtil.LoadingDialog(this, null);
            if (!isFinishing()) {
                loadingDiaog.show();
            }
            error_network_layout.setVisibility(View.GONE);
            error_message_layout.setVisibility(View.GONE);
            error_no_data_remove_layout.setVisibility(View.GONE);

            xrefreshView.setVisibility(View.GONE);

            _start = 0;
            onRefreshData(bubblingId, _start);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.bubblingpraiselist);
        PushAgent.getInstance(this).onAppStart();
        initView();
        setXrefreshListener();
        bubblingId = getIntent().getExtras().getString(
                YpSettings.BUBBLING_LIST_ID);

    }

    /**
     * 初始化
     */
    private void initView() {

        // 设置标题栏
        this.getTvTitle().setText("点赞的人");

        this.getGoBackLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewsUtils.preventViewMultipleClick(arg0, 500);

                finish();

            }
        });

        xrefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        praise_list = (ListView) findViewById(R.id.praise_list);
        adapter = new BubblingPraiseListAdapter(this);
        adapter.setOnItemClickLitener(this);
        praise_list.setAdapter(adapter);

        error_network_tv = (TextView) findViewById(R.id.error_network_tv);
        error_no_data_remove_tv = (TextView) findViewById(R.id.error_no_data_remove_tv);


        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);
        error_no_data_remove_layout = (LinearLayout) findViewById(R.id.error_no_data_remove_layout);

        error_message_layout = (LinearLayout) findViewById(R.id.error_message_layout);

        error_network_tv.setOnClickListener(this);

    }

    private void setXrefreshListener() {


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        xrefreshView.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshViewFooters = new XRefreshViewFooters(this);



        xrefreshView.setCustomFooterView(mXRefreshViewFooters);


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

                        onLoadMoreData(bubblingId, _start);
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

                        onLoadMoreData(bubblingId, _start);
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

                        _start = 0;
                        onRefreshData(bubblingId, _start);


                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        onLoadMoreData(bubblingId, _start);

                    }
                }, 1000);
            }

        });

    }

    private void onRefreshData(String id, int start) {


        BubblingBubbleLikesBean likesBean = new BubblingBubbleLikesBean();

        likesBean.setId(id);
        likesBean.setStart(start);
        likesBean.setRows(20);

        BubblingBubbleLikesService likesService = new BubblingBubbleLikesService(this);
        likesService.parameter(likesBean);
        likesService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BubblingBubbleLikesRespBean bubbleLikesRespBean = (BubblingBubbleLikesRespBean) respBean;
                BubblingPraiseDto bubblingPraiseDto = bubbleLikesRespBean.getResp();


                // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                // 如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                // 动画回收
                loadingDiaog.dismiss();


                if (bubblingPraiseDto != null) {

                    List<BubblingPraise> list = bubblingPraiseDto.getList();
                    _start = _start + bubblingPraiseDto.getRows();
                    _total = bubblingPraiseDto.getTotal();
                    if (list != null && list.size() > 0) {

                        haveFilterListData = true;
                        error_network_layout.setVisibility(View.GONE);
                        error_message_layout.setVisibility(View.GONE);
                        error_no_data_remove_layout.setVisibility(View.GONE);
                        xrefreshView.setVisibility(View.VISIBLE);

                        adapter.setData(bubblingPraiseDto.getList());
                        adapter.notifyDataSetChanged();

                    } else {
                        haveFilterListData = false;
                        error_network_layout.setVisibility(View.VISIBLE);
                        error_message_layout.setVisibility(View.GONE);
                        error_no_data_remove_layout.setVisibility(View.GONE);
                        xrefreshView.setVisibility(View.GONE);
                    }

                } else {

                    haveFilterListData = false;
                    error_network_layout.setVisibility(View.VISIBLE);
                    error_message_layout.setVisibility(View.GONE);
                    error_no_data_remove_layout.setVisibility(View.GONE);
                    xrefreshView.setVisibility(View.GONE);
                }


                xrefreshView.stopRefresh();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                String statusCode = respBean.getStatus();

                mXRefreshViewHeaders.onRefreshFail();
                xrefreshView.stopRefresh();

                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                // 动画回收
                // 动画回收
                // 动画回收
                loadingDiaog.dismiss();

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    error_network_layout.setVisibility(View.GONE);
                    error_message_layout.setVisibility(View.GONE);
                    error_no_data_remove_layout.setVisibility(View.VISIBLE);


                    xrefreshView.setVisibility(View.GONE);

                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    error_network_layout.setVisibility(View.GONE);
                    error_message_layout.setVisibility(View.VISIBLE);
                    error_no_data_remove_layout.setVisibility(View.GONE);


                    xrefreshView.setVisibility(View.GONE);
                    return;

                }

                error_network_layout.setVisibility(View.VISIBLE);
                error_message_layout.setVisibility(View.GONE);
                error_no_data_remove_layout.setVisibility(View.GONE);


                xrefreshView.setVisibility(View.GONE);


            }
        });
        likesService.enqueue();


    }

    private void onLoadMoreData(String id, int start) {

        if (_total <= start) {

            mXRefreshViewFooters.setLoadcomplete(true);
            xrefreshView.stopLoadMore(false);
            return;

        }


        BubblingBubbleLikesBean likesBean = new BubblingBubbleLikesBean();

        likesBean.setId(id);
        likesBean.setStart(start);
        likesBean.setRows(20);

        BubblingBubbleLikesService likesService = new BubblingBubbleLikesService(this);
        likesService.parameter(likesBean);
        likesService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BubblingBubbleLikesRespBean bubbleLikesRespBean = (BubblingBubbleLikesRespBean) respBean;
                BubblingPraiseDto bubblingPraiseDto = bubbleLikesRespBean.getResp();


                // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                // 如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                // 动画回收
                loadingDiaog.dismiss();


                if (bubblingPraiseDto != null) {

                    List<BubblingPraise> list = bubblingPraiseDto.getList();

                    _start = _start + bubblingPraiseDto.getRows();
                    _total = bubblingPraiseDto.getTotal();

                    if (list != null && list.size() > 0) {
                        _start = list.size();
                        haveFilterListData = true;
                        error_network_layout.setVisibility(View.GONE);
                        error_message_layout.setVisibility(View.GONE);
                        error_no_data_remove_layout.setVisibility(View.GONE);

                        xrefreshView.setVisibility(View.VISIBLE);

                        adapter.addData(bubblingPraiseDto.getList());
                        adapter.notifyDataSetChanged();

                    } else {
                        // 没有网络的场合，去提示页
                        DialogUtil
                                .showDisCoverNetToast(BubblingPraiseListActivity.this);
                    }

                } else {

                    // 没有网络的场合，去提示页
                    DialogUtil
                            .showDisCoverNetToast(BubblingPraiseListActivity.this);
                }

                if (_start >= _total) {
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

                String statusCode = respBean.getStatus();

                mXRefreshViewFooters.setLoadcomplete(false);
                xrefreshView.stopLoadMore(false);
                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                // 动画回收
                // 动画回收
                // 动画回收
                loadingDiaog.dismiss();

                if (TextUtils.equals("404", statusCode)) {// 内容不存在了
                    error_network_layout.setVisibility(View.GONE);
                    error_message_layout.setVisibility(View.GONE);
                    error_no_data_remove_layout.setVisibility(View.VISIBLE);

                    xrefreshView.setVisibility(View.GONE);
                    return;

                }
                if (TextUtils.equals("410", statusCode)) {
                    error_network_layout.setVisibility(View.GONE);
                    error_message_layout.setVisibility(View.VISIBLE);
                    error_no_data_remove_layout.setVisibility(View.GONE);

                    xrefreshView.setVisibility(View.GONE);

                    return;

                }

                error_network_layout.setVisibility(View.VISIBLE);
                error_message_layout.setVisibility(View.GONE);
                error_no_data_remove_layout.setVisibility(View.GONE);


                xrefreshView.setVisibility(View.GONE);


            }
        });
        likesService.enqueue();


    }

    @Override
    public void onItemPraiseClick(View view, int position, int userID) {
        Bundle bundle = new Bundle();
        bundle.putInt(YpSettings.USERID, userID);
        ActivityUtil.jump(BubblingPraiseListActivity.this,
                UserInfoActivity.class, bundle, 0, 200);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_network_tv:

                loadingDiaog = DialogUtil.LoadingDialog(this, null);
                if (!isFinishing()) {
                    loadingDiaog.show();
                }
                error_network_layout.setVisibility(View.GONE);
                error_message_layout.setVisibility(View.GONE);
                error_no_data_remove_layout.setVisibility(View.GONE);

                xrefreshView.setVisibility(View.GONE);

                _start = 0;
                onRefreshData(bubblingId, _start);

                break;

            default:
                break;
        }

    }


}
