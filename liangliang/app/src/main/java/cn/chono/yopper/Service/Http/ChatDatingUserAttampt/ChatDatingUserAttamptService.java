package cn.chono.yopper.Service.Http.ChatDatingUserAttampt;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class ChatDatingUserAttamptService extends HttpService {
    public ChatDatingUserAttamptService(Context context) {
        super(context);
    }

    private ChatDatingUserAttamptBean attamptBean;

    @Override
    public void enqueue() {
        OutDataClass = ChatDatingUserAttamptRespBean.class;
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("targetUserId", attamptBean.getTargetUserId());

        hashMap.put("datingId", attamptBean.getDatingId());

        callWrap = OKHttpUtils.post(context, HttpURL.v2_chat_dating_user_attampt, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        attamptBean = (ChatDatingUserAttamptBean) iBean;
    }
}
