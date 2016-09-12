package cn.chono.yopper.Service.Http.BubblingList;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.BubblingNearby.BubblingNearbyBean;
import cn.chono.yopper.Service.Http.BubblingNearby.BubblingNearbyRespBean;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.data.BubblingList;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.ISO8601;

/**
 *
 * 我的冒泡
 * Created by zxb on 2015/11/22.
 */
public class BubblingListService extends HttpService {
    public BubblingListService(Context context) {
        super(context);
    }


    private BubblingListBean nearbyBean;
    @Override
    public void enqueue() {

        OutDataClass=BubblingListRespBean.class;
        HashMap<String,Object> HashMap=new HashMap<>();

        HashMap.put("Lng",nearbyBean.getLng());
        HashMap.put("Lat",nearbyBean.getLat());
        HashMap.put("rows",nearbyBean.getRows());
        HashMap.put("start",nearbyBean.getStart());

        HashMap.put("r",nearbyBean.getR());



        String time= ISO8601.encodeGB(nearbyBean.getTime());
        HashMap.put("time",time);
        HashMap.put("userId",nearbyBean.getUserId());


        String url = HttpURL.bubbling_list + "?";

        callWrap= OKHttpUtils.get(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearbyBean= (BubblingListBean) iBean;
    }
}
