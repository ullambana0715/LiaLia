package cn.chono.yopper.utils;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.data.UserDto;
import cn.chono.yopper.common.Constant;
import cn.chono.yopper.data.UserPhoto;

/**
 * Created by sunquan on 16/5/24.
 */
public class UserInfoUtils {

    public static List<String> userPhotoToAlbum(UserDto dto) {

        List<String> album = new ArrayList<String>();// 包括标记符的

        if (dto.getPhotos() != null && dto.getPhotos().size() > 0) {

            for (int i = 0; i < dto.getPhotos().size(); i++) {

                album.add(dto.getPhotos().get(i).getImageUrl());
            }
        }

        return album;
    }



    /**
     * 判断该相册某种照片是否可以查看
     *
     * @param dto
     * @param position
     * @return
     */
    public static boolean iscanLook(UserDto dto, int position) {

        if (dto != null && !CheckUtil.isEmpty(dto.getAlbumMask())) {

            String albumMask = dto.getAlbumMask();

            char mask = albumMask.charAt(position);

            if (Integer.valueOf(String.valueOf(mask)) == 1) {

                return true;

            } else {

                return false;
            }

        } else {

            return false;
        }
    }


    /**
     * 通过年龄返回时几几后 如90后
     *
     * @param age
     * @return
     */
    public static String getBornPeriod(int age) {
        String age_str = "";
        String lone_str = "";
        int born_year = TimeUtils.getCurrYear() - age;

        int shiwei_num = (born_year / 10) % 10;
        int gewei_num = born_year % 10;


        if (gewei_num >= 0 && gewei_num < 5) {
            lone_str = "0后";
        } else {
            lone_str = "5后";
        }
        age_str = shiwei_num + lone_str;
        return age_str;

    }


    /**
     * 通过收入类型 返回收入字符串
     *
     * @param income_id
     * @return
     */
    public static String getIncome(int income_id) {

        String incomeStr = "";

        switch (income_id) {
            case Constant.Income_Type_secrecy:
                incomeStr = "保密";
                break;

            case Constant.Income_Type_3000down:
                incomeStr = "3000元以下";
                break;

            case Constant.Income_Type_3000up:
                incomeStr = "3000元以上";
                break;

            case Constant.Income_Type_5000up:
                incomeStr = "5000元以上";
                break;

            case Constant.Income_Type_10000up:
                incomeStr = "10000元以上";
                break;

            case Constant.Income_Type_20000up:
                incomeStr = "20000元以上";
                break;

            case Constant.Income_Type_50000up:
                incomeStr = "50000元以上";
                break;
            default:
                incomeStr = "未填写";
                break;

        }

        return incomeStr;
    }


    /**
     * 通过情感类型 返回情感状态字符串
     *
     * @param emotional_id
     * @return
     */

    public static String getEmotional(int emotional_id) {

        String emotional = "";

        switch (emotional_id) {
            case Constant.Emotional_Type_secrecy:
                emotional = "保密";

                break;

            case Constant.Emotional_Type_lone:

                emotional = "单身";

                break;

            case Constant.Emotional_Type_married:

                emotional = "已婚";

                break;

            case Constant.Emotional_Type_loving:

                emotional = "恋爱中";

                break;

            case Constant.Emotional_Type_Gay:

                emotional = "同性";

                break;

            default:

                emotional = "未填写";

                break;

        }

        return emotional;
    }


    /**
     * 获取相册可以查看的图片list
     *
     * @param albumlist
     * @param albumMask
     * @return
     */
    public static List<String> getCanLookList(List<String> albumlist, String albumMask) {

        List<String> newlist = new ArrayList<String>();

        for (int i = 0; i < albumlist.size(); i++) {

            String mask = String.valueOf(albumMask.charAt(i));

            if (Integer.valueOf(mask) == 1) {

                newlist.add(albumlist.get(i));

            }
        }
        return newlist;
    }


    /**
     * 获取相册可以查看的图片list
     *
     * @param albumlist
     * @param albumMask
     * @return
     */
    public static List<UserPhoto> getAlbumCanLookList(List<UserPhoto> albumlist, String albumMask) {

        List<UserPhoto> newlist = new ArrayList<UserPhoto>();

        for (int i = 0; i < albumlist.size(); i++) {

            String mask = String.valueOf(albumMask.charAt(i));

            if (Integer.valueOf(mask) == 1) {

                newlist.add(albumlist.get(i));

            }
        }
        return newlist;
    }


    /**
     * 获取图片在相册list中的位置
     *
     * @param albumlist
     * @param url
     * @return
     */
    public static int getUrlPosition(List<String> albumlist, String url) {

        int position = 0;

        for (int i = 0; i < albumlist.size(); i++) {

            if (url.equals(albumlist.get(i))) {

                position = i;

                break;
            }
        }
        return position;
    }
  /**
     * 获取图片在相册list中的位置
     *
     * @param albumlist
     * @param url
     * @return
     */
    public static int getAlbumUrlPosition(List<UserPhoto> albumlist, String url) {

        int position = 0;

        for (int i = 0; i < albumlist.size(); i++) {

            if (url.equals(albumlist.get(i).getImageUrl())) {

                position = i;

                break;
            }
        }
        return position;
    }


}
