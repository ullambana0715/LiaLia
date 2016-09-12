package cn.chono.yopper.Service.Http.CounselorsListMore;

import android.content.Context;

import cn.chono.yopper.Service.Http.CounselorList.CounselorsRespEntity;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * Created by cc on 16/5/3.
 */
public class CounselorsListMoreService extends HttpService {
    public CounselorsListMoreService(Context context) {
        super(context);
    }

    CounselorsListMoreBean mCounselorsListMoreBean;

    @Override
    public void enqueue() {

        OutDataClass= CounselorsRespEntity.class;

        OKHttpUtils.get(context,mCounselorsListMoreBean.nextQuery,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        mCounselorsListMoreBean= (CounselorsListMoreBean) iBean;
    }
}
