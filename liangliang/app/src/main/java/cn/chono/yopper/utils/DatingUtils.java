package cn.chono.yopper.utils;

import cn.chono.yopper.data.AppointDetailDto;
import cn.chono.yopper.data.AppointmentDto;
import cn.chono.yopper.common.Constant;

/**
 * Created by sunquan on 16/5/10.
 */
public class DatingUtils {

    public static String getChatDatingTitle(AppointmentDto dto) {

        String title = "邀约";

        switch (dto.getActivityType()) {
            case Constant.APPOINT_TYPE_MARRIED:
                title = "约定一生";

                break;
            case Constant.APPOINT_TYPE_TRAVEL:


                if (!CheckUtil.isEmpty(dto.getTravel().getAddress())) {

                    title = "约人旅行 · " + dto.getTravel().getAddress();

                } else {

                    title = "约人旅行";
                }
                break;
            case Constant.APPOINT_TYPE_OTHERS:

                title = dto.getOther().getTheme();

                break;

            case Constant.APPOINT_TYPE_MOVIE:

                //电影名称
                int mysex_movie = dto.getOwner().getSex();

                if (mysex_movie != dto.getMovie().getTargetSex()) {
                    title = "约人看电影 · " + dto.getMovie().getName();
                } else {
                    title = "看电影 · " + dto.getMovie().getName();
                }


                break;

            case Constant.APPOINT_TYPE_DOG:


                title = dto.getWalkTheDog().getDogType();


                break;

            case Constant.APPOINT_TYPE_FITNESS:


                title = dto.getSports().getTheme();


                break;

            case Constant.APPOINT_TYPE_KTV:

                //约人K歌
                int mysex_ktv = dto.getOwner().getSex();

                if (mysex_ktv != dto.getSinging().getTargetSex()) {
                    title = "约人K歌";
                } else {
                    title = "K歌";
                }


                break;

            case Constant.APPOINT_TYPE_EAT:

                //约人吃饭
                int mysex_eat = dto.getOwner().getSex();

                if (mysex_eat != dto.getDine().getTargetSex()) {

                    title = "约人吃饭";

                } else {

                    title = "吃美食";

                }

                break;
        }

        return title;
    }


    public static String getDatingTitle(AppointDetailDto dto, int sex) {

        String title = "邀约";

        switch (dto.getActivityType()) {

            case Constant.APPOINT_TYPE_MARRIED:

                //对他的寄语
                title = dto.getMarriage().getWish();

                break;
            case Constant.APPOINT_TYPE_TRAVEL:

                //旅行意义
                String meaningStr = "";
                String[] meaningTags = dto.getTravel().getMeaningTags();
                if (meaningTags != null && meaningTags.length > 0) {
                    for (int k = 0; k < meaningTags.length; k++) {
                        if (meaningTags.length == 0) {
                            meaningStr = meaningStr + meaningTags[k];
                        } else {
                            meaningStr = meaningStr + "," + meaningTags[k];
                        }

                    }
                }


                if (!CheckUtil.isEmpty(dto.getTravel().getAddress())) {

                    title = dto.getTravel().getAddress() + " " + meaningStr;

                } else {
                    title = "约人旅行" + " " + meaningStr;

                }


                break;
            case Constant.APPOINT_TYPE_OTHERS:

                //其他主题
                title = dto.getOther().getTheme();

                break;

            case Constant.APPOINT_TYPE_MOVIE:

                //电影名称

                if (sex != dto.getMovie().getTargetSex()) {
                    title = "约人看电影 " + dto.getMovie().getName();

                } else {
                    title = "看电影" + " " + dto.getMovie().getName();
                }


                break;

            case Constant.APPOINT_TYPE_DOG:

                //犬类别
                title = dto.getWalkTheDog().getDogType();

                break;

            case Constant.APPOINT_TYPE_FITNESS:

                //运动项目
                title = dto.getSports().getTheme();

                break;

            case Constant.APPOINT_TYPE_KTV:
                
                //约人K歌

                if (sex != dto.getSinging().getTargetSex()) {

                    title = "约人K歌";

                } else {

                    title = "K歌";
                }

                break;

            case Constant.APPOINT_TYPE_EAT:

                //约人吃饭

                if (sex != dto.getDine().getTargetSex()) {

                    title = "约人吃饭";

                } else {

                    title = "吃美食";

                }

                break;
        }

        return title;
    }
}
