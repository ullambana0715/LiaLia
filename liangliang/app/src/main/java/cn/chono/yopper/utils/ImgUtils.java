package cn.chono.yopper.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.lidroid.xutils.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.chono.yopper.common.YpSettings;

public class ImgUtils<T> {

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(Bitmap bm, String filePath) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }


    public static Bitmap resizesBitmap(String srcPath) {

        if (CheckUtil.isEmpty(srcPath)) {
            return null;
        }

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空


        int w = newOpts.outWidth;
        int h = newOpts.outHeight;


        float ww = 960f;//这里设置宽度为960f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w >= h) {
            if (w > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outWidth / ww);
            }
        } else {
            if (h > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outHeight / ww);
            }
        }
        if (be == 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inJustDecodeBounds = false;

        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        int degree = readPictureDegree(srcPath);
        bitmap = rotaingImageView(degree, bitmap);

        return bitmap;//压缩好比例大小后再进行质量压缩
    }


    /**
     * 不做任何压缩处理的bitmap
     *
     * @param @param  context
     * @param @param  bitmap
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: resizeBitmapImgformer
     * @Title: saveImgFile
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */

    public static String saveImgFile(Context context, Bitmap bitmap) {
        String cachePath = makeRootDirectory(context);
        String fileName = System.currentTimeMillis() + ".jpg";
        if (!TextUtils.isEmpty(cachePath)) {
            File file = null;
            try {
                file = new File(cachePath + fileName);
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (null != bitmap) {

                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);

                    fOut.flush();
                    fOut.close();

                    LogUtils.e("存的图片大小=" + file.length());

                    // 回收内存空间
                    bitmap.recycle();
                    System.gc();
                    return cachePath + fileName;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        return null;
    }


    public static String saveVideoImgFile(Context context, Bitmap bitmap) {
        String cachePath = makeRootDirectory(context);
        String fileName = System.currentTimeMillis() + ".jpg";
        if (!TextUtils.isEmpty(cachePath)) {
            File file = null;
            try {
                file = new File(cachePath + fileName);
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (null != bitmap) {

                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);

                    fOut.flush();
                    fOut.close();

                    LogUtils.e("存的图片大小=" + file.length());

//					// 回收内存空间
//					bitmap.recycle();
//					System.gc();
                    return cachePath + fileName;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        return null;
    }


    /**
     * 创建路径
     *
     * @return void 返回类型
     * @throws
     * @Title: makeRootDirectory
     */
    public static String makeRootDirectory(Context context) {
        String cachePath = "";
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            // SD卡上的缓存目录
            cachePath = android.os.Environment.getExternalStorageDirectory() + "/" + YpSettings.IMAGE_CACHE_DIRECTORY_ICON;
        } else {
            // 如没有存储卡，则在私有目录中放置缓存
            cachePath = context.getFilesDir().getPath() + "/" + YpSettings.IMAGE_CACHE_DIRECTORY_ICON;
        }

        File file = null;
        try {
            file = new File(cachePath);

            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return cachePath;
    }


    /**
     * @throws
     * @Title: fileIsExists
     * @Description: 判断图片是否存在(这里用一句话描述这个方法的作用)
     * @param: @param filePath 图片路径
     * @param: @return
     * @return: boolean
     */
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        return true;

    }


    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
        }

        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = null;
        try {
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {

        }

        return resizedBitmap;
    }


    /**
     * 处理图片url
     *
     * @param url
     * @param iw
     * @return
     */

    public static String DealImageUrl(String url, int iw) {
        return DealImageUrl(url, iw, -1);
    }

    public static String DealImageUrl(String url, int iw, int ih) {

        if (CheckUtil.isEmpty(url)) {
            return "";
        }
        boolean ishasC = false;
        boolean ishasW = false;
        boolean ishasH = false;

        // 如果传过来的高度为-1 则去除url中h参数
        boolean isH_no_need = false;

        if (ih == -1) {
            isH_no_need = true;
        }

        String[] paramsList = {"cx=", "cy=", "cw=", "ch="};

        String imageUrl = "";
        String params[];

        for (int i = 0; i < paramsList.length; i++) {
            if (url.indexOf(paramsList[i]) != -1) {
                // 包含
                ishasC = true;
                break;
            }
        }

        if (url.indexOf("&w=") != -1 || url.indexOf("?w=") != -1) {
            // 包含
            ishasW = true;
        }

        if (url.indexOf("&h=") != -1 || url.indexOf("?h=") != -1) {
            // 包含
            ishasH = true;
        }

        if (ishasC) {

            if (ishasW == false && ishasH == false) {

                if (isH_no_need) {
                    imageUrl = url + "&w=" + iw;
                } else {
                    imageUrl = url + "&w=" + iw + "&h=" + ih;
                }

            } else {
                String[] urlList = url.split("\\?");

                imageUrl = urlList[0] + "?";
                params = urlList[1].split("&");

                int size = params.length;
                for (int i = 0; i < size; i++) {
                    String[] dealparams = params[i].split("=");
                    if (dealparams[0].equals("w")) {
                        dealparams[1] = iw + "";
                        params[i] = dealparams[0] + "=" + dealparams[1];
                    } else if (dealparams[0].equals("h")) {

                        if (isH_no_need) {
                            dealparams[0] = "";
                            dealparams[1] = "";
                            params[i] = dealparams[0] + dealparams[1];
                        } else {
                            dealparams[1] = ih + "";
                            params[i] = dealparams[0] + "=" + dealparams[1];
                        }

                    } else {
                        params[i] = dealparams[0] + "=" + dealparams[1];
                    }

                    if (i == size - 1) {
                        imageUrl = imageUrl + params[i];
                    } else {
                        imageUrl = imageUrl + params[i] + "&";
                    }

                }

                if (ishasW == false) {
                    imageUrl = imageUrl + "&w=" + iw;
                }
                if (ishasH == false) {

                    if (!isH_no_need) {

                        imageUrl = imageUrl + "&h=" + ih;
                    }

                }
            }

        } else {

            if (ishasW == false && ishasW == false) {

                if (isH_no_need) {
                    imageUrl = url + "?w=" + iw;
                } else {
                    imageUrl = url + "?w=" + iw + "&h=" + ih;
                }

            } else {
                String[] urlList = url.split("\\?");

                imageUrl = urlList[0] + "?";
                params = urlList[1].split("&");

                int size = params.length;
                for (int i = 0; i < size; i++) {
                    String[] dealparams = params[i].split("=");
                    if (dealparams[0].equals("w")) {
                        dealparams[1] = iw + "";
                    }
                    if (dealparams[0].equals("h")) {
                        dealparams[1] = ih + "";
                    }
                    params[i] = dealparams[0] + "=" + dealparams[1];
                    if (i == size - 1) {
                        imageUrl = imageUrl + params[i];
                    } else {
                        imageUrl = imageUrl + params[i] + "&";
                    }

                }

                if (ishasW == false) {
                    imageUrl = imageUrl + "&w=" + iw;
                }
                if (ishasH == false) {
                    imageUrl = imageUrl + "&h=" + ih;
                }
            }

        }

        return imageUrl;

    }


    /**
     * 获得路径
     *
     * @param
     * @return
     */
    public static String parseImgPath(Context context, Uri uri) {
        String path = null;

        if (uri != null) {

            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {

                try {
                    if (cursor.moveToFirst()) {

                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        path = cursor.getString(column_index);
                    }
                } catch (IllegalArgumentException e) {
                    LogUtils.e(e.toString());
                    path = null;
                } finally {
                    cursor.close();
                }

            }

        }
        return path;
    }


    /**
     * 趣拍服务器 缩缩图
     *
     * @param url
     * @param iw
     * @param ih
     * @return
     */
    public static String DealVideoImageUrl(String url, int iw, int ih) {

        if (CheckUtil.isEmpty(url)) {
            return "";
        }


        StringBuffer sb = new StringBuffer();

        sb.append(url);

        sb.append("@");

        sb.append(iw);

        sb.append("w_");

        sb.append(ih);

        sb.append("h");


        return sb.toString();


    }


}
