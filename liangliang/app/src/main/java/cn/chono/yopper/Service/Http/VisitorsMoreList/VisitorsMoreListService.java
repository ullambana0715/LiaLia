package cn.chono.yopper.Service.Http.VisitorsMoreList;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.VisitorsList.VisitorsListRespBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/23.
 */
public class VisitorsMoreListService extends HttpService {
    public VisitorsMoreListService(Context context) {
        super(context);
    }

    private VisitorsMoreListBean listBean;

    @Override
    public void enqueue() {

        OutDataClass = VisitorsMoreListRespBean.class;

        callWrap = OKHttpUtils.get(context, listBean.getNextQuery(), okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        listBean = (VisitorsMoreListBean) iBean;
    }
}
