package cn.chono.yopper.Service.Http.CounselorList;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by cc on 16/5/3.
 */
public class CounselorsListService extends HttpService {

    public CounselorsListService(Context context) {
        super(context);
    }


    CounselorsBean mCounselorsBean;

    @Override
    public void enqueue() {

        OutDataClass=CounselorsRespEntity.class;

        String url= HttpURL.counselors+"?counselorType="+mCounselorsBean.counselorType;


        OKHttpUtils.get(context,url,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mCounselorsBean= (CounselorsBean) iBean;
    }
}
