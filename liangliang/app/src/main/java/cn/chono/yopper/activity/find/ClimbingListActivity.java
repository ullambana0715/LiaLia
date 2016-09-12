package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andbase.tractor.utils.LogUtils;
import com.andview.refreshview.XRefreshView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.ClimbList.ClimbListMoreReqBean;
import cn.chono.yopper.Service.Http.ClimbList.ClimbListMoreService;
import cn.chono.yopper.Service.Http.ClimbList.ClimbListReqBean;
import cn.chono.yopper.Service.Http.ClimbList.ClimbListRespBean;
import cn.chono.yopper.Service.Http.ClimbList.ClimbListService;
import cn.chono.yopper.Service.Http.ClimbingRank.ClimbRankReqBean;
import cn.chono.yopper.Service.Http.ClimbingRank.ClimbRankRespBean;
import cn.chono.yopper.Service.Http.ClimbingRank.ClimbRankService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.SimpleWebViewActivity;
import cn.chono.yopper.adapter.ClimbingListAdapter;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.recyclerview.XRefreshViewFooters;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.SharedprefUtil;
import cn.chono.yopper.view.CircleEnergy;

/**
 * Created by yangjinyu on 16/2/25.
 */
public class ClimbingListActivity extends MainFrameActivity implements View.OnClickListener {

    public static final int MESSAGE_CLIMBING_TIMER = 1;
    public static final int MESSAGE_CIRCLE_DECREASE = 2;

    private XRefreshView mXRefreshView;
    private ListView mClimbingListView;
    private ClimbingListAdapter mAdapter;
    private TextView mClimbingSortTv;
    private TextView mClimbingTimer;
    private TextView mClimbingInfo;
    private TextView mClimbRankingTv;
    private String mClimbingTimerText;
    private String temp;
    private int myPower = 0;
    private int mClimbingSeason;
    private String mClimbingSeasonid;
    private int mDayLeft;
    private int mHourLeft;
    private int mMinuteLeft;
    private int mSecondLeft;

    private LinearLayout no_data_layout;
    private TextView no_data_refresh_tv;

    private LinearLayout error_network_layout;
    private TextView network_error_reload_tv;

    CircleEnergy mEnergyCircle;
    private float progress = 0.01f;

    private String _nextQuery;

    private Calendar endCalendar;

    private int power_temp;


    private LinearLayout climbing_list_btnGoBack_container;

    private RelativeLayout climbing_list_btnOption_containers;

    private ImageView climbing_list_tvOption_prize_iv;

    XRefreshViewHeaders mXRefreshViewHeaders;

    XRefreshViewFooters mXRefreshViewFooters;


    private Dialog loadingDiaog;

    private List<ClimbListRespBean.ClimbListRespBeanTemp.Rank.RankItem> rankItems;
    String userId;
    Dialog dialogUtil;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CLIMBING_TIMER:
                    Calendar c = Calendar.getInstance();

                    if (endCalendar != null) {

                        mDayLeft = (endCalendar.get(Calendar.DAY_OF_YEAR) + 1) - (c.get(Calendar.DAY_OF_YEAR) + 1);
                        mHourLeft = endCalendar.getTime().getHours() - c.getTime().getHours();
                        mMinuteLeft = endCalendar.getTime().getMinutes() - c.getTime().getMinutes();
                        mSecondLeft = endCalendar.getTime().getSeconds() - c.getTime().getSeconds();

                        if (mDayLeft > 1) {
                            temp = getResources().getString(R.string.climbing_list_timer_day);
                            mClimbingTimerText = String.format(temp, mClimbingSeason, mDayLeft);
                        } else if (mHourLeft > 1) {
                            temp = getResources().getString(R.string.climbing_list_timer_hour);
                            mClimbingTimerText = String.format(temp, mClimbingSeason, mHourLeft, mMinuteLeft);
                        } else if (mMinuteLeft > 1) {
                            temp = getResources().getString(R.string.climbing_list_timer_minute);
                            mClimbingTimerText = String.format(temp, mClimbingSeason, mMinuteLeft, mSecondLeft);
                        } else if (mSecondLeft > 1) {
                            temp = getResources().getString(R.string.climbing_list_timer_second);
                            mClimbingTimerText = String.format(temp, mClimbingSeason, mSecondLeft);
                        }

                        mClimbingTimer.setText(mClimbingTimerText);

                        mHandler.sendEmptyMessageDelayed(MESSAGE_CLIMBING_TIMER, 1000);

                        if (mDayLeft <= 0 && mHourLeft <= 0 && mMinuteLeft <= 0 && mSecondLeft <= 0) {
                            mHandler.removeMessages(MESSAGE_CLIMBING_TIMER);
                        }
                    }

                    break;
                case MESSAGE_CIRCLE_DECREASE:
                    if (myPower > 0) {
                        myPower -= 1;
                        mEnergyCircle.setProgress(myPower * progress);
                        mEnergyCircle.setInnerText((int) (progress * myPower));
                        mHandler.sendEmptyMessageDelayed(MESSAGE_CIRCLE_DECREASE, 20);
                    } else {
                        mHandler.removeMessages(MESSAGE_CIRCLE_DECREASE);
                    }
                    break;
            }
        }
    };

    private void getEndTime(String timeStr) {

        if (!TextUtils.isEmpty(timeStr)) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            Date date = null;
            try {
                date = sdf.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            LogUtils.e("timeStr=" + timeStr);

            endCalendar = Calendar.getInstance();
            endCalendar.setTime(date);
            mHandler.sendEmptyMessage(MESSAGE_CLIMBING_TIMER);
        }
    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("爬榜"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("爬榜"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.climbing_list_activity);

        PushAgent.getInstance(this).onAppStart();

        RxBus.get().register(this);

        loadingDiaog = DialogUtil.LoadingDialog(this, null);

        climbing_list_btnGoBack_container = (LinearLayout) findViewById(R.id.climbing_list_btnGoBack_container);

        climbing_list_btnOption_containers = (RelativeLayout) findViewById(R.id.climbing_list_btnOption_containers);

        climbing_list_tvOption_prize_iv = (ImageView) findViewById(R.id.climbing_list_tvOption_prize_iv);

        boolean prize = SharedprefUtil.getBoolean(ClimbingListActivity.this, LoginUser.getInstance().getUserId() + "" + "prize", false);

        if (prize) {
            climbing_list_tvOption_prize_iv.setVisibility(View.VISIBLE);
        } else {
            climbing_list_tvOption_prize_iv.setVisibility(View.INVISIBLE);
        }

        mClimbingSortTv = (TextView) findViewById(R.id.climbing_sort_tv);
        mClimbingTimer = (TextView) findViewById(R.id.climbing_timer);
        mXRefreshView = (XRefreshView) findViewById(R.id.climbing_list_xrefreshview);
        mClimbingListView = (ListView) findViewById(R.id.climbing_list);
        mClimbingInfo = (TextView) findViewById(R.id.climbing_info);
        mClimbRankingTv = (TextView) findViewById(R.id.my_climbing_no);
        mEnergyCircle = (CircleEnergy) findViewById(R.id.my_energy_circle);

        error_network_layout = (LinearLayout) findViewById(R.id.error_network_layout);
        network_error_reload_tv = (TextView) findViewById(R.id.error_network_tv);
        network_error_reload_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshData();
            }
        });

        mEnergyCircle.setStatus(CircleEnergy.STATUS_WORKING);
        mEnergyCircle.setEnergyText("爬榜");
        mEnergyCircle.setEnergyTextSize(10);
        mEnergyCircle.setInnerText(0);
        mEnergyCircle.setInnerTextSize(24);
        mEnergyCircle.setInnerTextColor(Color.WHITE);
        mEnergyCircle.setPaintWidth(3);
        mEnergyCircle.setTextHeightSpace(8);
        mEnergyCircle.setStartAngleAndSweepAngle(270, 360);
        mEnergyCircle.setCircleBackgroudColor(getResources().getColor(R.color.color_333333));
        mEnergyCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (power_temp == 0) {
                    DialogUtil.showDisCoverNetToast(ClimbingListActivity.this, "当前能量不足");
                    return;
                }
                UserDto userDto = DbHelperUtils.getUserDto(LoginUser.getInstance().getUserId());

                if (((userDto.getProfile().getStatus() >> 0) & 1) == 0) {
                    BackCallListener listener = new BackCallListener() {
                        @Override
                        public void onEnsure(View view, Object... obj) {
                            Bundle bundle = new Bundle();
                            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "Standard/AvatarAudit");
                            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "头像审核规范");
                            bundle.putBoolean(YpSettings.BUNDLE_KEY_WEB_HIDE_TITLE, false);

                            ActivityUtil.jump(ClimbingListActivity.this, SimpleWebViewActivity.class, bundle, 0, 100);
                        }

                        @Override
                        public void onCancel(View view, Object... obj) {
                            dialogUtil.dismiss();
                        }
                    };
                    dialogUtil = DialogUtil.createHintOperateDialog(ClimbingListActivity.this,
                            "", "头像未通过审核", "确定", "查看帮助", listener);
                    dialogUtil.show();
                    return;
                    //头像不通过
                } else {
                    //头像通过
                }
                mHandler.sendEmptyMessage(MESSAGE_CIRCLE_DECREASE);
                onClimbRank();
            }
        });

        mClimbingInfo.setOnClickListener(this);
        initXRefreshView();
        mAdapter = new ClimbingListAdapter(this);
        mClimbingListView.setAdapter(mAdapter);

        climbing_list_btnOption_containers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedprefUtil.saveBoolean(ClimbingListActivity.this, LoginUser.getInstance().getUserId() + "" + "prize", false);

                climbing_list_tvOption_prize_iv.setVisibility(View.INVISIBLE);

                ActivityUtil.jump(ClimbingListActivity.this, MyBonusActivity.class, null, 0, 100);
            }
        });
        climbing_list_btnGoBack_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!isFinishing()) {
            loadingDiaog.show();
        }
        onRefreshData();
    }


    private void onRefreshData() {


        ClimbListService mService = new ClimbListService(this);
        final ClimbListReqBean climbListReqBean = new ClimbListReqBean();
        climbListReqBean.setStart(0);
        mService.parameter(climbListReqBean);
        mService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                loadingDiaog.dismiss();

                mXRefreshView.stopRefresh();
                ClimbListRespBean climbListRespBean = (ClimbListRespBean) respBean;
                mXRefreshView.setVisibility(View.VISIBLE);
                mClimbingListView.setVisibility(View.VISIBLE);
                findViewById(R.id.error_layout).setVisibility(View.GONE);
                myPower = climbListRespBean.getResp().getCurrentPower();
                if (TextUtils.isEmpty(climbListRespBean.getResp().getStageId())) {
                    mXRefreshView.setVisibility(View.GONE);
                    error_network_layout.setVisibility(View.GONE);
                    mClimbingSortTv.setVisibility(View.GONE);
                    mClimbRankingTv.setText("暂未上榜");
                    mClimbingInfo.setClickable(false);
                    findViewById(R.id.error_no_climblist).setVisibility(View.VISIBLE);
                    findViewById(R.id.error_layout).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.error_no_climblist)).setText("榜单暂未开放");
                    mXRefreshView.stopRefresh();
                    return;
                } else {
                    mClimbingInfo.setClickable(true);
                }

                mEnergyCircle.setInnerText(myPower);
                mEnergyCircle.setProgress(myPower * progress);
                power_temp = myPower;

                int myRank = climbListRespBean.getResp().getMyRank();

                if (myRank > 5000 || myRank == 0) {
                    mClimbingSortTv.setVisibility(View.GONE);
                    mClimbRankingTv.setText("暂未上榜");
                } else {
                    mClimbingSortTv.setVisibility(View.VISIBLE);
                    mClimbRankingTv.setText(String.valueOf(myRank));
                }

                mClimbingSeason = Integer.parseInt(climbListRespBean.getResp().getStageNumber());

                mClimbingSeasonid = climbListRespBean.getResp().getStageId();

                getEndTime(climbListRespBean.getResp().getEndTime());

                _nextQuery = climbListRespBean.getResp().getRanklists().getNextQuery();

                rankItems = climbListRespBean.getResp().getRanklists().getList();

                if (rankItems != null && rankItems.size() > 0) {
                    mAdapter.setRankItems(rankItems);

                    mAdapter.notifyDataSetChanged();
                } else {
//                    no_data_layout.setVisibility(View.VISIBLE);
                    mXRefreshView.setVisibility(View.GONE);
                    error_network_layout.setVisibility(View.GONE);
                    mClimbRankingTv.setText("暂未上榜");

                    findViewById(R.id.error_no_climblist).setVisibility(View.VISIBLE);
                    findViewById(R.id.error_layout).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.error_no_climblist)).setText("还没有人爬榜，快使用能量抢占第一吧");
                }
                mXRefreshView.stopRefresh();
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                loadingDiaog.dismiss();
                mXRefreshViewHeaders.onRefreshFail();
                mXRefreshView.stopRefresh();
                if (!TextUtils.isEmpty(respBean.getMsg())) {
                    DialogUtil.showDisCoverNetToast(ClimbingListActivity.this, respBean.getMsg());
                } else {
                    DialogUtil.showDisCoverNetToast(ClimbingListActivity.this);
                }
                if (rankItems == null) {
                    mClimbingListView.setVisibility(View.GONE);
                    findViewById(R.id.error_layout).setVisibility(View.VISIBLE);
                    return;
                }
            }
        });
        mService.enqueue();
    }

    private void onLoadMoreData(String nextQuery) {
        if (TextUtils.isEmpty(nextQuery)) {
            mXRefreshViewFooters.setLoadcomplete(true);
            mXRefreshView.setLoadComplete(false);
            DialogUtil.showDisCoverNetToast(this, "已是最后一页");
            return;
        }

        ClimbListMoreReqBean reqBean = new ClimbListMoreReqBean();
        reqBean.setNextQuery(nextQuery);

        ClimbListMoreService moreServices = new ClimbListMoreService(this);
        moreServices.parameter(reqBean);

        moreServices.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                ClimbListRespBean climbListRespBean = (ClimbListRespBean) respBean;

                if (climbListRespBean != null) {
                    mEnergyCircle.setInnerText(climbListRespBean.getResp().getCurrentPower());
                    if (climbListRespBean.getResp().getMyRank() > 5000 || climbListRespBean.getResp().getMyRank() == 0) {
                        mClimbRankingTv.setText("暂未上榜");
                    } else {
                        mClimbRankingTv.setText(String.valueOf(climbListRespBean.getResp().getMyRank()));
                    }

                    List<ClimbListRespBean.ClimbListRespBeanTemp.Rank.RankItem> list = new ArrayList<>();

                    list = climbListRespBean.getResp().getRanklists().getList();

                    _nextQuery = climbListRespBean.getResp().getRanklists().getNextQuery();

                    if (list != null && list.size() > 0) {
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                    }


                }

                if (TextUtils.isEmpty(_nextQuery)) {
                    mXRefreshViewFooters.setLoadcomplete(true);
                    mXRefreshView.stopLoadMore(false);

                } else {
                    mXRefreshViewFooters.setLoadcomplete(false);
                    mXRefreshView.stopLoadMore();
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mXRefreshViewFooters.setLoadcomplete(false);
                mXRefreshView.stopLoadMore(false);
                String str = respBean.getMsg();
                if (!TextUtils.isEmpty(str)) {
                    DialogUtil.showDisCoverNetToast(ClimbingListActivity.this, str);
                } else {
                    DialogUtil.showDisCoverNetToast(ClimbingListActivity.this);
                }

            }
        });
        moreServices.enqueue();
    }

    /**
     * 爬榜
     */
    private void onClimbRank() {
        final ClimbRankReqBean reqBean = new ClimbRankReqBean();
        MobclickAgent.onEvent(this, "btn_find_event_climb");
        ClimbRankService rankService = new ClimbRankService(this);
        rankService.parameter(reqBean);
        rankService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);
                ClimbRankRespBean resp = (ClimbRankRespBean) respBean;
                if (resp.getResp().getResult() == 1) {
                    onRefreshData();
                }
            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                mEnergyCircle.setInnerText(power_temp);
                mEnergyCircle.setProgress(power_temp * progress);
                DialogUtil.showDisCoverNetToast(ClimbingListActivity.this, respBean.getMsg());
            }
        });
        rankService.enqueue();
    }

    private void initXRefreshView() {


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        mXRefreshViewFooters = new XRefreshViewFooters(this);

        mXRefreshView.setCustomHeaderView(mXRefreshViewHeaders);

        mXRefreshView.setCustomFooterView(mXRefreshViewFooters);


        mXRefreshView.setPullLoadEnable(true);
        //如果需要在手指横向移动的时候，让XRefreshView不拦截事件
        mXRefreshView.setMoveForHorizontal(true);
        //滑动到底部自动加载更多
        mXRefreshView.setAutoLoadMore(true);


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


        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefreshData();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoadMoreData(_nextQuery);
                    }
                }, 1000);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mClimbingInfo.getId()) {
            System.out.println("OnClick");
            Bundle bundle = new Bundle();
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_URL, "/introduce/award?stageid=" + mClimbingSeasonid);
            bundle.putString(YpSettings.BUNDLE_KEY_WEB_TITLE, "爬行榜介绍");
            bundle.putInt(YpSettings.SOURCE_YTPE_KEY, 100000);
            ActivityUtil.jump(ClimbingListActivity.this, SimpleWebViewActivity.class, bundle, 0, 100);
        }
    }



    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag("prizeUpdate")

            }
    )
    public void prizeUpdate(CommonEvent event) {

        //奖品更新
        boolean prize = SharedprefUtil.getBoolean(ClimbingListActivity.this, LoginUser.getInstance().getUserId() + "" + "prize", false);

        if (prize) {
            climbing_list_tvOption_prize_iv.setVisibility(View.VISIBLE);
        }

    }

}
