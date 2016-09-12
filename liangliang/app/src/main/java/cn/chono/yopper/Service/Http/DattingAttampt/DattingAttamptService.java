package cn.chono.yopper.Service.Http.DattingAttampt;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 约TA
 * Created by zxb on 2015/11/21.
 */
public class DattingAttamptService extends HttpService{
    public DattingAttamptService(Context context) {
        super(context);
    }

    private DattingAttamptBean attamptBean;

    @Override
    public void enqueue() {
        OutDataClass=DattingAttamptRespBean.class;
        HashMap<String,Object> HashMap=new HashMap<>();

        HashMap.put("targetUserId", attamptBean.getTargetUserId());
        HashMap.put("confirm", attamptBean.isConfirm());

        DattingAttamptBean.Type type=attamptBean.getType();


        if (null !=type){
            HashMap.put("datingType", type.getDatingType());
            HashMap.put("locationId", type.getLocationId());
            HashMap.put("locationIndex",type.getLocationIndex());
        }

        callWrap= OKHttpUtils.post(context, HttpURL.v2_dating_attampt,HashMap,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        attamptBean= (DattingAttamptBean) iBean;
    }
}
