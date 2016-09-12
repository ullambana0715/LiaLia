package cn.chono.yopper.Service.Http.ReleaseAppointments;

import android.content.Context;

import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2016/1/4.
 */
public class ReleaseAppointmentsService extends HttpService {
    public ReleaseAppointmentsService(Context context) {
        super(context);
    }

    private ReleaseAppointmentsBean appointmentsBean;

    @Override
    public void enqueue() {
        OutDataClass = ReleaseAppointmentsRespBean.class;

        HashMap<String, Object> linkedHashMap = new HashMap<>();
        LogUtils.e("==========================="+ appointmentsBean.getCity());

        linkedHashMap.put("city", appointmentsBean.getCity());
        linkedHashMap.put("lat", appointmentsBean.getLat());
        linkedHashMap.put("lng", appointmentsBean.getLng());
        linkedHashMap.put("costType",appointmentsBean.getCostType());
        linkedHashMap.put("endTime", appointmentsBean.getEndTime());
        linkedHashMap.put("availableTime", appointmentsBean.getAvailableTime());
        linkedHashMap.put("sexRequired", appointmentsBean.getSexRequired());

        linkedHashMap.put("videoRequired", appointmentsBean.isVideoRequired());
        linkedHashMap.put("activityType",appointmentsBean.getActivityType());

        if(appointmentsBean.getShop()!=null){

            linkedHashMap.put("shop",appointmentsBean.getShop());
        }
        if(appointmentsBean.getMovie()!=null){

            linkedHashMap.put("movie",appointmentsBean.getMovie());
        }

        if(appointmentsBean.getSports()!=null){
            HashMap<String, Object> sportmap = new HashMap<>();
            sportmap.put("address",appointmentsBean.getSports().getAddress());
            sportmap.put("title",appointmentsBean.getSports().getTitle());
            linkedHashMap.put("sports",sportmap);
        }

        if(appointmentsBean.getWalkADog()!=null){

            HashMap<String, Object> dogmap = new HashMap<>();
            if(!CheckUtil.isEmpty(appointmentsBean.getWalkADog().getImg())){
                dogmap.put("img",appointmentsBean.getWalkADog().getImg());
            }
            dogmap.put("address",appointmentsBean.getWalkADog().getAddress());
            linkedHashMap.put("walkADog",dogmap);
        }
        LogUtils.e("==========================="+linkedHashMap.toString());
        String url = HttpURL.appointments_publish;

        callWrap = OKHttpUtils.post(context, url, linkedHashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        appointmentsBean = (ReleaseAppointmentsBean) iBean;
    }
}
