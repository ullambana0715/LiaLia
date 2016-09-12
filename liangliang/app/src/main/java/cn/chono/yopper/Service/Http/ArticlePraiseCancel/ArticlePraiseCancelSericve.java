package cn.chono.yopper.Service.Http.ArticlePraiseCancel;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/2/25.
 */
public class ArticlePraiseCancelSericve extends HttpService {
    public ArticlePraiseCancelSericve(Context context) {
        super(context);
    }

    private ArticlePraiseCancelBean cancelBean;


    @Override
    public void enqueue() {

        HashMap<String, Object> hashMap = new HashMap<>();

//        hashMap.put("isCancel", cancelBean.isCancel());

        String url = HttpURL.get_article_comments + cancelBean.getId() + "/praise?isCancel="+cancelBean.isCancel();

        callWrap = OKHttpUtils.put(context, url, hashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        cancelBean = (ArticlePraiseCancelBean) iBean;
    }
}
