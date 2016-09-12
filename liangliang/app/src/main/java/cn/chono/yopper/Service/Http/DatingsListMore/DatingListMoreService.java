package cn.chono.yopper.Service.Http.DatingsListMore;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import cn.chono.yopper.Service.Http.DatingsList.DatingListRespBean;
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
import cn.chono.yopper.utils.JsonUtils;

/**
 * Created by zxb on 2015/11/21.
 */
public class DatingListMoreService extends HttpService {
    public DatingListMoreService(Context context) {
        super(context);
    }

    private DatingListMoreBean nearestsBean;

    @Override
    public void enqueue() {
        OutDataClass = DatingListRespBean.class;

        String url = nearestsBean.getNextQuery();
        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        nearestsBean = (DatingListMoreBean) iBean;
    }

    @Override
    protected void onCallSucceed(RespBean respBean) {

        DatingListRespBean daingsNearestsRespBean = (DatingListRespBean) respBean;
        AppointListDto appointListDto = daingsNearestsRespBean.getResp();
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
        }catch (DbException e) {
            e.printStackTrace();
        }
    }
}
