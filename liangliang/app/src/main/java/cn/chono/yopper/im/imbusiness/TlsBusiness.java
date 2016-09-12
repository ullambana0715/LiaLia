package cn.chono.yopper.im.imbusiness;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.tencent.TIMManager;

import cn.chono.yopper.R;
import cn.chono.yopper.im.ImConstant;
import cn.chono.yopper.im.imService.TLSConfiguration;
import cn.chono.yopper.im.imService.TLSService;

/**
 * 初始化tls登录模块
 */
public class TlsBusiness {


    private TlsBusiness() {
    }

    public static void init(Context context) {
        TLSConfiguration.setSdkAppid(ImConstant.SDK_APPID);
        TLSConfiguration.setAccountType(ImConstant.ACCOUNT_TYPE);
        TLSConfiguration.setTimeout(8000);
        TLSService.getInstance().initTlsSdk(context);

    }

    public static void logout(String id) {

        if (!TextUtils.isEmpty(id)) {
            TLSService.getInstance().clearUserInfo(id);
        }
    }


}
