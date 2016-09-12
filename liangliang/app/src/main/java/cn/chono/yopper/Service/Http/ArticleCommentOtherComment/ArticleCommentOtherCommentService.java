package cn.chono.yopper.Service.Http.ArticleCommentOtherComment;

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
public class ArticleCommentOtherCommentService extends HttpService{

    public ArticleCommentOtherCommentService(Context context) {
        super(context);
    }



    private  ArticleCommentOtherCommentBean commentOtherCommentBean;

    @Override
    public void enqueue() {

        OutDataClass = RespBean.class;

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("commentId",commentOtherCommentBean.getCommentId());
        hashMap.put("content",commentOtherCommentBean.getContent());

        String url= HttpURL.article_commentOtherComment+"/"+commentOtherCommentBean.getCommentId();

        callWrap= OKHttpUtils.post(context,url,hashMap,okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        commentOtherCommentBean= (ArticleCommentOtherCommentBean) iBean;


    }
}
