package cn.chono.yopper.Service.Http.FeedBack;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 意思反馈
 * Created by zxb on 2015/11/22.
 */
public class FeedBackService extends HttpService {
    public FeedBackService(Context context) {
        super(context);
    }

    private FeedBackBean backBean;

    @Override
    public void enqueue() {
        OutDataClass = FeedBackRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("text", backBean.getText());

        callWrap = OKHttpUtils.post(context, HttpURL.feedback, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        backBean = (FeedBackBean) iBean;
    }
}
