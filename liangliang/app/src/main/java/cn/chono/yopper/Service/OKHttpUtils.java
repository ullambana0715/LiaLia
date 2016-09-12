package cn.chono.yopper.Service;

import com.andbase.tractor.http.CallWrap;
import com.andbase.tractor.http.HttpBase;
import com.andbase.tractor.http.OKHttp;
import com.andbase.tractor.listener.LoadListener;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.OkHttpHeader;

/**
 * Created by zxb on 2015/11/18.
 */
public class OKHttpUtils {



    static HttpBase mHttpBase = new OKHttp();

    /**
     * get请求
     *
     * @param tag            标记，用于方便手动取消请求
     * @param url
     * @param okHttpListener
     * @return
     */
    public static CallWrap get(Object tag, String url, OKHttpListener okHttpListener) {

        return get(tag, url, new HashMap<String, Object>(), okHttpListener);

    }

    /**
     * get请求
     *
     * @param tag            标记，用于方便手动取消请求
     * @param url
     * @param params         ？key==value
     * @param okHttpListener
     * @return
     */
    public static CallWrap get(Object tag, String url, HashMap<String, Object> params, OKHttpListener okHttpListener) {
        HashMap<String, String> header = addHeader();
        return get(tag, url, header, params, okHttpListener);

    }

    /**
     * get请求
     *
     * @param tag            标记，用于方便手动取消请求
     * @param url
     * @param header         请求头
     * @param params         ？key==value
     * @param okHttpListener
     * @return
     */
    public static CallWrap get(Object tag, String url, HashMap<String, String> header, HashMap<String, Object> params, OKHttpListener okHttpListener) {

        String urpParams = getParams(params);
        url = url + urpParams;//拼接url "?"后面的key==value;需要拼接时，原地址一定要带有"?",因为拼接方法不自动生成 "?"
        url = gethostUrl(url);//测试地址还是真实地址
        Logger.e("加个请求的打印======"+url);
        return mHttpBase.get(url, header, getLoadListener(okHttpListener), tag);

    }


    /**
     * 下载文件
     *
     * @param tag
     * @param url
     * @param okHttpListener
     * @return
     */
    public static CallWrap download(Object tag, String url, OKHttpListener okHttpListener) {
        HashMap<String, String> header = addHeader();
        return mHttpBase.download(url, header, getLoadListener(okHttpListener), tag);

    }

    /**
     * post请求
     *
     * @param tag            标记，用于方便手动取消请求
     * @param url
     * @param params
     * @param okHttpListener
     * @return
     */
    public static CallWrap post(Object tag, String url, HashMap<String, Object> params, final OKHttpListener okHttpListener) {
        HashMap<String, String> header = addHeader();
        return post(tag, url, header, params, okHttpListener);
    }

    /**
     * post请求
     *
     * @param tag            标记，用于方便手动取消请求
     * @param url
     * @param header         请求头
     * @param params
     * @param okHttpListener
     * @return
     */

    public static CallWrap post(Object tag, String url, HashMap<String, String> header, HashMap<String, Object> params, final OKHttpListener okHttpListener) {
        url = gethostUrl(url);//测试地址还是真实地址
        Logger.e("加个请求的打印======"+url);
        return mHttpBase.post(url, header, params, getLoadListener(okHttpListener), tag);
    }


    /**
     * 上传图片
     *
     * @param tag
     * @param url
     * @param filePath
     * @param okHttpListener
     * @return
     */
    public static CallWrap uploading(Object tag, String url, String filePath, final OKHttpListener okHttpListener) {
        HashMap<String, String> header = addHeader();
        return uploading(tag, url, header, filePath, okHttpListener);
    }


    public static CallWrap uploading(Object tag, String url, HashMap<String, String> header, String filePath, final OKHttpListener okHttpListener) {
        url = gethostUrl(url);//测试地址还是真实地址
        return mHttpBase.uploading(url, header, filePath, getLoadListener(okHttpListener), tag);
    }

    /**
     * put请求
     *
     * @param tag
     * @param url
     * @param params
     * @param okHttpListener
     * @return
     */

    public static CallWrap put(Object tag, String url,HashMap<String, Object> params, final OKHttpListener okHttpListener) {
        HashMap<String, String> header = addHeader();
        return put(tag, url, header, params, okHttpListener);
    }

    /**
     * put请求
     *
     * @param tag
     * @param url
     * @param header
     * @param params
     * @param okHttpListener
     * @return
     */
    public static CallWrap put(Object tag, String url, HashMap<String, String> header, HashMap<String, Object> params, final OKHttpListener okHttpListener) {
        url = gethostUrl(url);//测试地址还是真实地址
        System.out.println("加个请求的打印======"+url);
        return mHttpBase.put(url, header, params, getLoadListener(okHttpListener), tag);
    }


    /**
     * delete请求
     *
     * @param tag
     * @param url
     * @param okHttpListener
     * @return
     */
    public static CallWrap delete(Object tag, String url, final OKHttpListener okHttpListener) {
        HashMap<String, String> header = addHeader();
        return delete(tag, url, header,new HashMap<String, Object>(), okHttpListener);
    }


    public static CallWrap delete(Object tag, String url, HashMap<String, String> header, HashMap<String, Object> params, final OKHttpListener okHttpListener) {
        url = gethostUrl(url);//测试地址还是真实地址
        return mHttpBase.delete(url, header, params, getLoadListener(okHttpListener), tag);
    }

    private static LoadListener getLoadListener(final OKHttpListener okHttpListener) {


        LoadListener loadListener = new LoadListener() {
            @Override
            public void onStart() {
                if (null != okHttpListener)
                    okHttpListener.onStart();
            }

            @Override
            public void onLoading(Object result) {

                if (null != okHttpListener)
                    okHttpListener.onLoading(result);
            }

            @Override
            public void onFail(Object result) {
                if (null != okHttpListener)
                    okHttpListener.onFail(result);


            }

            @Override
            public void onSuccess(Object result) {

                if (null != okHttpListener)
                    okHttpListener.onSuccess(result);


            }

            @Override
            public void onCancel(Object result) {
                if (null != okHttpListener)
                    okHttpListener.onCancel(result);
            }

            @Override
            public void onTimeout(Object result) {
                if (null != okHttpListener)
                    okHttpListener.onTimeout(result);
            }

            @Override
            public void onCancelClick() {
                if (null != okHttpListener)
                    okHttpListener.onCancelClick();
            }
        };
        return loadListener;
    }

    /**
     * @param @param asyncHttpClient 设定文件
     * @return void 返回类型
     * @throws
     * @Title: httpHeader
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static HashMap<String, String> addHeader() {

//        String AppVersion = AppInfo.getInstance().getVersionName();// 客户端版本
//
//        String PlatformVersion = AppInfo.getInstance().getPhoneVersion();// 平台版本
//
//        String Market = AppInfo.getInstance().getAppMarketId();
//
//        String App_Version ="yuepeng-" + AppVersion + "-android-" + PlatformVersion + "-" + Market;
//
//        String RequestId = UUIDGenerator.getUUID();
//
//        String ClientId = AppInfo.getInstance().getMacAddress();
//
//
//        long Timestamp = TimeUtil.getHundredNanosecond();
//
//        String SecretKey = "chono_rocks";
//
//        String AppSign = SHA.encodeSHA1(App_Version + RequestId + ClientId + SHA.encodeSHA1(SecretKey + ClientId) + Timestamp);
//
//        HashMap<String, String> header = new HashMap<>();
//        header.put("AppSign", AppSign);
//        header.put("ClientId", ClientId);
//        header.put("Timestamp", Timestamp + "");
//        header.put("AppVersion", App_Version);
//        header.put("RequestId", RequestId);
//        if (!TextUtils.isEmpty(LoginUser.getInstance().getAuthToken())) {
//            header.put("AuthToken", LoginUser.getInstance().getAuthToken());
//        }
        return OkHttpHeader.addHeader();
    }


    private static String getParams(HashMap<String, Object> linkedHashMap) {

        StringBuffer result = new StringBuffer();
        if (null != linkedHashMap) {
            for (HashMap.Entry<String, Object> entry : linkedHashMap.entrySet()) {
                if (result.length() > 0) result.append("&");
                result.append(entry.getKey());
                result.append("=");
                result.append(entry.getValue());
            }
        }
        return result.toString();
    }

    /**
     * 控件URL服务器
     *
     * @param url
     * @return
     */
    public static String gethostUrl(String url) {

        if (YpSettings.isTest) {
            url = HttpURL.Test_URL + url;
        } else {
            url = HttpURL.URL + url;
        }
        return url;

    }


}
