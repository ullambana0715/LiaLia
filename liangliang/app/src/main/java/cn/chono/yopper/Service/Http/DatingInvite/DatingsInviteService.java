package cn.chono.yopper.Service.Http.DatingInvite;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/3/31.
 */
public class DatingsInviteService extends HttpService {
    public DatingsInviteService(Context context) {
        super(context);
    }


    private DatingsInviteBean datingsInviteBean;

    @Override
    public void enqueue() {

        OutDataClass = DatingsInviteRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("dateId", datingsInviteBean.getDateId());

        hashMap.put("targetUserId", datingsInviteBean.getTargetUserId());

        callWrap = OKHttpUtils.post(context, HttpURL.invite_datings, hashMap, okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        datingsInviteBean = (DatingsInviteBean) iBean;


    }
}
