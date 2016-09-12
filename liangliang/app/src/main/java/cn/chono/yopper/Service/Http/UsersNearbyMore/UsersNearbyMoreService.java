package cn.chono.yopper.Service.Http.UsersNearbyMore;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import cn.chono.yopper.Service.Http.HttpService;
import cn.chono.yopper.Service.Http.ParameterBean;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.OKHttpUtils;
import cn.chono.yopper.base.App;
import cn.chono.yopper.data.DiscoverPeopleDto;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.NearPeopleDto;
import cn.chono.yopper.data.Profile;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.utils.JsonUtils;

/**
 * 附近人更多
 * Created by zxb on 2015/11/22.
 */
public class UsersNearbyMoreService extends HttpService {
    public UsersNearbyMoreService(Context context) {
        super(context);
    }


    private UsersNearbyMoreBean usersNearbysBean;

    @Override
    public void enqueue() {
        OutDataClass = UsersNearbyMoreRespBean.class;

        String url = usersNearbysBean.getNextQuery();

        callWrap = OKHttpUtils.get(context, url, okHttpListener);

    }

    @Override
    public void parameter(ParameterBean iBean) {
        usersNearbysBean = (UsersNearbyMoreBean) iBean;
    }

    @Override
    protected void onCallSucceed(RespBean respBean) {
        UsersNearbyMoreRespBean moreRespBean = (UsersNearbyMoreRespBean) respBean;
        DiscoverPeopleDto peropleDto = moreRespBean.getResp();
        try {
            if (peropleDto != null) {
                List<NearPeopleDto> list = peropleDto.getList();
                if (list != null && list.size() > 0) {// 列表有数据
                    for(int i=0;i<list.size();i++){
                        NearPeopleDto dto= list.get(i);
                        UserInfo loginUserInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =",dto.getId()));
                        if(loginUserInfo!=null){
                            UserDto userInfo = JsonUtils.fromJson(loginUserInfo.getResp(), UserDto.class);
                            userInfo.setDistance(dto.getDistance());
                            userInfo.getProfile().setName(dto.getName());
                            userInfo.getProfile().setHeadImg(dto.getHeadImg());
                            userInfo.getProfile().setSex(dto.getSex());
                            userInfo.getProfile().setHoroscope(dto.getHoroscope());
                            String str=JsonUtils.toJson(userInfo);
                            loginUserInfo.setResp(str);
                            App.getInstance().db.update(loginUserInfo);

                        }else{
                            loginUserInfo = new UserInfo();
                            loginUserInfo.setId(dto.getId());
                            UserDto userDto=new UserDto();
                            userDto.setDistance(dto.getDistance());
                            Profile profile=new Profile();
                            profile.setName(dto.getName());
                            profile.setId(dto.getId());
                            profile.setHeadImg(dto.getHeadImg());
                            profile.setHoroscope(dto.getHoroscope());
                            profile.setSex(dto.getSex());
                            userDto.setProfile(profile);
                            String str=JsonUtils.toJson(userDto);
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