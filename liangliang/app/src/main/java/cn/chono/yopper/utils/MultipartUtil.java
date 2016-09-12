package cn.chono.yopper.utils;

import android.text.TextUtils;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class MultipartUtil {


    /**
     * 单张上传
     * @param path
     * @param key
     * @return
     */
    public static Map<String, RequestBody> getFilesBody(String path, String key) {

        Map<String, RequestBody> bodys = new IdentityHashMap<>();

        File file = new File(path);

        RequestBody requestBody =
                RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
        bodys.put(key + "\"; filename=\"" + file.getName() + "", requestBody);

        return bodys;

    }

    /**
     * 批量上传
     * 生成上传文件
     * 纯文件
     *
     * @param path
     * @return
     */
    public static Map<String, RequestBody> getFilesBody(List<String> path, String key) {
        Map<String, RequestBody> bodys = new IdentityHashMap<>();
        List<File> files = getFiles(path);
        for (File file : files) {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
            bodys.put(key + "\"; filename=\"" + file.getName() + "", requestBody);
        }
        return bodys;
    }

    private static List<File> getFiles(List<String> path) {
        List<File> files = new ArrayList<>();
        for (String s : path) {
            if (TextUtils.isEmpty(s)) {
                continue;
            }
            File file = new File(s);
            if (file.exists())
                files.add(file);
        }
        return files;
    }

    /**
     * 带其他参数类型
     *
     * @param path
     * @param options
     * @param fileKey
     * @return
     */
    public static Map<String, RequestBody> getFilesRequestBody(List<String> path, Map<String, String> options, String fileKey) {
        Map<String, RequestBody> bodys = new IdentityHashMap<>();
        Iterator<Map.Entry<String, String>> iterator = options.entrySet().iterator();
        //遍历添加普通参数
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            bodys.put(next.getKey(), RequestBody.create(MediaType.parse("text/plain"), next.getValue()));

        }
        if (null != path && path.size() > 0) {
            List<File> files = getFiles(path);
            for (File file : files) {
                RequestBody fileBody =
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                //bodys.put("name=\"\"; filename=\"7411759_164157418126_2.jpg", fileBody);
                bodys.put(fileKey + "\"; filename=\"" + file.getName() + "", fileBody);


            }
        }
        //添加文件参数

        return bodys;
    }


    /**
     * 带其他参数类型
     *
     * @param path
     * @param options
     * @param fileKey
     * @return
     */
    public static Map<String, RequestBody> getFilesRequestBody(String path, Map<String, String> options, String fileKey) {
        List<String> files = new ArrayList<>();
        files.add(path);


        return getFilesRequestBody(files, options, fileKey);
    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
