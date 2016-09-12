package cn.chono.yopper.api;

import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.HttpURL;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SQ on 16/7/12.
 */
public class HttpClient {

    private final HttpApi mHttpApi;

    private String mBase_url;

    public HttpClient() {

        if (YpSettings.isTest) {
            mBase_url = HttpURL.Test_URL;
        } else {
            mBase_url = HttpURL.URL;
        }

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(mBase_url)
                .client(OkHttpManager.getInstance())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mHttpApi = retrofit1.create(HttpApi.class);
    }

    public HttpApi getHttpApi() {
        return mHttpApi;
    }
}
