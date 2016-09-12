package cn.chono.yopper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by cc on 16/6/22.
 */
public class AppUtils {

    /**
     * Activity界面的跳转
     *
     * @param firstActivity 原目标 Activity
     * @param nextActivtiy  指向目标Activity
     * @param bundle
     * @param flagType      1 ABCD D跳B 则剩AB  2 ABCD C跳D 则剩D
     */

    public static void jump(Context firstActivity, Class<? extends Activity> nextActivtiy, Bundle bundle, int flagType) {


        try {
            Intent intent = new Intent();

            intent.setClass(firstActivity, nextActivtiy);

            if (bundle == null) {

                bundle = new Bundle();

            }

            intent.putExtras(bundle);

            switch (flagType) {

                case 1:// ABCD D跳B 则剩AB

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    break;

                case 2:// ABCD C跳D 则剩D

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    break;

            }

            firstActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Activity界面的跳转
     * @param firstActivity 原目标 Activity
     * @param nextActivtiy  指向目标Activity
     * @param flagType 1 ABCD D跳B 则剩AB  2 ABCD C跳D 则剩D
     */
    public static void jump(Context firstActivity, Class<? extends Activity> nextActivtiy, int flagType) {
        jump(firstActivity, nextActivtiy, null,  flagType);
    }   /**
     * Activity界面的跳转
     *
     * @param firstActivity 原目标 Activity
     * @param nextActivtiy  指向目标Activity
     * @param bundle
     */
    public static void jump(Context firstActivity, Class<? extends Activity> nextActivtiy, Bundle bundle) {
        jump(firstActivity, nextActivtiy, bundle, 0);
    }




    /**
     * Activity界面的跳转
     *
     * @param firstActivity 原目标 Activity
     * @param nextActivtiy  指向目标Activity
     */
    public static void jump(Context firstActivity, Class<? extends Activity> nextActivtiy) {
        jump(firstActivity, nextActivtiy, null, 0);
    }

    /**
     * Activity界面的跳转 需要回参
     *
     * @param firstActivity 原目标 Activity
     * @param nextActivtiy  指向目标Activity
     * @param bundle
     * @param flagType      1 ABCD D跳B 则剩AB  2 ABCD C跳D 则剩D
     * @param requestCode
     */

    public static void jumpForResult(Context firstActivity, Class<? extends Activity> nextActivtiy, Bundle bundle, int flagType, int requestCode) {
        try {
            Intent intent = new Intent();

            intent.setClass(firstActivity, nextActivtiy);

            if (bundle == null) {

                bundle = new Bundle();

            }

            intent.putExtras(bundle);

            switch (flagType) {

                case 1:// ABCD D跳B 则剩AB

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    break;

                case 2:// ABCD C跳D 则剩D

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    break;

            }

            if (firstActivity instanceof Activity) {

                Activity activity = (Activity) firstActivity;

                activity.startActivityForResult(intent, requestCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Activity界面的跳转 需要回参
     *
     * @param firstActivity 原目标 Activity
     * @param nextActivtiy  指向目标Activity
     * @param bundle
     * @param requestCode
     */

    public static void jumpForResult(Context firstActivity, Class<? extends Activity> nextActivtiy, Bundle bundle, int requestCode) {
        jumpForResult(firstActivity, nextActivtiy, bundle, 0, requestCode);
    }

    /**
     * Activity界面的跳转 需要回参
     *
     * @param firstActivity 原目标 Activity
     * @param nextActivtiy  指向目标Activity
     * @param requestCode
     */
    public static void jumpForResult(Context firstActivity, Class<? extends Activity> nextActivtiy, int requestCode) {
        jumpForResult(firstActivity, nextActivtiy, null, 0, requestCode);
    }



    /**
     * 将dip转换为像素
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue,Context context) {
        float scale =context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将像素转换为dip
     *
     * @param pxvalue
     * @return
     */
    public static float px2dip(int pxvalue,Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return pxvalue / scale;
    }

}
