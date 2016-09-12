package cn.chono.yopper.Service.Http.WorkDatetimes;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 获取时间列表【塔罗师预约】
 * Created by cc on 16/5/3.
 */
public class WorkTimesService extends HttpService {
    public WorkTimesService(Context context) {
        super(context);
    }


    WorkDatetimesBean mWorkDatetimesBean;

    @Override
    public void enqueue() {

        OutDataClass = WorkDatetimesEntity.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("counselorType", mWorkDatetimesBean.counselorType);


        String url = HttpURL.counselors + "/" + mWorkDatetimesBean.userId + "/workDatetimes?";

        OKHttpUtils.get(context, url, hashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

        mWorkDatetimesBean = (WorkDatetimesBean) iBean;

    }
}
