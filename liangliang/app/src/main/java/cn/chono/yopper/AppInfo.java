package cn.chono.yopper;

/**
 * 用户数据
 */
public class AppInfo {


    private static AppInfo appInfoInstance = new AppInfo();

    public static AppInfo getInstance() {
        return appInfoInstance;
    }



    private  static String versionName;

    private static int versionCode;

    private static String phoneVersion;

    private static String macAddress;

    private static String appMarketId;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getPhoneVersion() {
        return phoneVersion;
    }

    public void setPhoneVersion(String phoneVersion) {
        this.phoneVersion = phoneVersion;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAppMarketId() {
        return appMarketId;
    }

    public void setAppMarketId(String appMarketId) {
        this.appMarketId = appMarketId;
    }

}