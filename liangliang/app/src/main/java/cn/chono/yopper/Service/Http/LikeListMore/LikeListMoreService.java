package cn.chono.yopper.Service.Http.LikeListMore;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.LikeList.LikeListRespBean;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 *
 */
public class LikeListMoreService extends HttpService {
    public LikeListMoreService(Context context) {
        super(context);
    }

    private LikeListMoreBean listMoreBean;

    @Override
    public void enqueue() {
        OutDataClass = LikeListRespBean.class;

        callWrap = OKHttpUtils.get(context, listMoreBean.getNextQuery(), okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        listMoreBean = (LikeListMoreBean) iBean;
    }
}
