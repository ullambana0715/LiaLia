package cn.chono.yopper.Service.Http.UploadingChatImage;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 * 聊天图片上传
 * Created by zxb on 2015/11/21.
 */
public class UploadingChatImgService extends HttpService {
    public UploadingChatImgService(Context context) {
        super(context);
    }

    private  UploadingChatImgBean imgBean;
    @Override
    public void enqueue() {
        OutDataClass=UploadingChatImgRespBean.class;
        callWrap= OKHttpUtils.uploading(context, HttpURL.upload_chat_image,imgBean.getFilePath(),okHttpListener);
    }

    @Override
    public void parameter(ParameterBean iBean) {
        imgBean= (UploadingChatImgBean) iBean;
    }
}
