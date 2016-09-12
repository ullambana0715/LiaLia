package cn.chono.yopper.Service.Http.DatingsList;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.HashMap;
import java.util.List;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.base.App;
import cn.chono.yopper.data.AppointListDto;
import cn.chono.yopper.data.AppointmentDto;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.Profile;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.HttpURL;
import cn.chono.yopper.utils.JsonUtils;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingListService extends HttpService {
    public DatingListService(Context context) {
        super(context);
    }

    private DatingListBean nearestBean;

    @Override
    public void enqueue() {
        OutDataClass = DatingListRespBean.class;

        HashMap<String, Object> HashMap = new HashMap<>();

        HashMap.put("Lng", nearestBean.getLng());
        HashMap.put("Lat", nearestBean.getLat());

        HashMap.put("Sex", nearestBean.getSex());

        HashMap.put("Sort", nearestBean.getSort());

        HashMap.put("ActivityType", nearestBean.getType());


        if (!CheckUtil.isEmpty(nearestBean.getFirstArea())) {

            HashMap.put("FirstArea", nearestBean.getFirstArea());

        }


        if (nearestBean.getStart() != 0) {
            HashMap.put("Start", nearestBean.getStart());
        }


        callWrap = OKHttpUtils.get(context, HttpURL.dating_list, HashMap, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearestBean = (DatingListBean) iBean;
    }


    @Override
    protected void onCallSucceed(RespBean respBean) {
        DatingListRespBean datingListRespBean = (DatingListRespBean) respBean;
        AppointListDto appointListDto = datingListRespBean.getResp();
        try {
            if (appointListDto != null) {
                List<AppointmentDto> list = appointListDto.getList();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        AppointmentDto dto = list.get(i);
                        UserInfo loginUserInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", dto.getOwner().getUserId()));
                        if (loginUserInfo != null) {
                            UserDto userInfo = JsonUtils.fromJson(loginUserInfo.getResp(), UserDto.class);
                            userInfo.getProfile().setName(dto.getOwner().getName());
                            userInfo.getProfile().setHeadImg(dto.getOwner().getHeadImg());
                            userInfo.getProfile().setSex(dto.getOwner().getSex());
                            userInfo.getProfile().setHoroscope(dto.getOwner().getHoroscope());
                            String str = JsonUtils.toJson(userInfo);
                            loginUserInfo.setResp(str);
                            App.getInstance().db.update(loginUserInfo);

                        } else {
                            loginUserInfo = new UserInfo();
                            loginUserInfo.setId(dto.getOwner().getUserId());
                            UserDto userDto = new UserDto();
                            Profile profile = new Profile();
                            profile.setName(dto.getOwner().getName());
                            profile.setId(dto.getOwner().getUserId());
                            profile.setHeadImg(dto.getOwner().getHeadImg());
                            profile.setHoroscope(dto.getOwner().getHoroscope());
                            profile.setSex(dto.getOwner().getSex());
                            userDto.setProfile(profile);
                            String str = JsonUtils.toJson(userDto);
                            loginUserInfo.setResp(str);
                            App.getInstance().db.save(loginUserInfo);

                        }
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
