package cn.chono.yopper.im.imService;

/**
 * Created by dgy on 15/7/20.
 * Description: 该类用于保存TLS SDK、QQ SDK、WeChat SDK相关的配置信息
 */
public class TLSConfiguration {

    // TLS SDK
    public static long SDK_APPID = -1;
    public static int ACCOUNT_TYPE = -1;
    public static String COUNTRY_CODE = "86";
    public static int LANGUAGE_CODE = 2052;
    public static int TIMEOUT = 8000;
    public static String APP_VERSION = "1.0";     // 指的是TLSDemo的版本号，而不是TLSSDK的版本号



    public static void setSdkAppid(long sdkAppid) {
        SDK_APPID = sdkAppid;
    }


    public static void setAccountType(int accountType) {
        ACCOUNT_TYPE = accountType;
    }

    public static void setTimeout(int timeout) {
        TIMEOUT = timeout;
    }
}
