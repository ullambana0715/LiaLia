package cn.chono.yopper.Service.Http.ClimbList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/3/15.
 */
public class ClimbListService extends HttpService {

    private ClimbListReqBean climbListReqBean;

    public ClimbListService(Context context){
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ClimbListRespBean.class;

        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("start",climbListReqBean.getStart());

        callWrap = OKHttpUtils.get(context, HttpURL.climb_ranklist, objectHashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        climbListReqBean = (ClimbListReqBean)iBean;
    }
}
