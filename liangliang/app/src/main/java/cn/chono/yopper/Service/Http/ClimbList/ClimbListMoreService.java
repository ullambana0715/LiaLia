package cn.chono.yopper.Service.Http.ClimbList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 16/3/29.
 */
public class ClimbListMoreService extends HttpService {

    private ClimbListMoreReqBean climbListReqBean;

    public ClimbListMoreService(Context context){
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ClimbListRespBean.class;

        String nextQuery = climbListReqBean.getNextQuery();

        callWrap = OKHttpUtils.get(context, nextQuery, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        climbListReqBean = (ClimbListMoreReqBean)iBean;
    }
}
