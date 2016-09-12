package cn.chono.yopper.Service.Http.DatingRequirment;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

public class DatingRequirmentService extends HttpService {
    public DatingRequirmentService(Context context) {
        super(context);
    }


    @Override
    public void enqueue() {

        OutDataClass = DatingRequirmentRespBean.class;


        callWrap = OKHttpUtils.get(context, HttpURL.appointments_requirements, okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {

    }

}
