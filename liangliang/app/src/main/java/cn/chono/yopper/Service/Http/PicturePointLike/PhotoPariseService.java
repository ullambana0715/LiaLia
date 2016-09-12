package cn.chono.yopper.Service.Http.PicturePointLike;

import android.content.Context;
import java.util.HashMap;
import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by jianghua on 2016/3/18.
 */
public class PhotoPariseService extends HttpService {


    public PhotoPariseService(Context context) {
        super(context);
    }

    private PhotoPariseBean pariseBean;

    @Override
    public void enqueue() {

        OutDataClass = PhotoPariseRespBean.class;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("imagePath", pariseBean.getImagePath());
        hashMap.put("isCancel", pariseBean.getIsCancel());

        callWrap = OKHttpUtils.put(context,HttpURL.parise_photo,hashMap,okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        pariseBean = (PhotoPariseBean)iBean;
    }
}
