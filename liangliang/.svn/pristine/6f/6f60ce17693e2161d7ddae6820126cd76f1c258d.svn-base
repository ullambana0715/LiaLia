package com.andbase.tractor.http;

import com.andbase.tractor.listener.LoadListener;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * http请求的基类
 */
public interface HttpBase {
    /**
     * GET请求数据
     *
     * @param url
     * @param listener
     * @param tag
     * @return callWrap可以用来取消这次请求
     */
    public CallWrap get(String url, LoadListener listener, Object... tag);

    /**
     * GET请求数据
     *
     * @param url
     * @param header
     * @param listener
     * @param tag
     * @return
     */
    public CallWrap get(String url, HashMap<String, String> header, LoadListener listener, Object... tag);


    /**
     * POST提交键值对 可设置header
     *
     * @param url
     * @param header
     * @param hashMap
     * @param listener
     * @param tag
     * @return
     */
    public CallWrap post(String url, HashMap<String, String> header, HashMap<String, Object> hashMap,
                         LoadListener listener, Object tag);

    /**
     * 取消请求
     *
     * @param tag
     */
    public void cancel(Object... tag);

    /**
     * 上传图片
     *
     * @param url
     * @param header
     * @param filePath
     * @param listener
     * @param tag
     * @return
     */
    public CallWrap uploading(String url, HashMap<String, String> header, String filePath, LoadListener listener, Object... tag);

    public CallWrap put(String url, HashMap<String, String> header, HashMap<String, Object> params, LoadListener listener, Object... tag);


    public CallWrap delete(String url, HashMap<String, String> header, HashMap<String, Object> params, LoadListener listener, Object... tag);

    public CallWrap download(String url, HashMap<String, String> header, LoadListener listener, Object... tag);

}
