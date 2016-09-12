package cn.chono.yopper.Service.Http.BubblingBubbleComments;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 评论列表
 * Created by zxb on 2015/11/21.
 */
public class BubblingBubbleCommentsService extends HttpService {
    public BubblingBubbleCommentsService(Context context) {
        super(context);
    }

    private BubblingBubbleCommentsBean commentsBean;

    @Override
    public void enqueue() {
        OutDataClass = BubblingBubbleCommentsRespBean.class;

        HashMap<String, Object> linkedHashMap = new HashMap<>();
        linkedHashMap.put("comment", commentsBean.getComment());

        BubblingBubbleCommentsBean.ToUserID toUserID=commentsBean.getToUserID();
        if(null!=toUserID){
            linkedHashMap.put("toUserId", toUserID.getToUserId());
        }

        String url = HttpURL.bubbling_bubble + "/" + commentsBean.getId() + "/comment";

        callWrap = OKHttpUtils.post(context, url, linkedHashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        commentsBean = (BubblingBubbleCommentsBean) iBean;
    }
}
