package me.nereo.multi_image_selector.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by cc on 16/8/8.
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context,String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
