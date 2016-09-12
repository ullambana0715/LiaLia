package cn.chono.yopper.Service.Http.ClimbingRank;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/3/30.
 */
public class ClimbRankService extends HttpService {

    private ClimbRankReqBean reqBean;
    public ClimbRankService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = ClimbRankRespBean.class;

        HashMap<String , Object> hashMap = new HashMap<>();

        callWrap = OKHttpUtils.post(context, HttpURL.climb_rank,hashMap,okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        reqBean = (ClimbRankReqBean)iBean;
    }
}
