package cn.chono.yopper.Service.Http.SubmitVideoUserOnly;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class SubmitVideoUserOnlyService extends HttpService {
    public SubmitVideoUserOnlyService(Context context) {
        super(context);
    }

    private SubmitVideoUserOnlyBean onlyBean;

    @Override
    public void enqueue() {
        OutDataClass = SubmitVideoUserOnlyRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("chatWithVideoUserOnly", onlyBean.isChatWithVideoUserOnly());

        String url = HttpURL.change_dating_purpose + onlyBean.getUserId();

        callWrap = OKHttpUtils.put(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        onlyBean = (SubmitVideoUserOnlyBean) iBean;
    }
}
