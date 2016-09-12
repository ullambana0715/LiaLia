package cn.chono.yopper.Service.Http.DatingsStatusWithTargetUser;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingStatusWithTargetService extends HttpService {
    public DatingStatusWithTargetService(Context context) {
        super(context);
    }

    private DatingStatusWithTargetBean nearestBean;

    @Override
    public void enqueue() {
        OutDataClass = DatingStatusWithTargetRespBean.class;

        String url= HttpURL.get_dating_status+"DatingId="+nearestBean.getDatingId()+"&OtherUserId="+nearestBean.getOtherUserId();

        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearestBean = (DatingStatusWithTargetBean) iBean;
    }

}
