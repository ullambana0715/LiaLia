package cn.chono.yopper.Service.Http.UploadingBubbleImage;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 上传冒泡图片
 * Created by zxb on 2015/11/22.
 */
public class UploadingBubbleImageService extends HttpService {
    public UploadingBubbleImageService(Context context) {
        super(context);
    }

    private UploadingBubbleImageBean imageBean;

    @Override
    public void enqueue() {
        OutDataClass = UploadingBubbleImageRespBean.class;

        callWrap = OKHttpUtils.uploading(context, HttpURL.bubbling_Image, imageBean.getFilePath(), okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        imageBean = (UploadingBubbleImageBean) iBean;
    }
}
