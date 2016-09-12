package cn.chono.yopper.Service.Http.DatingsAttemptHandle;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.DatingsClose.DatingsCloseRespBean;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;


public class DatingsAttemptService extends HttpService {

    public DatingsAttemptService(Context context) {
        super(context);
    }

    private DatingsAttemptBean bean;

    @Override
    public void enqueue() {
        OutDataClass = DatingsAttempRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("targetUserId", bean.getTargetUserId());
        hashMap.put("datingId", bean.getDatingId());

        hashMap.put("type", bean.getType());

        String url = HttpURL.dating_attempt_handle;

        callWrap = OKHttpUtils.put(context, url, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        bean = (DatingsAttemptBean) iBean;
    }
}
