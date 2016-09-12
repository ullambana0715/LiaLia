package cn.chono.yopper.Service.Http.ArticleTalkinjectedService;

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
public class ArticleTalkinjectedService extends HttpService{

    public ArticleTalkinjectedService(Context context) {
        super(context);
    }

    private ArticleTalkinjectedBean articleBean;

    @Override
    public void enqueue() {

        OutDataClass = RespBean.class;

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("content",articleBean.getContent());

        String url= HttpURL.get_article_comments+"/"+articleBean.getId()+"/comment";

        callWrap= OKHttpUtils.post(context,url,hashMap,okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        articleBean= (ArticleTalkinjectedBean) iBean;


    }
}
