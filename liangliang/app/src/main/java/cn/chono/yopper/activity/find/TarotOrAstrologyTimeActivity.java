package cn.chono.yopper.activity.find;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.OrderCounsel.OrderCounselBean;
import cn.chono.yopper.Service.Http.OrderCounsel.OrderCounselEntity;
import cn.chono.yopper.Service.Http.OrderCounsel.OrderCounselRespEntity;
import cn.chono.yopper.Service.Http.OrderCounsel.OrderCounselService;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDateEntity;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDatetimesBean;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkDatetimesEntity;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkTimesEntity;
import cn.chono.yopper.Service.Http.WorkDatetimes.WorkTimesService;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.order.UserOrderListActivity;
import cn.chono.yopper.activity.order.UserOrderPayActivity;
import cn.chono.yopper.adapter.TarotAstrologyTimeAdapter;
import cn.chono.yopper.adapter.TarotAstrologyTimeTabAdapter;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.TimeUtils;
import cn.chono.yopper.recyclerview.XRefreshViewHeaders;

/**
 * 选择预约时间
 * Created by cc on 16/4/28.
 */
public class TarotOrAstrologyTimeActivity extends MainFrameActivity implements TarotAstrologyTimeAdapter.OnTimeItemClickLitener {


    TarotAstrologyTimeTabAdapter mTarotAstrologyTimeTabAdapter;

    TarotAstrologyTimeAdapter mTarotAstrologyTimeAdapter;

    XRefreshView tarotAstrologyTime_xrv_time;

    RecyclerView tarotAstrologyTime_rv;

    LayoutInflater mLayoutInflater;


    List<Fragment> mFragmentList;


    int userId;

    int mCounselorType;

    WorkDateEntity workDateEntity;

    Dialog reservation_dlg;


    boolean isTimeItemClick;

    Dialog mPay_dlg, mDealWith_dlg, mComplete_dlg;


    String mUserNmae;

    long mChare;

    XRefreshViewHeaders mXRefreshViewHeaders;


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("选择预约时间"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长


    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("选择预约时间"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.act_tarot_astrology_time);


        PushAgent.getInstance(this).onAppStart();


        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(YpSettings.USERID))
            userId = bundle.getInt(YpSettings.USERID);

        if (bundle.containsKey(YpSettings.COUNSEL_TYPE))
            mCounselorType = bundle.getInt(YpSettings.COUNSEL_TYPE);

        if (bundle.containsKey(YpSettings.COUNSE_DATA))
            workDateEntity = bundle.getParcelable(YpSettings.COUNSE_DATA);


        if (bundle.containsKey(YpSettings.USER_NAME))
            mUserNmae = bundle.getString(YpSettings.USER_NAME);

        if (bundle.containsKey(YpSettings.COUNSE_CHARE))
            mChare = bundle.getLong(YpSettings.COUNSE_CHARE);


        initView();

        initXRefreshView();

        initData();

    }


    private void initView() {

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getTvTitle().setText("选择咨询时间");


        mFragmentList = new ArrayList<>();


        mLayoutInflater = this.getLayoutInflater();

        tarotAstrologyTime_xrv_time = (XRefreshView) findViewById(R.id.tarotAstrologyTime_xrv_time);

        tarotAstrologyTime_rv = (RecyclerView) findViewById(R.id.tarotAstrologyTime_rv);

        tarotAstrologyTime_rv.setLayoutManager(new LinearLayoutManager(this));

        tarotAstrologyTime_rv.setItemAnimator(new DefaultItemAnimator());


        mTarotAstrologyTimeAdapter = new TarotAstrologyTimeAdapter(this);

        mTarotAstrologyTimeAdapter.setOnTimeItemClickLitener(this);

        mTarotAstrologyTimeTabAdapter = new TarotAstrologyTimeTabAdapter(this, mTarotAstrologyTimeAdapter);


        tarotAstrologyTime_rv.setAdapter(mTarotAstrologyTimeTabAdapter);


    }

    private void initData() {


        if (workDateEntity == null) {
            finish();
            return;
        }

        mTarotAstrologyTimeTabAdapter.setData(workDateEntity);


    }

    private void initXRefreshView() {

        tarotAstrologyTime_xrv_time.setMoveForHorizontal(true);

        // 设置是否可以下拉刷新
        tarotAstrologyTime_xrv_time.setPullRefreshEnable(true);

        // 设置是否可以上拉加载
        tarotAstrologyTime_xrv_time.setPullLoadEnable(false);


        mXRefreshViewHeaders = new XRefreshViewHeaders(this);

        tarotAstrologyTime_xrv_time.setCustomHeaderView(mXRefreshViewHeaders);


        tarotAstrologyTime_xrv_time.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWorkDateTimesData();

                    }
                }, 1000);

            }


        });

    }


    private void getWorkDateTimesData() {


        WorkDatetimesBean workDatetimesBean = new WorkDatetimesBean();

        workDatetimesBean.userId = userId;

        workDatetimesBean.counselorType = mCounselorType;


        WorkTimesService workTimesService = new WorkTimesService(this);

        workTimesService.parameter(workDatetimesBean);

        workTimesService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                WorkDatetimesEntity workDatetimesEntity = (WorkDatetimesEntity) respBean;

                WorkDateEntity workDateEntity = workDatetimesEntity.resp;


                mTarotAstrologyTimeTabAdapter.setData(workDateEntity);

                tarotAstrologyTime_xrv_time.stopRefresh();

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                mXRefreshViewHeaders.onRefreshFail();

                tarotAstrologyTime_xrv_time.stopRefresh();


                if (TextUtils.equals("400", respBean.getStatus())) {


                    reservation_dlg = DialogUtil.createHintOperateDialog(TarotOrAstrologyTimeActivity.this, "", respBean.msg, "", "好的", new BackCallListener() {
                        @Override
                        public void onCancel(View view, Object... obj) {

                            reservation_dlg.dismiss();

                        }

                        @Override
                        public void onEnsure(View view, Object... obj) {

                            reservation_dlg.dismiss();
                            finish();


                        }
                    });
                    reservation_dlg.show();

                    return;


                }


            }
        });

        workTimesService.enqueue();


    }


    /**
     * 预约请求
     *
     * @param counselType
     * @param receiveUserId
     * @param bookingTime
     */

    private void postOrdersCounsel(int counselType, int receiveUserId, String bookingTime) {


        if (counselType == Constant.CounselorType_Tarot) {

            counselType = Constant.OrdersType_counsel_Tarot;

        } else if (counselType == Constant.CounselorType_Astrology) {
            counselType = Constant.OrdersType_counsel_Star;

        } else if (counselType == Constant.CounselorType_Psychological) {

            isTimeItemClick = false;
            return;
        }


        final OrderCounselBean orderCounselBean = new OrderCounselBean();

        orderCounselBean.counselType = counselType;

        orderCounselBean.receiveUserId = receiveUserId;

        orderCounselBean.bookingTime = bookingTime;

        OrderCounselService orderCounselService = new OrderCounselService(TarotOrAstrologyTimeActivity.this);


        orderCounselService.parameter(orderCounselBean);

        orderCounselService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                OrderCounselRespEntity orderCounselRespEntity = (OrderCounselRespEntity) respBean;

                final OrderCounselEntity orderCounselEntity = orderCounselRespEntity.resp;


                if (orderCounselEntity == null) {
                    isTimeItemClick = false;
                    mTarotAstrologyTimeTabAdapter.setIsSelect(null);
                    return;
                }

                if (orderCounselEntity.result == 0) {//成功


                    if (orderCounselEntity.order == null) {
                        isTimeItemClick = false;
                        mTarotAstrologyTimeTabAdapter.setIsSelect(null);

                        return;
                    }

                    isTimeItemClick = false;


                    Bundle bundle = new Bundle();

                    bundle.putString(YpSettings.ORDER_ID, orderCounselEntity.order.orderId);
                    bundle.putInt(YpSettings.FROM_PAGE, 1);

                    ActivityUtil.jump(TarotOrAstrologyTimeActivity.this, UserOrderPayActivity.class, bundle, 0, 100);


                    return;
                }


                if (orderCounselEntity.result == 2) {//存在该咨询师其他订单

                    mDealWith_dlg = DialogUtil.createHintOperateDialog(TarotOrAstrologyTimeActivity.this, "", orderCounselEntity.msg, "取消", "去处理", new BackCallListener() {
                        @Override
                        public void onCancel(View view, Object... obj) {
                            isTimeItemClick = false;
                            mDealWith_dlg.dismiss();
                            mTarotAstrologyTimeTabAdapter.setIsSelect(null);


                        }

                        @Override
                        public void onEnsure(View view, Object... obj) {
                            isTimeItemClick = false;
                            mDealWith_dlg.dismiss();
                            mTarotAstrologyTimeTabAdapter.setIsSelect(null);


                            Bundle bundle = new Bundle();
                            bundle.putInt(YpSettings.ORDER_TYPE, Constant.OrderType_Advisory);


                            ActivityUtil.jump(TarotOrAstrologyTimeActivity.this, UserOrderListActivity.class, bundle, 0, 100);


                        }
                    });

                    mDealWith_dlg.setCanceledOnTouchOutside(false);
                    mDealWith_dlg.setCancelable(false);
                    mDealWith_dlg.show();

                    return;
                }

                //1：库存不足  3：预约时间过期 4：预约时间已删除 5：不能预约30分钟内的订单 6：服务被暂停

                mComplete_dlg = DialogUtil.createHintOperateDialog(TarotOrAstrologyTimeActivity.this, "", orderCounselEntity.msg, "", "好的", new BackCallListener() {
                    @Override
                    public void onCancel(View view, Object... obj) {
                        isTimeItemClick = false;
                        mComplete_dlg.dismiss();
                        mTarotAstrologyTimeTabAdapter.setIsSelect(null);
                        tarotAstrologyTime_xrv_time.startRefresh();

                    }

                    @Override
                    public void onEnsure(View view, Object... obj) {
                        isTimeItemClick = false;
                        mComplete_dlg.dismiss();
                        mTarotAstrologyTimeTabAdapter.setIsSelect(null);

                        tarotAstrologyTime_xrv_time.startRefresh();

                    }
                });

                mDealWith_dlg.setCanceledOnTouchOutside(false);
                mDealWith_dlg.setCancelable(false);
                mComplete_dlg.show();


                return;

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);
                isTimeItemClick = false;

                mTarotAstrologyTimeTabAdapter.setIsSelect(null);

                String msg = respBean.msg;

                if (TextUtils.isEmpty(msg)) {

                    DialogUtil.showDisCoverNetToast(TarotOrAstrologyTimeActivity.this);

                    return;

                }

                DialogUtil.showDisCoverNetToast(TarotOrAstrologyTimeActivity.this, msg);

            }
        });

        orderCounselService.enqueue();


    }

    @Override
    public void onTimeItemClick(int position, Object data) {
        if (isTimeItemClick) {
            return;
        }
        isTimeItemClick = true;

        mTarotAstrologyTimeTabAdapter.setIsSelect(position);

        final WorkTimesEntity workTimesEntity = (WorkTimesEntity) data;

        if (workTimesEntity == null) {
            isTimeItemClick = false;
            return;
        }

        if (!workTimesEntity.isFullReservation) {//可预约


            String name_title = "";

            if (mCounselorType == Constant.CounselorType_Tarot) {

                name_title = "服务塔罗师：";


            } else if (mCounselorType == Constant.CounselorType_Astrology) {

                name_title = "服务占卜师：";

            } else if (mCounselorType == Constant.CounselorType_Psychological) {

            }


            long time = TimeUtils.getFormat(workTimesEntity.workTime);

            String  date = TimeUtils.longToString(time, "yyyy-MM-dd HH:mm:ss");

            StringBuilder sb = new StringBuilder();


            sb.append("￥");
            sb.append(mChare);


            mPay_dlg = DialogUtil.createHintTimeDialog(TarotOrAstrologyTimeActivity.this, name_title, mUserNmae, "预约时间：", date, "咨询费用：", sb.toString(), "取消", "确认预约", new BackCallListener() {
                @Override
                public void onCancel(View view, Object... obj) {
                    isTimeItemClick = false;
                    mPay_dlg.dismiss();
                    mTarotAstrologyTimeTabAdapter.setIsSelect(null);

                }

                @Override
                public void onEnsure(View view, Object... obj) {

                    mTarotAstrologyTimeTabAdapter.setIsSelect(null);
                    mPay_dlg.dismiss();


                    if (mCounselorType == Constant.CounselorType_Tarot) {

                        MobclickAgent.onEvent(TarotOrAstrologyTimeActivity.this, "btn_Timelist_event_T_Reservation_confirm");


                    } else if (mCounselorType == Constant.CounselorType_Astrology) {

                        MobclickAgent.onEvent(TarotOrAstrologyTimeActivity.this, "btn_Timelist_event_A_Reservation_confirm");

                    }

                    postOrdersCounsel(mCounselorType, userId, workTimesEntity.workTime);


                }
            });

            mPay_dlg.setCanceledOnTouchOutside(false);
            mPay_dlg.setCancelable(false);
            mPay_dlg.show();


        } else {
            isTimeItemClick = false;
        }


    }
}
