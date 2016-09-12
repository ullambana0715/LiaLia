package cn.chono.yopper.Service.Http.DatingPublish;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/3/31.
 */
public class DatingsService extends HttpService {
    public DatingsService(Context context) {
        super(context);
    }


    private DatingsBean datingsBean;

    @Override
    public void enqueue() {

        OutDataClass = DatingsRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("lat", datingsBean.getLat());

        hashMap.put("lng", datingsBean.getLng());

        hashMap.put("firstArea",datingsBean.getFirstArea());

        hashMap.put("secondArea",datingsBean.getSecondArea());

        hashMap.put("activityType", datingsBean.getActivityType());

        if (null != datingsBean.getMarriage()) {
            hashMap.put("marriage", datingsBean.getMarriage());
        }
        if (null != datingsBean.getTravel()) {
            hashMap.put("travel", datingsBean.getTravel());
        }
        if (null != datingsBean.getDine()) {
            hashMap.put("dine", datingsBean.getDine());
        }
        if (null != datingsBean.getMovie()) {
            hashMap.put("movie", datingsBean.getMovie());
        }
        if (null != datingsBean.getSports()) {
            hashMap.put("sports", datingsBean.getSports());
        }
        if (null != datingsBean.getWalkTheDog()) {
            hashMap.put("walkTheDog", datingsBean.getWalkTheDog());
        }

        if (null != datingsBean.getSinging()) {
            hashMap.put("singing", datingsBean.getSinging());
        }
        if (null != datingsBean.getOther()) {
            hashMap.put("other", datingsBean.getOther());
        }

        callWrap = OKHttpUtils.post(context, HttpURL.datings, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        datingsBean = (DatingsBean) iBean;


    }
}
