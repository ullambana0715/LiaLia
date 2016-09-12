package cn.chono.yopper.Service.Http.InviteType;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 邀请
 * Created by zxb on 2015/11/22.
 */
public class InviteTypeService extends HttpService {
    public InviteTypeService(Context context) {
        super(context);
    }

    private InviteTypeBean typeBean;

    @Override
    public void enqueue() {

        OutDataClass = InviteTypeRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();

        String url = HttpURL.v2_invite + "InviteeId=" + typeBean.getInviteeId() + "&InviteType=" + typeBean.getInviteType() + "&Confirm=" + typeBean.isConfirm();

        callWrap = OKHttpUtils.post(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        typeBean = (InviteTypeBean) iBean;
    }
}
