package cn.chono.yopper.task;

import android.os.AsyncTask;
import android.os.Bundle;

import com.umeng.message.PushAgent;

import cn.chono.yopper.base.App;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.login.LoginOrRegisterActivity;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.im.imEvent.MessageEvent;
import cn.chono.yopper.im.imbusiness.TlsBusiness;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.AppUtil;
import cn.chono.yopper.utils.ContextUtil;

public class RemoveAliasAsyncTask extends AsyncTask<String, Integer, String> {

    // 该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
    // 主要用于进行异步操作
    @Override
    protected String doInBackground(String... params) {


        try {
            PushAgent mPushAgent = PushAgent.getInstance(ContextUtil.getContext());
            mPushAgent.removeAlias(LoginUser.getInstance().getUserId() + "", "chono");


        } catch (Exception e) {
            e.printStackTrace();
        }


        return params[0];

    }

    // 该方法运行在Ui线程内，可以对UI线程内的控件设置和修改其属性
    @Override
    protected void onPreExecute() {
    }


    // 在doInBackground方法执行结束后再运行，并且运行在UI线程当中
    // 主要用于将异步操作任务执行的结果展示给用户
    @Override
    protected void onPostExecute(String result) {


        if (YpSettings.isTest) {
            TlsBusiness.logout(LoginUser.getInstance().getUserId() + "@test");
        } else {
            TlsBusiness.logout(LoginUser.getInstance().getUserId() + "");
        }

        MessageEvent.getInstance().clear();

        AppUtil.setRefreshTokenExpiration(ContextUtil.getContext(), 0);

        LoginUser.getInstance().reSetLoginUser();


        Bundle bundle = new Bundle();
        String topact = ActivityUtil.getTopActivity();
        if (topact == null || !topact.equals("ComponentInfo{cn.chono.yopper/cn.chono.yopper.activity.login.LoginOrRegisterActivity}")) {
            bundle.putString(YpSettings.ConnectionClosedOnErrorMsg, result);
        }

        //需要清理task 所以传入flagtype=2
        ActivityUtil.jump(ContextUtil.getContext(), LoginOrRegisterActivity.class, bundle, 2, 0);

    }


}
