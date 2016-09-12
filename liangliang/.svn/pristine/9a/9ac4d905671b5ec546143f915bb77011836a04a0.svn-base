package cn.chono.yopper.Service.Http.UploadingUserImage;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/22.
 */
public class UploadingUserImageService extends HttpService {
    public UploadingUserImageService(Context context) {
        super(context);
    }

    private UploadingUserImageBean imageBean;

    @Override
    public void enqueue() {
        OutDataClass = UploadingUserImageRespBean.class;

        callWrap = OKHttpUtils.uploading(context, HttpURL.user_image, imageBean.getFilePath(), okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        imageBean = (UploadingUserImageBean) iBean;
    }
}
