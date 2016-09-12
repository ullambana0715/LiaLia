package cn.chono.yopper.utils;

import android.text.TextUtils;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import cn.chono.yopper.Service.Http.ActivitiesList.ActivityResp;
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileEntity;
import cn.chono.yopper.Service.Http.MyActivitiesList.ActivityRespBean;
import cn.chono.yopper.Service.Http.MyActivitiesList.MyActivityResp;
import cn.chono.yopper.base.App;
import cn.chono.yopper.data.ActivitiesDto;
import cn.chono.yopper.data.AppointDatav3Db;
import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.data.AppointFilterDto;
import cn.chono.yopper.data.BannerDb;
import cn.chono.yopper.data.BaseUser;
import cn.chono.yopper.data.CounselInfoTable;
import cn.chono.yopper.data.CounselOrderStatusTable;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.data.MyActivitiesDto;
import cn.chono.yopper.data.PeopleFilterDto;
import cn.chono.yopper.data.UserInfo;
import cn.chono.yopper.data.LoginVideoStatusDto;
import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.data.UserSelectBannerDb;
import cn.chono.yopper.data.Visits;
import cn.chono.yopper.smack.entity.ChatDto;

/**
 * Created by SQ on 2015/11/24.
 */
public class DbHelperUtils {

    /**
     * 根据id从数据库获取基本的用户信息
     */
    public static BaseUser getBaseUser(int id) {
        BaseUser userDto = null;
        try {
            userDto = App.getInstance().db.findFirst(Selector.from(BaseUser.class).where(" id", " =", id));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return userDto;
    }

    /**
     * 将用户（并不单指登录用户）基本信息保存到数据库
     */
    public static void saveBaseUser(int id, String name, int horoscope, String headImg, int sex, String regTime) {
        try {
            BaseUser userDto = new BaseUser(id, name, horoscope, headImg, sex, regTime);
            App.getInstance().db.save(userDto);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库中用户（并不单指登录用户）基本信息
     */
    public static void updateBaseUser(BaseUser userDto) {
        try {
            App.getInstance().db.update(userDto);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户基本信息数据
     *
     * @param userId
     * @param
     * @return
     */
    public static void saveBaseUser(final int userId, final UserDto userInfo) {
        try {
            BaseUser baseUser = App.getInstance().db.findFirst(Selector.from(BaseUser.class).where("id", " =", userId));

            if (userInfo != null) {
                if (baseUser != null) {

                    baseUser.setHeadImg(userInfo.getProfile().getHeadImg());
                    baseUser.setName(userInfo.getProfile().getName());
                    baseUser.setHoroscope(userInfo.getProfile().getHoroscope());
                    baseUser.setSex(userInfo.getProfile().getSex());

                    App.getInstance().db.update(baseUser);

                } else {
                    baseUser = new BaseUser();
                    baseUser.setId(userId);
                    baseUser.setHeadImg(userInfo.getProfile().getHeadImg());
                    baseUser.setName(userInfo.getProfile().getName());
                    baseUser.setHoroscope(userInfo.getProfile().getHoroscope());
                    baseUser.setSex(userInfo.getProfile().getSex());
                    baseUser.setRegTime(userInfo.getProfile().getRegTime());
                    App.getInstance().db.save(baseUser);
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据id获取用户的性别
     *
     * @param userid
     * @return
     */
    public static int getDbUserSex(int userid) {
        int mysex = 1;
        try {
            UserInfo loginUserInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userid));

            if (loginUserInfo != null) {
                UserDto userInfo = JsonUtils.fromJson(loginUserInfo.getResp(), UserDto.class);
                if (userInfo != null) {
                    mysex = userInfo.getProfile().getSex();
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return mysex;
    }


    /**
     * 根据id获取用户星座
     *
     * @param userid
     * @return
     */
    public static int getDbUserHor(int userid) {
        int hor = 0;
        try {
            UserInfo loginUserInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userid));

            if (loginUserInfo != null) {
                UserDto userInfo = JsonUtils.fromJson(loginUserInfo.getResp(), UserDto.class);
                if (userInfo != null) {
                    hor = userInfo.getProfile().getHoroscope();
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return hor;
    }


    /**
     * 根据id获取用户
     *
     * @param userid
     * @return
     */
    public static UserDto getDbUserInfo(int userid) {
        UserDto dto = null;
        try {
            UserInfo loginUserInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userid));

            if (loginUserInfo != null) {
                UserDto userInfo = JsonUtils.fromJson(loginUserInfo.getResp(), UserDto.class);
                if (userInfo != null) {
                    dto = userInfo;
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return dto;
    }


    /**
     * 保存或更新用户信息数据到数据库
     *
     * @param userId
     * @param data
     * @return
     */
    public static void saveUserInfo(final int userId, final String data) {
        try {
            UserInfo userInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userId));
            if (userInfo != null) {
                userInfo.setId(userId);
                userInfo.setResp(data);
                App.getInstance().db.update(userInfo);
            } else {
                userInfo = new UserInfo();
                userInfo.setId(userId);
                userInfo.setResp(data);
                App.getInstance().db.save(userInfo);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据id从数据库中获取用户信息数据
     *
     * @param userId
     * @return
     */
    public static UserInfo getUserInfo(final int userId) {
        UserInfo userInfo = null;
        try {
            userInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userId));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    public static UserDto getUserDto(final int userId) {
        UserInfo userInfo = null;
        UserDto userdto = null;
        try {
            userInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userId));
            if (userInfo != null) {
                userdto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return userdto;
    }


    public static List<AppointDetailDto> getDatingList(final int userId) {
        UserInfo userInfo = null;
        UserDto userdto = null;
        List<AppointDetailDto> list = null;
        try {
            userInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userId));
            if (userInfo != null) {
                userdto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
                list = userdto.getDatingList();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static CounselorProfileEntity getCounselInfo(final int counselId) {
        CounselInfoTable counselInfo = null;
        CounselorProfileEntity dto = null;
        try {
            counselInfo = App.getInstance().db.findFirst(Selector.from(CounselInfoTable.class).where("id", " =", counselId));
            if (counselInfo != null) {
                dto = JsonUtils.fromJson(counselInfo.getResp(), CounselorProfileEntity.class);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 保存或更新咨询师信息数据到数据库
     *
     * @param counselId
     * @param data
     * @return
     */
    public static void saveCounselInfo(final int counselId, final String data) {
        try {
            CounselInfoTable counselInfo = App.getInstance().db.findFirst(Selector.from(CounselInfoTable.class).where("id", " =", counselId));
            if (counselInfo != null) {
                counselInfo.setId(counselId);
                counselInfo.setResp(data);
                App.getInstance().db.update(counselInfo);
            } else {
                counselInfo = new CounselInfoTable();
                counselInfo.setId(counselId);
                counselInfo.setResp(data);
                App.getInstance().db.save(counselInfo);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存约会数据
     */

    public static void saveAppointV3FilterList(int userId, String results) {

        try {
            if (!CheckUtil.isEmpty(userId + "")) {
                AppointDatav3Db dto = App.getInstance().db.findFirst(Selector.from(AppointDatav3Db.class).where("id", " =", userId));
                if (dto != null) {
                    dto.setAppointListStr(results);
                    App.getInstance().db.update(dto);
                } else {
                    dto = new AppointDatav3Db();
                    dto.setId(userId);
                    dto.setAppointListStr(results);
                    App.getInstance().db.save(dto);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取缓存最新约会数据
     */

    public static String getAppointV3FilterList(int userId) {
        String appointFFilterStr = "";
        try {
            if (!CheckUtil.isEmpty(userId + "")) {
                AppointDatav3Db dto = App.getInstance().db.findFirst(Selector.from(AppointDatav3Db.class).where("id", " =", userId));
                if (dto != null) {
                    appointFFilterStr = dto.getAppointListStr();
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return appointFFilterStr;
    }

    /**
     * 获取约会筛选条件
     */
    public static AppointFilterDto getAppointFilter(int userId) {
        AppointFilterDto dto = null;
        try {
            dto = App.getInstance().db.findFirst(Selector.from(AppointFilterDto.class).where("id", " =", userId));

        } catch (DbException e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 缓存约会筛选条件
     */

    public static void saveAppointFilter(int appointType, int sexType, int sortType, String firstArea, String secondArea) {

        try {
            String id = LoginUser.getInstance().getUserId() + "";
            if (!CheckUtil.isEmpty(id)) {
                AppointFilterDto dto = App.getInstance().db.findFirst(Selector.from(AppointFilterDto.class).where("id", " =", id));
                if (dto != null) {
                    dto.setDatingType(appointType);
                    dto.setSexType(sexType);
                    dto.setFirstArea(firstArea);
                    dto.setSecondArea(secondArea);
                    dto.setSortType(sortType);
                    App.getInstance().db.update(dto);

                } else {
                    dto = new AppointFilterDto();
                    dto.setId(LoginUser.getInstance().getUserId());
                    dto.setSortType(sortType);
                    dto.setDatingType(appointType);
                    dto.setSexType(sexType);
                    dto.setFirstArea(firstArea);
                    dto.setSecondArea(secondArea);


                    App.getInstance().db.save(dto);

                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 数据中获取登录用户的头像通过状态
     */
    public static int getDbLoginUserHeadStatus() {
        int status = 0;
        try {
            UserInfo userInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", LoginUser.getInstance().getUserId()));
            if (userInfo != null) {

                UserDto loginuserdto = JsonUtils.fromJson(userInfo.getResp(), UserDto.class);
                if (loginuserdto != null) {
                    status = loginuserdto.getProfile().getStatus();
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * 保存服务器返回的banner数据
     *
     * @param bannerId
     * @param bannerStr
     */

    public static void saveBannersToDb(String bannerId, String bannerStr) {
        try {
            BannerDb bannerDb = App.getInstance().db.findFirst(Selector.from(BannerDb.class).where("bannerId", " =", bannerId));

            if (bannerDb != null) {
                bannerDb.setRespStr(bannerStr);
                App.getInstance().db.update(bannerDb);

            } else {
                bannerDb = new BannerDb();
                bannerDb.setBannerId(bannerId);
                bannerDb.setRespStr(bannerStr);
                App.getInstance().db.save(bannerDb);

            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    /**
     * 从本地数据库中获取banner数据
     *
     * @param bannerId
     */

    public static String getBannersFromDb(String bannerId) {

        String bannersStr = "";
        try {
            BannerDb bannerDb = App.getInstance().db.findFirst(Selector.from(BannerDb.class).where("bannerId", " =", bannerId));

            if (bannerDb != null) {
                bannersStr = bannerDb.getRespStr();

            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return bannersStr;

    }


    /**
     * 保存用户选择banner数据
     *
     * @param bannerIdAddUserID
     * @param bannerStr
     */

    public static void saveUserBannersToDb(String bannerIdAddUserID, String bannerStr) {
        try {
            UserSelectBannerDb userbannerDb = App.getInstance().db.findFirst(Selector.from(UserSelectBannerDb.class).where("bannerIdAddUserID", " =", bannerIdAddUserID));

            if (userbannerDb != null) {
                userbannerDb.setRespStr(bannerStr);
                App.getInstance().db.update(userbannerDb);

            } else {
                userbannerDb = new UserSelectBannerDb();
                userbannerDb.setBannerIdAddUserID(bannerIdAddUserID);
                userbannerDb.setRespStr(bannerStr);
                App.getInstance().db.save(userbannerDb);

            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    /**
     * 从本地数据库中获取用户选择的banner数据
     *
     * @param bannerIdAddUserID
     */

    public static String getUserBannersFromDb(String bannerIdAddUserID) {

        String bannersStr = "";
        try {
            UserSelectBannerDb userbannerDb = App.getInstance().db.findFirst(Selector.from(UserSelectBannerDb.class).where("bannerIdAddUserID", " =", bannerIdAddUserID));

            if (userbannerDb != null) {
                bannersStr = userbannerDb.getRespStr();

            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return bannersStr;

    }


    /**
     * 从本地数据库中查找是否存在用户与某用户基于某条邀约的聊天记录
     */

    public static boolean isExistChatRecordWithDating(String loginUserId, String userId, String datingId) {

        boolean isExistChatRecord = false;
        try {
            List<ChatDto> recoverlist = App.getInstance().db.findAll(Selector.from(ChatDto.class).where("jid", " =", userId).and("mid", " =", loginUserId).and("datingId", " =", datingId).orderBy("date"));

            if (recoverlist != null && recoverlist.size() > 0) {
                isExistChatRecord = true;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return isExistChatRecord;

    }


    /**
     * 获取咨询订单状态
     */

    public static CounselOrderStatusTable getCounselOrderInfo(String mid, String jid) {

        CounselOrderStatusTable dto = null;

        try {
            dto = App.getInstance().db.findFirst(Selector.from(CounselOrderStatusTable.class).where("mid", " =", mid).and("jid", " =", jid));


        } catch (DbException e) {
            e.printStackTrace();
        }

        return dto;
    }


    /**
     * 保存更新咨询订单状态
     *
     * @param orderId
     * @param status
     */

    public static void saveCounselOrderInfo(String mid, String jid, String hint, String orderId, int status, int counselorType) {
        try {
            CounselOrderStatusTable dto = App.getInstance().db.findFirst(Selector.from(CounselOrderStatusTable.class).where("mid", " =", mid).and("jid", " =", jid));

            if (dto != null) {
                dto.setCounselOrderStatus(status);
                dto.setOrderId(orderId);
                dto.setHint(hint);

                if (counselorType != 0) {
                    dto.setCounselorType(counselorType);
                }


                App.getInstance().db.update(dto);

            } else {
                dto = new CounselOrderStatusTable();
                dto.setMid(mid);
                dto.setJid(jid);
                dto.setHint(hint);

                dto.setCounselOrderStatus(status);
                dto.setOrderId(orderId);

                if (counselorType != 0) {
                    dto.setCounselorType(counselorType);
                }

                App.getInstance().db.save(dto);

            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取访客总数已经新增访客等访客信息
     */
    public static Visits getVisits(int userid) {
        Visits dto = null;
        try {
            dto = App.getInstance().db.findFirst(Selector.from(Visits.class).where("id", " =", userid));

        } catch (Exception e) {

        }

        return dto;
    }


    /**
     * 获取访客总数已经新增访客等访客信息
     */
    public static void saveOrupdateVisits(int userid, int total, int newAdd) {
        Visits dto = null;
        try {
            dto = App.getInstance().db.findFirst(Selector.from(Visits.class).where("id", " =", userid));
            if (dto != null) {

                dto.setTotal(total);
                dto.setNewadded(newAdd);
                App.getInstance().db.update(dto);

            } else {

                dto = new Visits();
                dto.setId(userid);
                dto.setTotal(total);
                dto.setNewadded(newAdd);
                App.getInstance().db.save(dto);

            }


        } catch (Exception e) {

        }
    }


    /**
     * 获取用户视频认证状态等信息
     */
    public static LoginVideoStatusDto getLoginVideoStatusDto(int userid) {
        LoginVideoStatusDto dto = null;
        try {
            dto = App.getInstance().db.findFirst(Selector.from(LoginVideoStatusDto.class).where("id", " =", userid));

        } catch (Exception e) {

        }
        return dto;
    }

    /**
     * 获取用户视频认证状态等信息
     */
    public static void saveOrUpdateLoginVideoStatusDto(int userid, int videoVerificationStatus, int isVisible) {
        LoginVideoStatusDto dto = null;
        try {
            dto = App.getInstance().db.findFirst(Selector.from(LoginVideoStatusDto.class).where("id", " =", userid));
            if (dto != null) {

                dto.setIsVisible(isVisible);
                dto.setVideoVerificationStatus(videoVerificationStatus);
                App.getInstance().db.update(dto);

            } else {
                dto = new LoginVideoStatusDto();
                dto.setId(userid);
                dto.setIsVisible(isVisible);
                dto.setVideoVerificationStatus(videoVerificationStatus);
                App.getInstance().db.save(dto);

            }


        } catch (Exception e) {

        }
    }


    /**
     * 保存附近人的筛选条件
     *
     * @param userid
     * @param filterType
     */
    public static void savePeopleFilter(int filterType, int userid) {

        try {
            PeopleFilterDto dataDb = App.db.findFirst(Selector.from(PeopleFilterDto.class).where("UserId", " =", userid));
            if (dataDb != null) {
                dataDb.setUserId(userid);
                dataDb.setFilterType(filterType);

                App.db.update(dataDb);
            } else {
                dataDb = new PeopleFilterDto();
                dataDb.setUserId(userid);
                dataDb.setFilterType(filterType);

                App.db.save(dataDb);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取附近人的筛选条件
     *
     * @return
     */
    public static int getPeopleFilter(int userid) {
        PeopleFilterDto dataDb = null;
        int filterType = -1;
        try {
            dataDb = App.db.findFirst(Selector.from(PeopleFilterDto.class).where("UserId", " =", userid));

            if (null != dataDb) {
                filterType = dataDb.getFilterType();
            }
        } catch (DbException e) {
            e.printStackTrace();
            filterType = -1;
        }
        if (-1 == filterType) {

            BaseUser baseUser = DbHelperUtils.getBaseUser(userid);

            if (baseUser != null) {
                int sex = baseUser.getSex();
                if (sex == 1) {
                    filterType = 2;
                } else if (sex == 2) {
                    filterType = 1;
                } else {
                    filterType = 0;
                }
            } else {
                filterType = 0;
            }
            savePeopleFilter(filterType, userid);
        }

        return filterType;

    }


    /**
     * 根据id获取用户的历史VIP等级
     *
     * @param userid
     * @return
     */
    public static int getOldVipPosition(int userid) {
        int vipPosition = 0;
        try {
            UserInfo loginUserInfo = App.getInstance().db.findFirst(Selector.from(UserInfo.class).where("id", " =", userid));

            if (loginUserInfo != null) {
                UserDto userInfo = JsonUtils.fromJson(loginUserInfo.getResp(), UserDto.class);
                if (userInfo != null) {
                    vipPosition = userInfo.getLastUserVipPosition();
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return vipPosition;
    }


    /**
     * 保存活动列表数据
     *
     * @param userId
     * @param dataStr
     */
    public static void saveIndexActivities(int userId, String dataStr) {
        ActivitiesDto activitiesDto;
        try {
            activitiesDto = App.getInstance().db.findFirst(Selector.from(ActivitiesDto.class).where("userId", "=", userId));
            if (null != activitiesDto) {
                activitiesDto.setUserId(userId);
                activitiesDto.setResp(dataStr);
                App.getInstance().db.update(activitiesDto);
            } else {
                activitiesDto = new ActivitiesDto();
                activitiesDto.setUserId(userId);
                activitiesDto.setResp(dataStr);
                App.getInstance().db.save(activitiesDto);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取活动列表数据
     *
     * @param userId
     * @return
     */
    public static ActivityResp.IndexActivitiesResp getIndexActivities(int userId) {
        ActivitiesDto activitiesDto;

        ActivityResp.IndexActivitiesResp dto = null;
        try {
            activitiesDto = App.getInstance().db.findFirst(Selector.from(ActivitiesDto.class).where("userId", "=", userId));
            if (null != activitiesDto) {
                String jsonStr = activitiesDto.getResp();
                if (!TextUtils.isEmpty(jsonStr)) {
                    ActivityResp dbDto = JsonUtils.fromJson(jsonStr, ActivityResp.class);

                    if (null != dbDto && null != dbDto.getResp()) {
                        dto = dbDto.getResp();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

    /**
     * 保存我的活动列表
     *
     * @param userId
     * @param dataStr
     */
    public static void saveMyActiviteis(int userId, String dataStr) {
        MyActivitiesDto myActivitiesDto;
        try {
            myActivitiesDto = App.getInstance().db.findFirst(Selector.from(MyActivitiesDto.class).where("userId", "=", userId));
            if (null != myActivitiesDto) {
                myActivitiesDto.setUserId(userId);
                myActivitiesDto.setResp(dataStr);
                App.getInstance().db.update(myActivitiesDto);
            } else {
                myActivitiesDto = new MyActivitiesDto();
                myActivitiesDto.setUserId(userId);
                myActivitiesDto.setResp(dataStr);
                App.getInstance().db.save(myActivitiesDto);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取活动列表数据
     *
     * @param userId
     * @return
     */
    public static ActivityRespBean getMyActivities(int userId) {

        MyActivitiesDto myActivitiesDto;

        ActivityRespBean dto = null;
        try {
            myActivitiesDto = App.getInstance().db.findFirst(Selector.from(MyActivitiesDto.class).where("userId", "=", userId));
            if (null != myActivitiesDto) {
                String jsonStr = myActivitiesDto.getResp();
                if (!TextUtils.isEmpty(jsonStr)) {
                    MyActivityResp dbDto = JsonUtils.fromJson(jsonStr, MyActivityResp.class);
                    if (null != dbDto && null != dbDto.getResp()) {
                        dto = dbDto.getResp();
                        LogUtils.e("打印得到的信息：：：："+dto.toString());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }
}
