package cn.chono.yopper.Service.Http.CounselorsProfile;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 获取咨询师信息和服务类型及价格
 * Created by cc on 16/5/3.
 */
public class CounselorProfileService extends HttpService {


    public CounselorProfileService(Context context) {
        super(context);
    }

    CounselorsProfileBean mCounselorsProfileBean;

    @Override
    public void enqueue() {

        OutDataClass = CounselorProfileRespEntity.class;

        String url = HttpURL.counselors +"/"+ mCounselorsProfileBean.userId + "/profile?counselorType=" + mCounselorsProfileBean.counselorType;

        OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mCounselorsProfileBean = (CounselorsProfileBean) iBean;
    }
}
