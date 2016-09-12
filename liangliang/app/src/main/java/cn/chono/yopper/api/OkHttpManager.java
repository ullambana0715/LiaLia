package cn.chono.yopper.api;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.chono.yopper.base.App;
import cn.chono.yopper.common.Constants;
import cn.chono.yopper.utils.NetWorkUtil;
import cn.chono.yopper.utils.OkHttpHeader;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by SQ on 16/7/12.
 */
public class OkHttpManager {

    private static OkHttpClient mOkHttpClient;

    public static OkHttpClient getInstance() {

        if (mOkHttpClient == null) {

            synchronized (OkHttpManager.class) {

                if (mOkHttpClient == null) {

//                    String AppName = "yuepeng";// 统一为 yuepeng （注意是小写）
//
//                    String AppVersionName = AppInfo.getInstance().getVersionName();// 客户端版本
//
//                    String PlatformVersion = AppInfo.getInstance().getPhoneVersion();// 手机平台版本
//
//                    String Market = AppInfo.getInstance().getAppMarketId();
//
//
//                    String App_Version = AppName + "-" + AppVersionName + "-android-" + PlatformVersion + "-" + Market;
//
//                    String RequestId = UUIDGenerator.getUUID();
//
//                    String ClientId = AppInfo.getInstance().getMacAddress();
//
//
//                    long Timestamp = TimeUtil.getHundredNanosecond();
//
//                    String SecretKey = "chono_rocks";
//
//                    String AppSign = SHA.encodeSHA1(App_Version + RequestId + ClientId + SHA.encodeSHA1(SecretKey + ClientId) + Timestamp);



                    Interceptor    apikey = chain -> {

                        Request.Builder builder = chain.request().newBuilder();

                        HashMap<String, String> header = OkHttpHeader.addHeader();

                        if (header != null) {

                            for (HashMap.Entry<String, String> entry : header.entrySet()) {

                                builder.addHeader(entry.getKey(), entry.getValue());
                            }
                        }

                        return chain.proceed(builder.build());

                    };

//
//                    if (!TextUtils.isEmpty(LoginUser.getInstance().getAuthToken())) {
//
//                        apikey = chain ->
//                                chain.proceed(chain.request().newBuilder()
//                                        .addHeader("AppSign", AppSign)
//                                        .addHeader("ClientId", ClientId)
//                                        .addHeader("Timestamp", Timestamp + "")
//                                        .addHeader("AppVersion", App_Version)
//                                        .addHeader("RequestId", RequestId)
//                                        .addHeader("AuthToken", LoginUser.getInstance().getAuthToken())
//                                        .build()
//
//                                );
//
//
//                    } else {
//
//                        apikey = chain -> chain.proceed(chain.request().newBuilder()
//                                .addHeader("AppSign", AppSign)
//                                .addHeader("ClientId", ClientId)
//                                .addHeader("Timestamp", Timestamp + "")
//                                .addHeader("AppVersion", App_Version)
//                                .addHeader("RequestId", RequestId)
//                                .build());
//
//                    }


//                    File cacheFile = new File(App.getInstance().getCacheDir(), "cache");
//                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb


                    mOkHttpClient = new OkHttpClient.Builder()
                            .readTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .connectTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .addInterceptor(apikey)
                            .addInterceptor(new OKHTTPLogInterceptor())
                            .addNetworkInterceptor(new HttpCacheInterceptor())
//                            .cache(cache)
                            .build();
                }
            }
        }

        return mOkHttpClient;
    }

    static class OKHTTPLogInterceptor implements Interceptor {


        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            okhttp3.Response response = chain.proceed(chain.request());

            okhttp3.MediaType mediaType = response.body().contentType();

            String content = response.body().string();


            Logger.e(request.toString());

            Logger.json(content);


            if (response.body() != null) {
                // 深坑！
                // 打印body后原ResponseBody会被清空，需要重新设置body
                ResponseBody body = ResponseBody.create(mediaType, content);
                return response.newBuilder().body(body).build();
            } else {
                return response;
            }
        }
    }


    static class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetConnected(App.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();

                Logger.d("no network");

            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetConnected(App.getInstance())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

}
