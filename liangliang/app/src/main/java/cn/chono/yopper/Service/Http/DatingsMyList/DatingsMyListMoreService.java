package cn.chono.yopper.Service.Http.DatingsMyList;

import android.content.Context;

import cn.chono.yopper.Service.Http.DatingsList.DatingListRespBean;
import cn.chono.yopper.Service.Http.DatingsListMore.DatingListMoreBean;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/22.
 */
public class DatingsMyListMoreService extends HttpService {
    public DatingsMyListMoreService(Context context) {
        super(context);
    }

    private DatingsMyListMoreBean nearestsBean;

    @Override
    public void enqueue() {

        OutDataClass = DatingListRespBean.class;

        String url = nearestsBean.getNextQuery();
        callWrap = OKHttpUtils.get(context, url, okHttpListener);
    }

    
    @Override
    public void parameter(ParameterBean iBean) {

    }

}
