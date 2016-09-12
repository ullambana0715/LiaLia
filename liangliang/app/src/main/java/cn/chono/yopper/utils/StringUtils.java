package cn.chono.yopper.utils;

import android.text.TextUtils;

/**
 * Created by cc on 16/7/19.
 */
public class StringUtils {

    public static String isEmpty(String str) {

        if (TextUtils.isEmpty(str))

            return "";

        else

            return str;
    }

}
