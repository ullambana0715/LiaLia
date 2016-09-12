package cn.chono.yopper.Service.Http.SubmitDatingPurpose;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class SubmitDatingPurposeService extends HttpService {

    public SubmitDatingPurposeService(Context context) {
        super(context);
    }

    private SubmitDatingPurposeBean purposeBean;

    @Override
    public void enqueue() {
        OutDataClass = SubmitDatingPurposeRespBean.class;

        String url = HttpURL.change_dating_purpose + purposeBean.getUserId();
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("purpose", purposeBean.getPurpose());
        callWrap = OKHttpUtils.put(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        purposeBean = (SubmitDatingPurposeBean) iBean;
    }
}
