package cn.chono.yopper.Service.DaillyTask;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import cn.chono.yopper.Service.DaillyTaskService.DaillyBean;
import cn.chono.yopper.Service.DaillyTaskService.DaillyTaskService;
import cn.chono.yopper.Service.Http.DaillyTouch.DaillyTouchBean;
import cn.chono.yopper.Service.Http.DaillyTouch.DaillyTouchRespBean;
import cn.chono.yopper.Service.Http.DaillyTouch.DaillyTouchService;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.DailyTouchDto;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.utils.TimeUtil;

/**
 * Created by zxb on 2015/11/24.
 */
public class DaillyTask {

    private static DaillyTask daillyTask;


    public static DaillyTask getInstance() {

        if (null == daillyTask) {
            synchronized (DaillyTask.class) {
                if (null == daillyTask) {
                    daillyTask = new DaillyTask();
                }
            }
        }
        return daillyTask;

    }


    private OnDaillTaskListener onDaillTaskListener;

    public void enqueue(Context context, OnDaillTaskListener onDaillTaskListener) {


        this.onDaillTaskListener = onDaillTaskListener;
        //检测任务
        String authToken = LoginUser.getInstance().getAuthToken();

        if (TextUtils.isEmpty(authToken)) {//检测登陆状态 ---未登陆
            return;

        }
        int userId = LoginUser.getInstance().getUserId();
        try {


            DailyTouchDto dailyTouchDto = App.getInstance().db.findFirst(Selector.from(DailyTouchDto.class).where("id", " =", userId));
            if (null == dailyTouchDto) {//没有本地数据。应该去请求网络
                getDaillyTouch(context, userId);
            } else { //已经登陆，检测是不是当天
                long time = dailyTouchDto.getTime();
                if (TimeUtil.isToday(time)) {//是今天
                    if (null != onDaillTaskListener) {
                        onDaillTaskListener.onSuccess();
                    }

                } else {
                    //不是今天的
                    getDaillyTouch(context, userId);
                }


            }


        } catch (DbException e) {
            e.printStackTrace();
            getDaillyTouch(context, userId);
        }


    }

    /**
     * 获取每日任务
     *
     * @param context
     * @param userId
     */
    private void getDaillyTouch(final Context context, final int userId) {

        DaillyTouchBean daillyTouchBean = new DaillyTouchBean();
        daillyTouchBean.setUserId(userId);

        DaillyTouchService daillyTouchService = new DaillyTouchService(context);
        daillyTouchService.parameter(daillyTouchBean);
        daillyTouchService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                DaillyTouchRespBean daillyTouchRespBean = (DaillyTouchRespBean) respBean;
                DaillyTouchRespBean.DaillyTouch daillyTouch = daillyTouchRespBean.getResp();

                if (null != daillyTouch) {

                    int dailyKeysGet = daillyTouch.getDailyKeysGet();

                    long time = System.currentTimeMillis();

                    //每日钥匙
                    if (dailyKeysGet > 0) {

                        DaillyBean daillyBean = new DaillyBean();
                        daillyBean.setUserID(userId);
                        daillyBean.setTime(time);
                        daillyBean.setTag(YpSettings.DAILLY_TASK_KEYS);
                        daillyBean.setState(false);
                        daillyBean.setData(dailyKeysGet + "");
                        DaillyTaskService.getInstance().addTask(userId, daillyBean);

                    }


                    saveDataDaily(userId, time);

                    if (null != onDaillTaskListener) {
                        onDaillTaskListener.onSuccess();
                    }

                } else {
                    if (null != onDaillTaskListener) {
                        onDaillTaskListener.onSuccess();
                    }
                }


            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                if (null != onDaillTaskListener) {
                    onDaillTaskListener.onSuccess();
                }
            }


        });

        daillyTouchService.enqueue();

    }


    private void saveDataDaily(final int userId, final long time) {
        try {
            DailyTouchDto dailyTouchDto = App.getInstance().db.findFirst(Selector.from(DailyTouchDto.class).where("id", " =", userId));
            if (null != dailyTouchDto) {

                dailyTouchDto.setTime(time);
                dailyTouchDto.setId(userId);
                App.getInstance().db.update(dailyTouchDto);
            } else {
                DailyTouchDto dto = new DailyTouchDto();
                dto.setTime(time);
                dto.setId(userId);
                App.getInstance().db.save(dto);
            }


        } catch (DbException e) {
            e.printStackTrace();
        }

    }

}
