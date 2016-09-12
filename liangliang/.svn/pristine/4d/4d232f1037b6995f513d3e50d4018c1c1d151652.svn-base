package cn.chono.yopper.Service.Http.UploadingUserHeadImage;

import android.content.Context;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.Service.OKHttpUtils;

/**
 *  上传用户头像照片
 * Created by zxb on 2015/11/20.
 */
public class UploadingUserHeadImgService extends HttpService {

    private UploadingUserHeadImgBean iBean;

    public UploadingUserHeadImgService(Context context) {
        super(context);
    }

    @Override
    public void enqueue() {
        OutDataClass = UploadingUserHeadImgRespBean.class;
        String filePath=iBean.getFilePath();
        boolean saveToAlbum=iBean.isSaveToAlbum();
        String url= HttpURL.user_headimg+"SaveToAlbum=" + saveToAlbum;
        callWrap= OKHttpUtils.uploading(context,url,filePath,okHttpListener);



    }

    @Override
    public void parameter(ParameterBean iBean) {
        this.iBean = (UploadingUserHeadImgBean) iBean;


    }
}
