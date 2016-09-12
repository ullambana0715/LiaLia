package cn.chono.yopper.location;

import android.location.Location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.chono.yopper.base.App;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.CheckUtil;

public class LocBaidu {

    public static Location loc = null;
    static LocationClient mLocationClient = null;


    static BDLocationListener mLocationListener = null;//create时注册此listener，Destroy时需要Remove

    public static BDLocation currentLocation = null;



    public static void init(final App app) {


        // 注册定位事件
        mLocationListener = new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                //后台中 定位停止
                if (!ActivityUtil.isOnForeground(app.getApplicationContext())) {
                    Loc.sendLocControlMessage(false);
                }

                if (location != null) {


                    currentLocation = location;

                    Location tloc = new Location("baidu[" + location.getLocType() + "]");
                    tloc.setLatitude(location.getLatitude());
                    tloc.setLongitude(location.getLongitude());
                    tloc.setTime(System.currentTimeMillis());
                    loc = tloc;

                    String city = currentLocation.getCity();


                    if (!CheckUtil.isEmpty(city)) {

                        if (city.contains("市")) {
                            String[] cityarray = city.split("市");
                            if (cityarray != null && !CheckUtil.isEmpty(cityarray[0])) {
                                city = cityarray[0];
                            } else {
                                city = "上海";
                            }

                        }

                        if (city.contains("特别行政区")) {

                            String[] cityarray = city.split("特");
                            if (cityarray != null && !CheckUtil.isEmpty(cityarray[0])) {
                                city = cityarray[0];
                            }

                        }

                    } else {
                        city = "上海";
                    }


                }

            }
        };
        mLocationClient = new LocationClient(app.getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);


    }


    public static void stop() {
        try {
            mLocationClient.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        try {
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);
            option.setIsNeedAddress(true); //设置是否需要地址信息，默认为无地址wgs84
            option.setCoorType("gcj02");//返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(2000);//设置发起定位请求的间隔时间为5000ms
            option.SetIgnoreCacheException(true);//设置是否进行异常捕捉
//			option.setEnableSimulateGps(true);
//			option.disableCache(true);//禁止启用缓存定位
//			option.setPoiNumber(5);	//最多返回POI个数	
//			option.setPoiDistance(1000); //poi查询距离		
//			option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            mLocationClient.setLocOption(option);
//			myApp.mBMapMan.start();
            mLocationClient.start();
            mLocationClient.requestLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
