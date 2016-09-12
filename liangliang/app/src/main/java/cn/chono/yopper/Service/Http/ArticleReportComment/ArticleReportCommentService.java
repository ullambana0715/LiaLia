package cn.chono.yopper.Service.Http.ArticleReportComment;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 评论回复
 * Created by cc on 16/2/25.
 */
public class ArticleReportCommentService extends HttpService{

    public ArticleReportCommentService(Context context) {
        super(context);
    }



    private  ArticleReportCommentBean reportCommentBean;

    @Override
    public void enqueue() {

        OutDataClass = RespBean.class;

        String url= HttpURL.article_commentOtherComment+"/"+reportCommentBean.getCommentId()+"/report";

        callWrap= OKHttpUtils.put(context,url,null,okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        reportCommentBean= (ArticleReportCommentBean) iBean;


    }
}
