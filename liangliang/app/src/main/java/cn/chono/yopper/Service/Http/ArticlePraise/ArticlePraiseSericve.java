package cn.chono.yopper.Service.Http.ArticlePraise;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/2/25.
 */
public class ArticlePraiseSericve extends HttpService {
    public ArticlePraiseSericve(Context context) {
        super(context);
    }

    private ArticlePraiseBean articlePraiseBean;

    @Override
    public void enqueue() {
        OutDataClass=ArticlePraiseRespBean.class;

        String url=HttpURL.get_article_comments+articlePraiseBean.getId()+"/praise";

        callWrap= OKHttpUtils.get(context, url,okHttpListener);




    }

    @Override
    public void parameter(ParameterBean iBean) {

        articlePraiseBean= (ArticlePraiseBean) iBean;

    }
}
