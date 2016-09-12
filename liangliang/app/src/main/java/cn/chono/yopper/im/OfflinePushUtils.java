package cn.chono.yopper.im;

import android.content.Context;
import android.net.Uri;

import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushSettings;

import cn.chono.yopper.R;

/**
 * Created by sunquan on 16/8/24.
 */
public class OfflinePushUtils {


    public static void initPush(Context context) {

        TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
        //开启离线推送
        settings.setEnabled(true);
        //设置收到C2C离线消息时的提示声音，这里把声音文件放到了res/raw文件夹下
        settings.setC2cMsgRemindSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.office));
        //设置收到群离线消息时的提示声音，这里把声音文件放到了res/raw文件夹下
        settings.setGroupMsgRemindSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.office));

        TIMManager.getInstance().configOfflinePushSettings(settings);

        TIMManager.getInstance().disableRecentContact();

    }

}
