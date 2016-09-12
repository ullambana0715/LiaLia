package cn.chono.yopper.location;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.lidroid.xutils.util.LogUtils;

import java.util.Date;

import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;

public class Loc {
    public static LocationManager lm;
    private static KeyguardManager mKeyguardManager;

    // 消息
    public static final int MSG_START_UPDATE = 1;
    public static final int MSG_STOP_UPDATE = 2;

    // 缓存时间间隔
    // public static final int LOC_CACHE_INTERVAL = 60* 1000; //60秒
    // public static final int LOC_CACHE_FIRST_USED = 5 * 60* 1000; //5分钟

    public static final int LOC_CACHE_INTERVAL = 10 * 60 * 1000; // 缓存超时时间：60秒
    public static final int LOC_CACHE_FIRST_USED = 10 * 60 * 1000; // 首次使用缓存的超时时间：4分钟

    // 监听时间间隔
    public static final int PROVIDER_WATCH_INTERVAL = 2000;

    // 其他
    private static Location lastLoc;
    private static Location tmpNetworkLoc;
    private static Location tmpGpsLoc;
    public static boolean isGpsOkTag = false; // 是否gps ok
    public static Location tmpNetworkLocByListener; // 由Listener更新的网络位置
    public static Location tmpGpsLocByListener; // 由Listener更新的Gps位置

    public static Application myApp;
    private static String addressInfo = "";
    private static String lastaddressInfo = "";

    public static long locationTime = 0;

    public static boolean isOpenGpsUpdate = false;
    private static boolean isOpenNetworkUpdate = false;
    private static boolean isOpenBaiduUpdate = false;

    private static Handler locHandler;
    public static boolean isLocMonitorStarted = false;

    private static String District = "";
    private static String Street = "";
    private static String AddrStr = "";
    private static String city = "";
    private static String province="";

    private static String lastcity = "";
    private static String lastDistrict = "";
    private static String lastStreet = "";
    private static String lastAddrStr = "";
    private static String lastprovince="";
	/*--------------------------------------------------------------------------
    | 公开方法
	--------------------------------------------------------------------------*/
    // 初始化 在系统启动时候调用

    public static void ini(Application app) {
        // 启动定位线程
        locHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    super.handleMessage(msg);
                    if (msg.what == MSG_START_UPDATE) {
                        // 开始监听
                        if (YpSettings.gBaiduAvailable) {
                            startBaiduUpdate();
                        } else {
                            startGpsUpdate();
                            startNetworkUpdate();
                        }
                        //
                    } else if (msg.what == MSG_STOP_UPDATE) {
                        // 停止监听
                        //
                        stopBaiduUpdate();
                        stopGpsUpdate();
                        stopNetworkUpdate();
                        //
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        myApp = app;

        lm = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);

        mKeyguardManager = (KeyguardManager) app
                .getSystemService(Context.KEYGUARD_SERVICE);

        // 启动网络线程
        // new LocBaiduThread(locHandler,myApp);
        // 锁屏控制，当锁屏时就不再进行GPS定位
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);

        app.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action) && ActivityUtil.isOnForeground(myApp.getApplicationContext())
                        && !mKeyguardManager.inKeyguardRestrictedInputMode()) {
                    // Log.d("屏幕", "屏幕开启");
                    // LocBaiduThread.isScreenOn=true;
                    sendLocControlMessage(true);

                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    // Log.d("屏幕", "屏幕关闭");
                    // LocBaiduThread.isScreenOn=false;
                    sendLocControlMessage(false);
                }
            }
        }, filter);
    }

    public static void sendLocControlMessage(boolean isStart) {
        if (isStart) {
            if (!isLocMonitorStarted)
                locHandler.sendMessage(locHandler
                        .obtainMessage(Loc.MSG_START_UPDATE));
        } else {
            if (isLocMonitorStarted)
                locHandler.sendMessage(locHandler
                        .obtainMessage(Loc.MSG_STOP_UPDATE));
        }
    }

    public static boolean IsLocExist() {

        try {

            LocInfo obj = getLoc();
            if (obj != null && obj.getLoc() != null) {
                return true;
            } else {

                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    // 获得位置信息
    public static LocInfo getLoc() {

        try {
            LocInfo re = new LocInfo();
            if (YpSettings.gBaiduAvailable) {
                if (isAvailabelLoc(LocBaidu.loc)) {
                    LogUtils.e("使用了最新定位");
                    lastLoc = LocBaidu.loc;
                    if (lastLoc == null) {
                        lastLoc = new Location("baidu[" + "161" + "]");
                        lastLoc.setLatitude(31.240517);
                        lastLoc.setLongitude(121.478844);


                    }
                    lastLoc.setTime(System.currentTimeMillis());

                    String citystr = LocBaidu.currentLocation.getCity();


                    if (!CheckUtil.isEmpty(citystr)) {


                        if (citystr.contains("市")) {
                            String[] cityarray = citystr.split("市");
                            if (cityarray != null && !CheckUtil.isEmpty(cityarray[0])) {
                                city = cityarray[0];

                            } else {
                                city = "上海";

                            }

                        } else if (citystr.contains("特别行政区")) {

                            String[] cityarray = citystr.split("特");
                            if (cityarray != null && !CheckUtil.isEmpty(cityarray[0])) {
                                city = cityarray[0];
                            }

                        } else {
                            city = citystr;
                        }

                    } else {
                        city = "上海";

                    }

                    String  provinceStr=LocBaidu.currentLocation.getProvince();

                    if (!CheckUtil.isEmpty(provinceStr)) {

                        String lastprostr=provinceStr.substring(provinceStr.length()-1);

                        String twolastprostr="";

                        if(provinceStr.contains("特别行政区")){
                            twolastprostr=provinceStr.substring(provinceStr.length()-5);
                        }

                        if (lastprostr.equals("省") || lastprostr.equals("市")) {

                            province=provinceStr.substring(0,provinceStr.length()-1);

                        } else if (twolastprostr.equals("特别行政区")) {

                            province=provinceStr.substring(0,provinceStr.length()-5);

                        }else {

                            province = citystr;

                        }

                    } else {
                        province = "上海";

                    }

                    addressInfo = LocBaidu.currentLocation.getDistrict() + LocBaidu.currentLocation.getStreet();
                    AddrStr = LocBaidu.currentLocation.getAddrStr();
                    District = LocBaidu.currentLocation.getDistrict();
                    Street = LocBaidu.currentLocation.getStreet();


                } else if (isAvailabelLoc(lastLoc) && isLocAvailableBasisOfFirstTimeUsed(lastLoc)) {
                    // 缓存
                    LogUtils.e("使用了缓存定位");
                    lastLoc.setTime(System.currentTimeMillis()); // 每次使用此缓存，更新缓存位置的时间戳为当前时间
                    addressInfo = lastaddressInfo;
                    AddrStr = lastAddrStr;
                    District = lastDistrict;
                    Street = lastStreet;
                    city = lastcity;
                    province=lastprovince;
                } else {
                    lastLoc = null;
                    addressInfo = "";
                    AddrStr = "";
                    District = "";
                    Street = "";
                    city = "";
                    province="";
                }
            } else {


                if (isAvailabelLoc(tmpGpsLoc)) {

                    // GPS
                    lastLoc = tmpGpsLoc;
                    lastLoc.setTime(System.currentTimeMillis());
                    if (lastLoc != null) {
                        addressInfo = "";
                        AddrStr = "";
                        District = "";
                        Street = "";
                        city = "";
                        province="";
                    }

                } else if (isAvailabelLoc(tmpNetworkLoc)) {
                    // 网络
                    lastLoc = tmpNetworkLoc;
                    lastLoc.setTime(System.currentTimeMillis());
                    if (lastLoc != null) {
                        addressInfo = "";
                        AddrStr = "";
                        District = "";
                        Street = "";
                        city = "";
                        province="";
                    }

                } else {
                    lastLoc = null;
                    addressInfo = "";
                    AddrStr = "";
                    District = "";
                    Street = "";
                    city = "";
                    province="";
                }

            }

            lastaddressInfo = addressInfo;
            lastAddrStr = AddrStr;
            lastDistrict = District;
            lastStreet = Street;
            lastcity = city;
            lastprovince=province;
            re.setLoc(lastLoc);
            re.setInfo(addressInfo);
            re.setAddrStr(AddrStr);
            re.setDistrict(District);
            re.setStreet(Street);
            re.setCity(city);
            re.setProvince(province);
            return re;
        } catch (Exception e) {
            if (e != null) {

                LogUtils.e("异常＝" + e.getMessage().toString());
            } else {
                LogUtils.e("异常");
            }

            return null;
        }
    }


    // 是否开通了gps定位或者网络定位
    public static boolean isGpsAvailable() {

        try {
            if (YpSettings.gBaiduAvailable) {
                return true;
            } else {
                if (lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                        || lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    // public static Location getAvailableLocByPoviderAndTime(String provider,
    // long nowTime) {
    // List<String> providers = Loc.lm.getProviders(true);
    // if (providers != null && providers.contains(provider)) {
    // Location tmpLoc = Loc.lm.getLastKnownLocation(provider);
    // if (tmpLoc != null && tmpLoc.getTime() >= nowTime) {
    // return tmpLoc;
    // }
    // }
    // return null;
    // }

    /*--------------------------------------------------------------------------
    | 其他
    --------------------------------------------------------------------------*/
    // 是否是有效经纬度
    private static boolean isAvailabelLoc(Location c) {
        if (c != null && isOVerLocCacheInterval(c.getTime()) == false) {
            // 中国有效经纬度 纬度17-54 经度 70-136
            if (c.getLatitude() > 10 && c.getLatitude() < 60 && c.getLongitude() > 60 && c.getLongitude() < 150) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }


    public static LatLng getBaiduGpsFromGcj(double lat, double lon) {

        LatLng pt = new LatLng(lat, lon);
        CoordinateConverter ct = new CoordinateConverter();
        ct.from(CoordinateConverter.CoordType.COMMON);
        ct.coord(pt);

        return ct.convert();
    }

    // 是否超出时间间隔
    private static boolean isOVerLocCacheInterval(long t) {
        long cc = new Date().getTime() - t;
        if (cc > LOC_CACHE_INTERVAL) {
            return true;
        } else {
            return false;
        }
    }

    // 开始监听
    private static void startGpsUpdate() {
        try {
            if (!isOpenGpsUpdate
                    && isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                        1, gpsLocationListener);
                isOpenGpsUpdate = true;
                isLocMonitorStarted = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private static void startNetworkUpdate() {
        try {
            if (!isOpenNetworkUpdate
                    && isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000, 1, networkLocationListener);
                isOpenNetworkUpdate = true;
                isLocMonitorStarted = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // 停止监听
    private static void stopGpsUpdate() {
        try {
            if (isOpenGpsUpdate) {
                lm.removeUpdates(gpsLocationListener);
                isOpenGpsUpdate = false;
                isLocMonitorStarted = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private static void stopNetworkUpdate() {
        try {
            if (isOpenNetworkUpdate) {
                lm.removeUpdates(networkLocationListener);
                isOpenNetworkUpdate = false;
                isLocMonitorStarted = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private static void startBaiduUpdate() {
        try {
            if (!isOpenBaiduUpdate) {
                LocBaidu.start();
                isOpenBaiduUpdate = true;
                isLocMonitorStarted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void stopBaiduUpdate() {
        try {
            if (isOpenBaiduUpdate) {

                LocBaidu.stop();
                isOpenBaiduUpdate = false;
                isLocMonitorStarted = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isProviderEnabled(String provider) {
        try {
            return lm.isProviderEnabled(provider);
        } catch (Exception e) {
            return false;
        }
    }

    private static final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (!ActivityUtil.isOnForeground(myApp.getApplicationContext())) {
                Loc.sendLocControlMessage(false);
            }

            if (location != null) {
                tmpGpsLocByListener = location;

                StringBuffer sb = new StringBuffer();
                sb.append("Update gps").append(" -- ");
                sb.append(location.getProvider()).append(" -- ");
                sb.append(getFormatDateStr(location.getTime())).append(" -- ");
                sb.append(location.getLongitude()).append(",")
                        .append(location.getLatitude()).append("\r\n");
                // IOUtils.writeTestInfo(myApp, "log_gps.txt", sb.toString());

                // if (location != null) {

                tmpGpsLoc = tmpGpsLocByListener;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private static final LocationListener networkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 当Network的Listener被触发时更新locationTime
            // locationTime = System.currentTimeMillis();
            if (!ActivityUtil.isOnForeground(myApp.getApplicationContext())) {
                Loc.sendLocControlMessage(false);

            }
            if (location != null) {

                tmpNetworkLocByListener = location;

                StringBuffer sb = new StringBuffer();
                sb.append("Update network gps").append(" -- ");
                sb.append(location.getProvider()).append(" -- ");
                sb.append(getFormatDateStr(location.getTime())).append(" -- ");
                sb.append(location.getLongitude()).append(",")
                        .append(location.getLatitude()).append("\r\n");
                // IOUtils.writeTestInfo(myApp, "log_gps.txt", sb.toString());

                tmpNetworkLoc = tmpNetworkLocByListener;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public static String getFormatDateStr(long d) {
        return getFormatDateStr(new Date(d), "yyyy-MM-dd HH:mm:ss");
    }

    private static String getFormatDateStr(Date d, String style) {
        if (d == null) {
            return "";
        } else {
            return new java.text.SimpleDateFormat(style).format(d);
        }
    }

    /**
     * 返回"此位置首次被使用的时间"距离"现在"是否可用
     *
     * @param loc
     * @return
     */
    private static boolean isLocAvailableBasisOfFirstTimeUsed(Location loc) {
        try {
            if (loc.getExtras() == null
                    || !loc.getExtras().containsKey("first")) {
                initLocFirstUsedTime(loc);
                return true;
            }
            long firstUsedTime = loc.getExtras().getLong("first");
            boolean result = (System.currentTimeMillis() - firstUsedTime) <= LOC_CACHE_FIRST_USED;
            // Log.e("Loc", "isLocAvailableBasisOfFirstTimeUsed:" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 初始化此位置首次使用的时间
     *
     * @param loc
     */
    private static void initLocFirstUsedTime(Location loc) {
        try {
            if (loc.getExtras() != null) {
                return;
            }
            Bundle extras = new Bundle();
            extras.putLong("first", System.currentTimeMillis());
            loc.setExtras(extras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
