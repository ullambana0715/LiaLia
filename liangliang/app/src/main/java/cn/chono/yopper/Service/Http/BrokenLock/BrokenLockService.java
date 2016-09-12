package cn.chono.yopper.Service.Http.BrokenLock;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/21.
 */
public class BrokenLockService extends HttpService {
    public BrokenLockService(Context context) {
        super(context);
    }

    private BrokenLockBean bean;

    @Override
    public void enqueue() {
        OutDataClass=BrokenLockRespBean.class;


        String  url = HttpURL.unlock +bean.getOtherUserId();

        callWrap= OKHttpUtils.post(context,url,null,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

        bean= (BrokenLockBean) iBean;
    }
}


