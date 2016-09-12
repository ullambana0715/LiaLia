package cn.chono.yopper.Service.Http.MyBonusList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by yangjinyu on 16/3/10.
 */
public class MyBonusListServices extends HttpService{

    private MyBonusListReqBean myBonusListReqBean;
    public MyBonusListServices(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = MyBonusListRespBean.class;

        HashMap<String, Object> objectHashMap = new HashMap<>();

        objectHashMap.put("start",myBonusListReqBean.getStart());

        callWrap = OKHttpUtils.get(context, HttpURL.bonus_list,objectHashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        myBonusListReqBean = (MyBonusListReqBean)iBean;
    }
}
