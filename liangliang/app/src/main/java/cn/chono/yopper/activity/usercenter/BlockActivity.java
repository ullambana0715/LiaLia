package cn.chono.yopper.activity.usercenter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.BlockList.BlockListBean;
import cn.chono.yopper.Service.Http.BlockList.BlockListRespBean;
import cn.chono.yopper.Service.Http.BlockList.BlockListService;
import cn.chono.yopper.Service.Http.BlockListMore.BlockListMoreBean;
import cn.chono.yopper.Service.Http.BlockListMore.BlockListMoreService;
import cn.chono.yopper.Service.Http.BlockRequest.BlockRequestBean;
import cn.chono.yopper.Service.Http.BlockRequest.BlockRequestService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.adapter.BlockListAdapter;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BlockDto;
import cn.chono.yopper.data.BlockListDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.ui.UserInfoActivity;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.MyDialog;

/**
 * 黑名单
 *
 * @author sam.sun
 */
public class BlockActivity extends MainFrameActivity {

    // 本地缓存数据
    private LayoutInflater mInflater;
    private View contextView;

    private ListView block_listview;// 地点列表

    private XRefreshView xrefreshView;

    private BlockListAdapter mAdapter;

    private TextView block_hint_tv;

    private ViewStub block_error_network_vs;
    private ViewStub block_error_no_data_vs;

    private String nextQuery;
    //haveGeting只有赋值为false的地方,没有为true的地方.
    private boolean haveGeting = false;

    private int userId;

    // 解除黑名单
    private Dialog blockDialog;

    private Dialog loadingDiaog;

    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        userId = LoginUser.getInstance().getUserId();
        initComponent();
        block_hint_tv.setVisibility(View.GONE);

        loadingDiaog = DialogUtil.LoadingDialog(BlockActivity.this, null);
        if (!isFinishing()) {
            loadingDiaog.show();
        }
        getNewBlockList(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("黑名单列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("黑名单列表"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长
    }

    /**
     * 初始化
     */
    private void initComponent() {

        // 设置标题栏
        this.getTvTitle().setText("黑名单列表");
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
        contextView = mInflater.inflate(R.layout.block_activity, null);

        block_listview = (ListView) contextView.findViewById(R.id.block_listview);

        xrefreshView = (XRefreshView) contextView.findViewById(R.id.block_xrefreshview);

        setXrefreshListener();

        mAdapter = new BlockListAdapter(this);
        block_listview.setAdapter(mAdapter);

        block_hint_tv = (TextView) contextView.findViewById(R.id.block_hint_tv);

        block_error_network_vs = (ViewStub) contextView.findViewById(R.id.block_error_network_vs);
        block_error_no_data_vs = (ViewStub) contextView.findViewById(R.id.block_error_no_data_vs);


        block_listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {
                BlockDto dto = mAdapter.getList().get(position);

                showBlockDialog(dto.getUser().getId(), position);
                return true;
            }
        });

        block_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BlockDto dto = mAdapter.getList().get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(YpSettings.USERID, dto.getUser().getId());
                ActivityUtil.jump(BlockActivity.this, UserInfoActivity.class, bundle, 0, 100);
            }
        });

        this.getMainLayout().addView(contextView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    private void handleNoDataError() {

        block_error_no_data_vs.setVisibility(View.VISIBLE);

        LinearLayout error_no_data_layout = (LinearLayout) this.findViewById(R.id.error_no_data_layout);
        error_no_data_layout.setVisibility(View.VISIBLE);

        TextView error_no_data_tv = (TextView) this.findViewById(R.id.error_no_data_tv);

        error_no_data_tv.setText("暂无拉黑名单");
    }


    private void handleNetError() {

        block_error_network_vs.setVisibility(View.VISIBLE);

        LinearLayout error_network_layout = (LinearLayout) this.findViewById(R.id.error_network_layout);
        error_network_layout.setVisibility(View.VISIBLE);

        TextView error_network_tv = (TextView) this.findViewById(R.id.error_network_tv);

        error_network_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewsUtils.preventViewMultipleClick(v, 1000);
                block_error_network_vs.setVisibility(View.GONE);
                block_error_no_data_vs.setVisibility(View.GONE);
                block_hint_tv.setVisibility(View.GONE);
                xrefreshView.setVisibility(View.GONE);
                loadingDiaog = DialogUtil.LoadingDialog(BlockActivity.this, null);
                if (!isFinishing()) {
                    loadingDiaog.show();
                }
                getNewBlockList(0);
            }
        });
    }


    public void showBlockDialog(final int id, final int position) {

        // 初始化一个自定义的Dialog
        blockDialog = new MyDialog(BlockActivity.this, R.style.MyDialog, R.layout.select_operate_dialog_layout,
                new MyDialog.DialogEventListener() {

                    @Override
                    public void onInit(View contentView) {

                        TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                        LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                        LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                        LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                        TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                        select_operate_dialog_title_tv.setText("操作");
                        select_operate_dialog_one_tv.setText("解除黑名单");

                        select_operate_dialog_one_layout.setVisibility(View.VISIBLE);

                        select_operate_dialog_two_layout.setVisibility(View.GONE);

                        select_operate_dialog_three_layout.setVisibility(View.GONE);


                        // 点击升级按钮
                        select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ViewsUtils.preventViewMultipleClick(v, 3000);

                                BlockActivity.this.blockDialog.dismiss();

                                doDeleteBlockRequest(id, position);

                            }

                        });
                    }
                });
        blockDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
        blockDialog.show();

    }

    private void setXrefreshListener() {


        mXRefreshViewHeaders=new XRefreshViewHeaders(this);

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

                        loadMoregetNewBlockList();
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

                        loadMoregetNewBlockList();
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

                        getNewBlockList(0);

                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        loadMoregetNewBlockList();

                    }
                }, 1000);
            }
        });

        xrefreshView.setOnAbsListViewScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 获取约会列表
     */

    private void getNewBlockList(final int start) {

        if (haveGeting) {
            mXRefreshViewHeaders.onRefreshFail();
            xrefreshView.stopRefresh();
            return;
        }


        BlockListBean listBean = new BlockListBean();

        listBean.setUserId(userId);
        listBean.setStart(start);

        BlockListService listService = new BlockListService(this);

        listService.parameter(listBean);

        listService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();
                BlockListRespBean listRespBean = (BlockListRespBean) respBean;
                BlockListDto blockListDto = listRespBean.getResp();

                // 定位视图隐藏 网络加载失败视图隐藏-小圆圈视图隐藏
                // 如果没有数据，则显示无数据视图，如果有数据则刷新视图
                // 动画回收
                haveGeting = false;

                block_hint_tv.setVisibility(View.VISIBLE);
                block_error_network_vs.setVisibility(View.GONE);

                if (blockListDto != null) {
                    List<BlockDto> list = blockListDto.getList();

                    if (list != null && list.size() > 0) {
                        block_error_no_data_vs.setVisibility(View.GONE);
                        xrefreshView.setVisibility(View.VISIBLE);
                        nextQuery = blockListDto.getNextQuery();

                        mAdapter.setList(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        handleNoDataError();
                        xrefreshView.setVisibility(View.GONE);
                        block_hint_tv.setVisibility(View.GONE);
                    }
                } else {
                    handleNoDataError();
                    xrefreshView.setVisibility(View.GONE);
                    block_hint_tv.setVisibility(View.GONE);
                }
                xrefreshView.stopRefresh();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                // 定位视图隐藏 无数据视图隐藏-小圆圈视图隐藏-
                // 如果没有本地缓存，则显示网络加载失败视图，如果有缓存则提示网络问题
                // 动画回收
                // 动画回收
                loadingDiaog.dismiss();

                haveGeting = false;
                mXRefreshViewHeaders.onRefreshFail();
                xrefreshView.stopRefresh();
                block_hint_tv.setVisibility(View.GONE);
                handleNetError();
                xrefreshView.setVisibility(View.GONE);
                block_error_no_data_vs.setVisibility(View.GONE);
            }
        });

        listService.enqueue();

    }

    /**
     * 获取约会列表
     */

    private void loadMoregetNewBlockList() {

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


        BlockListMoreBean listMoreBean = new BlockListMoreBean();

        listMoreBean.setNextQuery(nextQuery);

        BlockListMoreService listMoreService = new BlockListMoreService(this);

        listMoreService.parameter(listMoreBean);

        listMoreService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                BlockListRespBean listRespBean = (BlockListRespBean) respBean;
                BlockListDto blockListDto = listRespBean.getResp();


                haveGeting = false;

                if (blockListDto != null) {

                    List<BlockDto> list = blockListDto.getList();

                    if (list != null && list.size() > 0) {

                        nextQuery = blockListDto.getNextQuery();
                        List<BlockDto> curlist = mAdapter.getList();
                        curlist.addAll(list);
                        mAdapter.setList(curlist);
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
                block_error_network_vs.setVisibility(View.GONE);
                block_error_no_data_vs.setVisibility(View.GONE);
                // 提示连接失败
                DialogUtil.showDisCoverNetToast(BlockActivity.this);
            }
        });

        listMoreService.enqueue();

    }


    /**
     * 解除拉黑
     */
    private void doDeleteBlockRequest(int id, final int position) {


        BlockRequestBean requestBean = new BlockRequestBean();
        requestBean.setBlock(false);
        requestBean.setUserId(userId);
        requestBean.setId(id);

        BlockRequestService requestService = new BlockRequestService(this);
        requestService.parameter(requestBean);
        requestService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                mAdapter.getList().remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        requestService.enqueue();

    }

}
