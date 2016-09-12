package cn.chono.yopper.Service.Http.WebHttpService;

import com.andbase.tractor.http.HttpBase;
import com.andbase.tractor.http.OKHttp;
import com.andbase.tractor.listener.LoadListener;

/**
 * Created by zxb on 2016/1/7.
 */
public class WebHttpService {

    public void enqueue( String url, LoadListener okHttpListener) {
        HttpBase mHttpBase = new OKHttp();


        mHttpBase.get(url, okHttpListener);

    }

}
