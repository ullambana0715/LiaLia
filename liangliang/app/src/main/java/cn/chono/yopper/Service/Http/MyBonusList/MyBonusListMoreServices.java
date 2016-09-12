package cn.chono.yopper.Service.Http.MyBonusList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 16/3/24.
 */
public class MyBonusListMoreServices extends HttpService {

    private MyBonusListMoreReqBean myBonusListReqBean;

    public MyBonusListMoreServices(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = MyBonusListRespBean.class;

        HashMap<String, Object> objectHashMap = new HashMap<>();

        String url = myBonusListReqBean.getNextQuery();

        callWrap = OKHttpUtils.get(context, url, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        myBonusListReqBean = (MyBonusListMoreReqBean) iBean;
    }
}
