package cn.chono.yopper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudioFileUtils {

    /**
     * 判断语音文件是否存在
     * @param filePath
     * @return
     */
    public static boolean isMyAudioFileExists( String filePath) {
        if (CheckUtil.isEmpty(filePath)) {
            return false;
        }
        File dir = new File(filePath);
        if (!dir.exists()) {
            return false;
        }
        return true;
    }

}
