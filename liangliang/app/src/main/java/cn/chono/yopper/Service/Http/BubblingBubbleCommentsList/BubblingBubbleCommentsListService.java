package cn.chono.yopper.Service.Http.BubblingBubbleCommentsList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 评论列表
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubbleCommentsListService extends HttpService {
    public BubblingBubbleCommentsListService(Context context) {
        super(context);
    }

    private BubblingBubbleCommentsListBean commentsBean;

    @Override
    public void enqueue() {
        OutDataClass = BubblingBubbleCommentsListRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("id", commentsBean.getId());
        HashMap.put("start", commentsBean.getStart());
        HashMap.put("rows", commentsBean.getRows());

        String url = HttpURL.bubbling_bubble + "/" + commentsBean.getId() + "/comments?";

        callWrap = OKHttpUtils.get(context, url, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        commentsBean = (BubblingBubbleCommentsListBean) iBean;
    }
}
