package cn.chono.yopper.Service.Http.DatingsMyList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.DatingsList.DatingListRespBean;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/22.
 */
public class DatingsMyListService extends HttpService {
    public DatingsMyListService(Context context) {
        super(context);
    }


    @Override
    public void enqueue() {

        OutDataClass = DatingListRespBean.class;

        callWrap = OKHttpUtils.get(context, HttpURL.my_dating_list, okHttpListener);
    }


    @Override
    public void parameter(ParameterBean iBean) {

    }

}
