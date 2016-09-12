package cn.chono.yopper.api;

/**
 * Created by SQ on 16/7/12.
 */
public class HttpFactory {

    private static HttpApi mHttpApi = null;
    private static final Object WATCH = new Object();

    public HttpFactory() {

    }

    public static HttpApi getHttpApi() {
        synchronized (WATCH) {
            if (mHttpApi == null) {
                mHttpApi = new HttpClient().getHttpApi();
            }
        }
        return mHttpApi;
    }
}
