package cn.chono.yopper.Service.Http.VedioDetail;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class VedioDetailService extends HttpService {
    public VedioDetailService(Context context) {
        super(context);
    }

    private VedioDetailBean detailBean;

    @Override
    public void enqueue() {
        OutDataClass = VedioDetailRespBean.class;
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("VisitorId", detailBean.getVisitorId());
        String url = HttpURL.verification_vedio_detail + detailBean.getUserId() + "?";
        callWrap = OKHttpUtils.get(context, url, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        detailBean = (VedioDetailBean) iBean;
    }
}
