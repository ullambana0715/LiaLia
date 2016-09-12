package cn.chono.yopper.Service.Http.BubblingNearby;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.ISO8601;

/**
 *
 * 附近冒泡
 * Created by zxb on 2015/11/22.
 */
public class BubblingNearbyService extends HttpService {
    public BubblingNearbyService(Context context) {
        super(context);
    }


    private  BubblingNearbyBean nearbyBean;
    @Override
    public void enqueue() {

        OutDataClass=BubblingNearbyRespBean.class;
        HashMap<String,Object> HashMap=new HashMap<>();

        HashMap.put("Lng",nearbyBean.getLng());
        HashMap.put("Lat",nearbyBean.getLat());
        HashMap.put("rows",nearbyBean.getRows());
        HashMap.put("start",nearbyBean.getStart());

        HashMap.put("r",nearbyBean.getR());



        String time= ISO8601.encodeGB(nearbyBean.getTime());
        HashMap.put("time",time);




        String url = HttpURL.bubbling_nearby + "?";

        callWrap= OKHttpUtils.get(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearbyBean= (BubblingNearbyBean) iBean;
    }
}
