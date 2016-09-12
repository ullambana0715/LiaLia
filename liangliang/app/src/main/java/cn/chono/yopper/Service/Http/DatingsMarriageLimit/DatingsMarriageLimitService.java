package cn.chono.yopper.Service.Http.DatingsMarriageLimit;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/4/6.
 */
public class DatingsMarriageLimitService extends HttpService {

    public DatingsMarriageLimitService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {

        OutDataClass = DatingsMarriageLimitRespBean.class;

        callWrap = OKHttpUtils.get(context, HttpURL.dating_marriageLimit, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {

    }
}
