package cn.chono.yopper.utils;

import android.text.TextUtils;

import java.util.HashMap;

import cn.chono.yopper.AppInfo;
import cn.chono.yopper.base.App;
import cn.chono.yopper.data.LoginUser;

/**
 * Created by cc on 16/8/8.
 */
public class OkHttpHeader {


    /**
     * @param @param asyncHttpClient 设定文件
     * @return void 返回类型
     * @throws
     * @Title: httpHeader
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static HashMap<String, String> addHeader() {


        String AppVersion = AppInfo.getInstance().getVersionName();// 客户端版本

        String PlatformVersion = AppInfo.getInstance().getPhoneVersion();// 平台版本

        String Market = AppInfo.getInstance().getAppMarketId();

        String ClientId = AppInfo.getInstance().getMacAddress();


        if (TextUtils.isEmpty(AppVersion)) {
            AppVersion = AppUtil.getAppVersionName(App.getContext());
        }

        if (TextUtils.isEmpty(PlatformVersion)) {
            PlatformVersion = AppUtil.getPhoneVersion(App.getContext());
        }

        if (TextUtils.isEmpty(Market)) {
            Market = AppUtil.getChannelFromApk(App.getContext(), "channelId");
        }

        if (TextUtils.isEmpty(ClientId)) {

            ClientId = AppUtil.getMacAddress(App.getContext());

        }


        String App_Version = "yuepeng-" + AppVersion + "-android-" + PlatformVersion + "-" + Market;

        String RequestId = UUIDGenerator.getUUID();

        long Timestamp = TimeUtil.getHundredNanosecond();

        String SecretKey = "chono_rocks";

        String AppSign = SHA.encodeSHA1(App_Version + RequestId + ClientId + SHA.encodeSHA1(SecretKey + ClientId) + Timestamp);

        HashMap<String, String> header = new HashMap<>();
        header.put("AppSign", AppSign);
        header.put("ClientId", ClientId);
        header.put("Timestamp", Timestamp + "");
        header.put("AppVersion", App_Version);
        header.put("RequestId", RequestId);
        if (!TextUtils.isEmpty(LoginUser.getInstance().getAuthToken())) {
            header.put("AuthToken", LoginUser.getInstance().getAuthToken());
        }
        return header;
    }


}


