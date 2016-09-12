package cn.chono.yopper.Service.Http.UploadingDatingsImage;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.UploadingUserImage.UploadingUserImageBean;
import cn.chono.yopper.Service.Http.UploadingUserImage.UploadingUserImageRespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/22.
 */
public class UploadingDatingsImageService extends HttpService {
    public UploadingDatingsImageService(Context context) {
        super(context);
    }

    private UploadingDatingsImageBean imageBean;

    @Override
    public void enqueue() {
        OutDataClass = UploadingDatingsImageRespBean.class;

        callWrap = OKHttpUtils.uploading(context, HttpURL.dating_image, imageBean.getFilePath(), okHttpListener);


    }

    @Override
    public void parameter(ParameterBean iBean) {
        imageBean = (UploadingDatingsImageBean) iBean;
    }
}
