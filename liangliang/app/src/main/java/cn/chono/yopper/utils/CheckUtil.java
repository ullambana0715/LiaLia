package cn.chono.yopper.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 输入数据check
 *
 * @author SQ
 */
public class CheckUtil {

    private static final String[] invalidChar = {"&", "?", "=", "/", ":", ";",
            ".", "\\", ",", "<", ">", "~", "`", "!", "@", "#", "$", "%", "^",
            "。", "，", "！", "？", "："};
    private static List<String> invalidCharList = new ArrayList<String>();
    private static char[] numbersAndLetters = null;
    private static Object initLock = new Object();
    private static Random randGen = null;

    static {
        invalidCharList = java.util.Arrays.asList(invalidChar);
    }


    /**
     * 是否是字母或数字
     *
     * @param str
     * @return
     */
    public static boolean isNumOrWord(String str) {
        String regularExpression = "[a-z0-9A-Z]*";

        if (isEmpty(str)) {
            return false;
        } else if (!str.matches(regularExpression)) {
            return false;
        }
        return true;
    }


    public static boolean isNumeric(String str) {
        if (str.matches("\\d*")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断email格式
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        String regularExpression = "^([a-z0-9A-Z]+[-|//.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?//.)+[a-zA-Z]{2,}$";

        if (isEmpty(str)) {
            return false;
        } else if (!str.matches(regularExpression)) {
            return false;
        }
        return true;
    }

    /**
     * 字符数是否在范围之间
     *
     * @param str
     * @param begin
     * @param end
     * @return
     */
    public static boolean isLengthBetween(String str, int begin, int end) {
        if (isEmpty(str)) {
            return false;
        } else if (str.length() < begin || str.length() > end) {
            return false;
        }
        return true;
    }

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return false;
        }
        return true;
    }


    /**
     * 是否是电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        if (Pattern.matches("\\d+\\-?\\d+", phone == null ? "" : phone)) {
            return true;
        }
        return false;
    }


    /**
     * 是否是手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isCellPhone(String phone) {
        if (Pattern.matches("\\d{11}", phone == null ? "" : phone)) {
            return true;
        }
        return false;
    }

    /**
     * 产生不重复随机数
     *
     * @param length
     * @return String类型随机数
     */
    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        // Init of pseudo random number generator.
        if (randGen == null) {
            synchronized (initLock) {
                if (randGen == null) {
                    randGen = new Random();
                    // Also initialize the numbersAndLetters array
                    numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
                            + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                            .toCharArray();
                }
            }
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }


    public static boolean existSDcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 小数 四舍五入
     *
     * @param val
     * @param precision
     * @return
     */

    public static Double roundDouble(double val, int precision) {

        Double ret = null;
        try {
            double factor = Math.pow(10, precision);
            ret = Math.floor(val * factor + 0.5) / factor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String ConstellationMatching(int constellation) {
        String matching = "";
        switch (constellation) {

            case 1:
                matching = "白羊";
                break;

            case 2:
                matching = "金牛";
                break;
            case 3:
                matching = "双子";
                break;
            case 4:
                matching = "巨蟹";
                break;
            case 5:
                matching = "狮子";
                break;
            case 6:
                matching = "处女";
                break;
            case 7:
                matching = "天秤";
                break;
            case 8:
                matching = "天蝎";
                break;
            case 9:
                matching = "射手";
                break;
            case 10:
                matching = "摩羯";
                break;
            case 11:
                matching = "水瓶";

                break;
            case 12:
                matching = "双鱼";

                break;

            default:
                break;
        }

        return matching;

    }


    public static String getSpacingTool(double spacing) {

        String S;

        double spac = roundDouble(spacing, 1);

        if (spac < 1) {

            S = (int) (spac * 1000) + "m";

        } else if (spac >= 1 && spac < 5) {

            S = spac + "km";

        } else {

            S = (int) spac + "km";
        }

        return S;

    }


    /***
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    public static String splitStringWithNum(String inputStr, int num) {

        String outPutStr = "";

        String regex = "(.{" + num + "})";
        outPutStr = inputStr.replaceAll(regex, "$1 ");

        return outPutStr;
    }

}
