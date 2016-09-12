package cn.chono.yopper.Service.Http.UploadingOrderImage;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/22.
 */
public class UploadingOrderImageService extends HttpService {
    public UploadingOrderImageService(Context context) {
        super(context);
    }

    private UploadingOrderImageBean imageBean;

    @Override
    public void enqueue() {
        OutDataClass = UploadingOrderImageRespBean.class;

        callWrap = OKHttpUtils.uploading(context, HttpURL.orders_image, imageBean.getFilePath(), okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        imageBean = (UploadingOrderImageBean) iBean;
    }
}
