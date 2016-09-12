package cn.chono.yopper.Service.Http.VisitorsList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/23.
 */
public class VisitorsListService extends HttpService {
    public VisitorsListService(Context context) {
        super(context);
    }

    private VisitorsListBean listBean;

    @Override
    public void enqueue() {
        OutDataClass = VisitorsListRespBean.class;

        String url = HttpURL.visitors_list + listBean.getUserid() + "/visitors?";
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("Start", listBean.getStart());

        callWrap = OKHttpUtils.get(context, url, HashMap, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        listBean = (VisitorsListBean) iBean;
    }
}
