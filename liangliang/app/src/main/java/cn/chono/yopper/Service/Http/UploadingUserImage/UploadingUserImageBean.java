package cn.chono.yopper.Service.Http.UploadingUserImage;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/22.
 */
public class UploadingUserImageBean extends ParameterBean{

    private  String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
