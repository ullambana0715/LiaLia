package cn.chono.yopper.Service.Http.PublishDating;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by zxb on 2015/11/21.
 */
public class PublishDatingService extends HttpService {
    public PublishDatingService(Context context) {
        super(context);
    }

    private PublishDatingBean datingBean;

    @Override
    public void enqueue() {


        OutDataClass = PublishDatingRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("datingType", datingBean.getDatingType());
        HashMap.put("locationId", datingBean.getLocationId());
        HashMap.put("meetingDate", datingBean.getMeetingDate());
        HashMap.put("inviteeId", datingBean.getInviteeId());

        callWrap = OKHttpUtils.post(context, HttpURL.publish_dating, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        datingBean = (PublishDatingBean) iBean;
    }
}
