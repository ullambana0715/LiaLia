package cn.chono.yopper.Service.Http.ChatReadMsg;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class ChatReadMsgService extends HttpService {
    public ChatReadMsgService(Context context) {
        super(context);
    }

    private ChatReadMsgBean bean;

    @Override
    public void enqueue() {

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("targetUserId", bean.getTargetUserId());

        hashMap.put("fromUserId", bean.getFromUserId());

        hashMap.put("datingId", bean.getDatingId());


        callWrap = OKHttpUtils.put(context, HttpURL.v2_chat_read_msg, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        bean = (ChatReadMsgBean) iBean;
    }
}
