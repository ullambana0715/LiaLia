package cn.chono.yopper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import cn.chono.yopper.activity.base.StartActivity;
import cn.chono.yopper.utils.ActivityUtil;


public class UmengmsgReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.enable();

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                String code=msg.extra.get("code").toString();
                String navigate=msg.extra.get("navigate").toString();
                if(code!=null && code.equals("ArticleView")){

                    Bundle bundle=new Bundle();
                    bundle.putString("ArticleView",navigate);
                    ActivityUtil.jump(context,StartActivity.class, bundle, 2, 100);
                }

            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }
}