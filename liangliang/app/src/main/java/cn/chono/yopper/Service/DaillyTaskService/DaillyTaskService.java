package cn.chono.yopper.Service.DaillyTaskService;

import android.text.TextUtils;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.base.App;
import cn.chono.yopper.utils.TimeUtil;

/**
 * Created by zxb on 2015/11/25.
 */
public class DaillyTaskService {

    private static DaillyTaskService daillyTaskService;

    public static DaillyTaskService getInstance() {

        if (null == daillyTaskService) {
            synchronized (DaillyTaskService.class) {
                if (null == daillyTaskService) {
                    daillyTaskService = new DaillyTaskService();
                }
            }
        }
        return daillyTaskService;

    }

    public static List<DaillyBean> listTask;

    /**
     * 抓取自已的任务
     */
    public void seekTask() {
        listTask = new ArrayList<>();
        //检测任务
        String authToken = LoginUser.getInstance().getAuthToken();

        if (TextUtils.isEmpty(authToken)) {//检测登陆状态 ---未登陆
            return;

        }

        //已经登陆，检测是不是当天

        int userid = LoginUser.getInstance().getUserId();

        try {
            List<DaillyBean> beanList = App.getInstance().db.findAll(Selector.from(DaillyBean.class).where("userID", " =", userid));
            //判断是不是当天的任务
            if (null != beanList && beanList.size() > 0) {
                for (int i = 0; i < beanList.size(); i++) {
                    long time = beanList.get(i).getTime();
                    boolean isSte = beanList.get(i).isState();
                    if (TimeUtil.isToday(time)) {//是今天，则加入任务中
                        LogUtils.e("  isSte  =" + isSte);
                        if (!isSte) {
                            listTask.add(beanList.get(i));
                        }

                    } else {//如果不是当天的，直接删除掉。
                        App.getInstance().db.delete(beanList.get(i));
                    }
                }


            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        LogUtils.e("  list  =" + listTask.toString() + "\n" + listTask.size());

    }


    public void addTask(int userID, DaillyBean daillyBean) {
        if (null == daillyBean) {
            return;
        }
        try {

            DaillyBean bean = App.getInstance().db.findFirst(Selector.from(DaillyBean.class).where("userID", " =", userID).and("tag", " =", daillyBean.getTag()));
            if (null == bean) {
                App.getInstance().db.save(daillyBean);
                LogUtils.e("---------------------------保存----");
            }


        } catch (DbException e) {
            e.printStackTrace();
            LogUtils.e("--------------------------不-保存----");
        }


    }

    public void getNowTask(OnDaillyTaskServiceListener onDaillyTaskServiceListener, String tag) {

        for (int i = 0; i < listTask.size(); i++) {
            String tags = listTask.get(i).getTag();
            int useriD = listTask.get(i).getUserID();
            String data = listTask.get(i).getData();
            if (TextUtils.equals(tag, tags)) {
                if (null != onDaillyTaskServiceListener) {
                    boolean isexecution = onDaillyTaskServiceListener.execution(data);
                    if (isexecution) {
                        try {
                            DaillyBean bean = App.getInstance().db.findFirst(Selector.from(DaillyBean.class).where("userID", " =", useriD).and("tag", " =", tags));
                            if (null != bean) {
                                bean.setState(true);
                                App.getInstance().db.update(bean);
                            }

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        listTask.remove(i);

                    }
                }

            }

        }

    }

    public void upDateTask(String tag, boolean isState) {
        for (int i = 0; i < listTask.size(); i++) {
            String tags = listTask.get(i).getTag();
            int useriD = listTask.get(i).getUserID();
            String data = listTask.get(i).getData();
            if (TextUtils.equals(tag, tags)) {
                if (isState) {
                    try {
                        DaillyBean bean = App.getInstance().db.findFirst(Selector.from(DaillyBean.class).where("userID", " =", useriD).and("tag", " =", tags));
                        if (null != bean) {
                            bean.setState(isState);
                            App.getInstance().db.update(bean);
                        }

                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    listTask.remove(i);

                }


            }

        }


    }

    public void getUpdateTaskDate(String tag, String upDate) {
        for (int i = 0; i < listTask.size(); i++) {
            String tags = listTask.get(i).getTag();
            int useriD = listTask.get(i).getUserID();
            String data = listTask.get(i).getData();
            if (TextUtils.equals(tag, tags)) {
                try {
                    DaillyBean bean = App.getInstance().db.findFirst(Selector.from(DaillyBean.class).where("userID", " =", useriD).and("tag", " =", tags));
                    if (null != bean) {
                        bean.setState(false);
                        bean.setData(upDate);
                        App.getInstance().db.update(bean);
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

                listTask.get(i).setData(upDate);

            }

        }

    }


}
