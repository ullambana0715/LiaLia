package cn.chono.yopper.Service.Http.ProfileUser;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.utils.HttpURL;

/**
 * Created by zxb on 2015/11/22.
 */
public class ProfileUserService extends HttpService {
    public ProfileUserService(Context context) {
        super(context);
    }

    private ProfileUserBean userBean;

    @Override
    public void enqueue() {
        OutDataClass = ProfileUserRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();

        Integer age = userBean.getAge();


        Integer relationship = userBean.getRelationship();
        Integer height = userBean.getHeight();
        Integer weight = userBean.getWeight();
        Integer incomeLevel = userBean.getIncomeLevel();
        String career = userBean.getCareer();
        String tags = userBean.getTags();
        String name=userBean.getName();
        if (null != age) HashMap.put("age",age);

        if (null!= relationship) HashMap.put("relationship", relationship);


        if (null!= height) HashMap.put("height", height);


        if (null!= weight) HashMap.put("weight", weight);


        if (null!= incomeLevel) HashMap.put("incomeLevel", incomeLevel);


        if (!TextUtils.isEmpty(career)) HashMap.put("career", career);


        if (!TextUtils.isEmpty(tags)) HashMap.put("tags", tags);

        if (! TextUtils.isEmpty(name)){//当name不为时，是用户详情页提交的修改信息，上 面的是添加身高，体重等信息

            HashMap.put("age", age);
            HashMap.put("relationship", relationship);
            HashMap.put("height", height);
            HashMap.put("weight", weight);
            HashMap.put("incomeLevel", incomeLevel);
            HashMap.put("career", career);
            HashMap.put("tags", tags);

            HashMap.put("name", name);
            HashMap.put("birthdayPrivacy", userBean.isBirthdayPrivacy());
            HashMap.put("likes", userBean.getLikes());
            HashMap.put("dislikes", userBean.getDislikes());
            HashMap.put("headImg", userBean.getHeadImg());
            HashMap.put("hometown", userBean.getHometown());
            HashMap.put("album", userBean.getAlbum());


        }


        String url = HttpURL.profile_user + userBean.getUserId() + "/profile";

        callWrap = OKHttpUtils.put(context, url, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        userBean = (ProfileUserBean) iBean;

    }
}
