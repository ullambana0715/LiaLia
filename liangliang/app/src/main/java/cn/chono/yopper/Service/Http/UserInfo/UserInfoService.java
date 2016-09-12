package cn.chono.yopper.Service.Http.UserInfo;

import android.content.Context;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * 获取用户信息及相册信息
 * Created by zxb on 2015/11/20.
 */
public class UserInfoService extends HttpService {
    public UserInfoService(Context context) {
        super(context);
    }

    private UserInfoBean infoBean;

    @Override
    public void enqueue() {
        OutDataClass = UserInfoRespBean.class;

        String url = HttpURL.get_user_info_album + infoBean.getUserId() + "/profile?";

        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("NewAlbum", infoBean.isNewAlbum());
        HashMap.put("Wish", infoBean.isWish());
        HashMap.put("Bubble", infoBean.isBubble());
        HashMap.put("Dating", infoBean.isDating());
        HashMap.put("Verification", infoBean.isVerification());
        HashMap.put("FaceRating", infoBean.isFaceRating());
        HashMap.put("Friends", infoBean.isFriends());

        double Lng = infoBean.getLng();
        if (-1 != Lng) HashMap.put("Lng", Lng);

        double Lat = infoBean.getLat();
        if (-1 != Lat) HashMap.put("Lat", Lat);

        callWrap = OKHttpUtils.get(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        infoBean = (UserInfoBean) iBean;

    }
}
