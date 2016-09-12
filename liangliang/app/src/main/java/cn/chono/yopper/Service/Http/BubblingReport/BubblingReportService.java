package cn.chono.yopper.Service.Http.BubblingReport;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 提交举报
 * Created by zxb on 2015/11/22.
 */
public class BubblingReportService extends HttpService {
    public BubblingReportService(Context context) {
        super(context);
    }

    private BubblingReportBean reportBean;

    @Override
    public void enqueue() {
        OutDataClass = BubblingReportRespBean.class;


        String url = HttpURL.user_report  + "Type=" + reportBean.getType() + "&Id=" + reportBean.getId() + "&Content=" + reportBean.getContent();
        callWrap = OKHttpUtils.post(context, url, new HashMap<String, Object>(), okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        reportBean = (BubblingReportBean) iBean;
    }
}
